package com.skillstorm.budgetservice.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillstorm.budgetservice.dto.TransactionDTO;
import com.skillstorm.budgetservice.models.Buckets;
import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.repositories.BudgetRepository;
import com.skillstorm.exceptions.IdMismatchException;

@Service
public class BudgetService {

    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    TranscationService transcationService;

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
     * @param user The budget to be saved.
     * @return The saved budget.
     */
    public Budget saveBudget(Budget budget) {
        return budgetRepository.save(budget);
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
        List<TransactionDTO> transactions = transcationService.getTransactionsExcludingIncome(userId);

        // Filter transactions to only include those that match the specified month and
        // year
        List<TransactionDTO> filteredTransactions = transactions.stream()
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getDate(); // Assuming getDate() returns a LocalDate
                    return transactionDate.getYear() == monthYear.getYear() &&
                            transactionDate.getMonth() == monthYear.getMonth();
                })
                .collect(Collectors.toList());

        return filteredTransactions;
    }

    /**
     * Deletes all budgets by user ID.
     *
     * @param id the ID of the user
     */
    public void deleteAllBudgetsByUserId(int id) {
        budgetRepository.deleteAllBudgetsByUserId(id);
    }

    // This method checks to make sure that the user is retrieving or updating
    // information that relates to their own account. This prevents a user with an
    // ID of 1 from updating the data of a different user
    public void compareHeaderIdWithRequestedDataId(int userId, String headerUserId) {

        if (userId != Integer.valueOf(headerUserId)) {
            throw new IdMismatchException();
        }
    }
}
