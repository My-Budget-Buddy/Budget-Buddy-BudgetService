package com.skillstorm.budgetservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.services.BudgetService;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    BudgetService budgetService;

    /**
     * Retrieves all the budgets from the budget repository.
     *
     * @return a ResponseEntity object containing a list of Budget objects
     *         representing all the budgets found and the HTTP status code
     */
    @GetMapping
    public ResponseEntity<List<Budget>> findAllBudgets() {
        List<Budget> budgets = budgetService.findAllBudgets();
        return new ResponseEntity<List<Budget>>(budgets, HttpStatus.OK);
    }

    /**
     * Retrieves the Budget objects associated with the given ID.
     *
     * @param id the ID of the user
     * @return a ResponseEntity containing the Budgets list and the HTTP status code
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<Budget>> getBudgetsById(@PathVariable int id) {
        List<Budget> budgets = budgetService.findBudgetsByUserId(id);
        return new ResponseEntity<List<Budget>>(budgets, HttpStatus.OK);
    }

    /**
     * Creates a new budget by saving the provided Budget object.
     *
     * @param budget The Budget object to be created.
     * @return A ResponseEntity object containing the created budget and the HTTP
     *         status code.
     */
    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budget) {
        Budget newBudget = budgetService.saveBudget(budget);
        return new ResponseEntity<Budget>(newBudget, HttpStatus.CREATED);
    }

    /**
     * Edits the details of a budget with the given ID.
     *
     * @param id     the ID of the budget to edit
     * @param budget the updated budget object containing the new details
     * @return the updated Budget object
     */
    @PutMapping("/{id}")
    public ResponseEntity<Budget> editBudget(@PathVariable int id, @RequestBody Budget budget) {
        Budget updatedBudget = budgetService.editBudget(id, budget);
        return new ResponseEntity<Budget>(updatedBudget, HttpStatus.OK);
    }

    /**
     * Deletes a budget from the database by their ID.
     *
     * @param id the ID of the budget to be deleted
     * @return a ResponseEntity object with no content and the corresponding HTTP
     *         status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Budget> deleteBudget(@PathVariable int id) {
        budgetService.deleteBudgetById(id);
        return ResponseEntity.noContent().build();
    }

}
