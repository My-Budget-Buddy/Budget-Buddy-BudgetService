package com.skillstorm.budgetservice.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.skillstorm.budgetservice.dto.TransactionDTO;
import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.repositories.BudgetRepository;

@Service
public class BudgetService {

    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    final ConcurrentMap<String, CompletableFuture<List<TransactionDTO>>> correlationMap = new ConcurrentHashMap<>();

    public List<Budget> findAllBudgets() {
        return budgetRepository.findAll();
    }

    /**
     * Finds a Budget by their ID.
     *
     * @param id the ID of the Budget to find
     * @return the Budget with the specified ID, or null if no Budget is found
     */
    public List<Budget> findBudgetsByUserId(int id) {
        return budgetRepository.findByUserId(id);
    }

    /**
     * Saves the given budget into the repository.
     *
     * @param budget The budget to be saved.
     * @param headerUserId The id of the user making the request
     * @return The saved budget.
     */
    public Budget saveBudget(Budget budget, Integer headerUserId) {
        Budget newBudget = new Budget();

        newBudget.setUserId(headerUserId);

        if (budget.getCategory() != null) {
            newBudget.setCategory(budget.getCategory());
        }
        if (budget.getTotalAmount() != null) {
            newBudget.setTotalAmount(budget.getTotalAmount());
        }
        if (budget.getIsReserved() != null) {
            newBudget.setIsReserved(budget.getIsReserved());
        }
        if (budget.getMonthYear() != null) {
            newBudget.setMonthYear(budget.getMonthYear());
        }
        if (budget.getNotes() != null) {
            newBudget.setNotes(budget.getNotes());
        }

        return budgetRepository.save(newBudget);
    }

    /**
     * Edits an existing budget in the system with the specified ID.
     *
     * @param id     The ID of the budget to edit.
     * @param budget The updated budget object
     * @return The edited budget object.
     */
    public Budget editBudget(int id, Budget budget) {

        Optional<Budget> existingBudgetOptional = budgetRepository.findById(id);

        if (existingBudgetOptional.isPresent()) {
            Budget existingBudget = existingBudgetOptional.get();

            // Check if fields in the incoming budget are not null, then update
            if (budget.getCategory() != null) {
                existingBudget.setCategory(budget.getCategory());
            }
            if (budget.getTotalAmount() != null) {
                existingBudget.setTotalAmount(budget.getTotalAmount());
            }
            if (budget.getIsReserved() != null) {
                existingBudget.setIsReserved(budget.getIsReserved());
            }
            if (budget.getMonthYear() != null) {
                existingBudget.setMonthYear(budget.getMonthYear());
            }
            if (budget.getNotes() != null) {
                existingBudget.setNotes(budget.getNotes());
            }

            return budgetRepository.save(existingBudget);
        } else {
            // If the budget does not exist, create a new one
            return budgetRepository.save(budget);
        }
    }

    /**
     * Deletes a budget by their ID.
     *
     * @param id the ID of the budget to be deleted
     */
    public void deleteBudgetById(int id) {
        budgetRepository.deleteById(id);
    }

    /**
     * Get all budgets for a user by date
     * 
     * 
     */
    public List<Budget> getBudgetsByMonthYearAndUserId(LocalDate date, int userId) {
        return budgetRepository.findByMonthYearAndUserId(date, userId);
    }

    /**
     * Get all transactions related to budget categories for a user by date
     * 
     * 
     */
    public List<TransactionDTO> findTransactionByMonthYear(LocalDate monthYear, int userId) {
        // Retrieve all transactions for the given user, excluding income transactions
        List<TransactionDTO> transactions = getUserTransactions(userId);

        // If no transactions are found, return an empty list
        if(transactions == null) {
          return List.of();
        }

        // Otherwise, filter transactions to only include those that match the specified month and
        // year
        return transactions.stream()
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getDate(); // Assuming getDate() returns a LocalDate
                    return transactionDate.getYear() == monthYear.getYear() &&
                            transactionDate.getMonth() == monthYear.getMonth();
                })
                .collect(Collectors.toList());
    }

    /**
     *  Send a request to the Transaction-Service for all transactions related to the User
     * @param userId the ID for the user making the request
     * @return the list of the user's transactions
     */
    private List<TransactionDTO> getUserTransactions(int userId) {
        // Generate a correlationId so the queue can map the correct response and store it as a future
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<List<TransactionDTO>> transactionsFuture = new CompletableFuture<>();
        correlationMap.put(correlationId, transactionsFuture);

        // Send the request to the Transactions-Service using RabbitMq:
        rabbitTemplate.convertAndSend("budget-request", userId, message -> {
            message.getMessageProperties().setCorrelationId(correlationId);
            message.getMessageProperties().setReplyTo("budget-response");
            return message;
        });

        // Message queues are async by nature, but we're in a synchronous environment and
        // the request takes time to complete. We need to tell the thread to wait or it will immediately
        // return null
        try {
            return transactionsFuture.get();
        } catch (Exception e) {
            throw new RuntimeException("Error waiting for transactions response", e);
        } finally {
            // Once the request has completed, we can remove it from our list of requests
            correlationMap.remove(correlationId);
        }
    }


    /**
     *
     * @param transactions the List of the user's transactions received from Transaction-Service
     * @param correlationId the id for the request
     */
    @RabbitListener(queues = "budget-response")
    public void handleTransactionsResponse(@Payload List<TransactionDTO> transactions, @Header(AmqpHeaders.CORRELATION_ID) String correlationId ) {
        // Find the future that was stored in the our correlation map that is relevant to the request and use it to store our response
        CompletableFuture<List<TransactionDTO>> transactionsFuture = correlationMap.remove(correlationId);
        if(transactionsFuture != null) {
            transactionsFuture.complete(transactions);
        }
    }

    /**
     * Deletes all budgets by user ID.
     *
     * @param id the ID of the user
     */
    public void deleteAllBudgetsByUserId(int id) {
        budgetRepository.deleteAllBudgetsByUserId(id);
    }

}
