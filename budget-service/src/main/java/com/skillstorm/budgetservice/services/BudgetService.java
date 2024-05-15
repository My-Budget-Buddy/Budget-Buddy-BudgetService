package com.skillstorm.budgetservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.repositories.BudgetRepository;

@Service
public class BudgetService {

    @Autowired
    BudgetRepository budgetRepository;

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
            if (budget.getAmount() != 0) {
                existingBudget.setAmount(budget.getAmount());
            }
            if (budget.getCurrentAmount() != 0) {
                existingBudget.setCurrentAmount(budget.getCurrentAmount());
            }
            if (budget.getBudgetName() != null) {
                existingBudget.setBudgetName(budget.getBudgetName());
            }
            if (budget.getCategory() != null) {
                existingBudget.setCategory(budget.getCategory());
            }
            if (budget.getMonth() != null) {
                existingBudget.setMonth(budget.getMonth());
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

}
