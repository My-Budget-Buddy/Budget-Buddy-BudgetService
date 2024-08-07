package com.skillstorm.budgetservice.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.budgetservice.dto.TransactionDTO;
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
     * @RequestHeader headerUserId the ID of the user
     * @return a ResponseEntity containing the Budgets list and the HTTP status code
     */
    @GetMapping("/userBudgets")
    public ResponseEntity<List<Budget>> getBudgetsById(
            @RequestHeader(name = "User-ID") Integer headerUserId) {

        List<Budget> budgets = budgetService.findBudgetsByUserId(headerUserId);
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
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budget,
            @RequestHeader(name = "User-ID") Integer headerUserId) {
        Budget newBudget = budgetService.saveBudget(budget, headerUserId);
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
    public ResponseEntity<Budget> editBudget(@RequestBody Budget budget, @PathVariable int id,
            @RequestHeader(name = "User-ID") Integer headerUserId) {

        Budget updatedBudget = budgetService.editBudget(id, budget);
        return new ResponseEntity<Budget>(updatedBudget, HttpStatus.OK);
    }

    /**
     * Deletes a budget from the database by their ID.
     *
     * @param id the ID of the budget to be deleted
     * @return a ResponseEntity object with no content and the corresponding HTTP
     *         status code/monthyear/{monthYear}/user/{userId}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Budget> deleteBudget(@PathVariable int id,
            @RequestHeader(name = "User-ID") String headerUserId) {

        budgetService.deleteBudgetById(id);
        return ResponseEntity.noContent().build();
    }

    /*
     * Controller to recieve the budgets a user has for a specific month and year
     */
    @GetMapping("/monthyear/{monthYear}")
    public ResponseEntity<List<Budget>> getBudgetsByMonthYear(@PathVariable String monthYear,
            @RequestHeader(name = "User-ID") Integer headerUserId) {

        // Define a DateTimeFormatter to parse the date in the format yyyy-MM-dd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parse the monthYear string to a LocalDate object by appending "-01"
        // This ensures the date is in the format yyyy-MM-dd for LocalDate parsing
        LocalDate date = LocalDate.parse(monthYear + "-01", formatter);
        List<Budget> budgets = budgetService.getBudgetsByMonthYearAndUserId(date, headerUserId);

        return new ResponseEntity<List<Budget>>(budgets, HttpStatus.OK);
    }

    /*
     * Controller to recieve the transactions a user had for a specific month year
     * related to categories of budgets
     * 
     */
    @GetMapping("transactions/{monthYear}")
    public ResponseEntity<List<TransactionDTO>> getTranscationsByMonthYear(@PathVariable String monthYear,
            @RequestHeader(name = "User-ID") Integer headerUserId) {

        // Define a DateTimeFormatter to parse the date in the format yyyy-MM-dd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parse the monthYear string to a LocalDate object by appending "-01"
        // This ensures the date is in the format yyyy-MM-dd for LocalDate parsing
        LocalDate date = LocalDate.parse(monthYear + "-01", formatter);
        List<TransactionDTO> transactions = budgetService.findTransactionByMonthYear(date, headerUserId);
        return new ResponseEntity<List<TransactionDTO>>(transactions, HttpStatus.OK);
    }

    /*
     * Controller to delete all budgets associated with a userId
     * 
     */
    @DeleteMapping("deleteAll/user")
    public ResponseEntity<Budget> deleteAllBudgetsByUserId(
            @RequestHeader(name = "User-ID") Integer headerUserId) {

        budgetService.deleteAllBudgetsByUserId(headerUserId);
        return ResponseEntity.noContent().build();
    }

}
