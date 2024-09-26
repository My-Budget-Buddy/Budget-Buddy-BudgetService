package com.skillstorm.budgetservice.services.integration;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.repositories.BudgetRepository;
import com.skillstorm.budgetservice.services.BudgetService;

@SpringBootTest
@Transactional
public class BudgetServiceIntegrationTest {
    
    @Autowired
    private BudgetService budgetService;          // The class under test

    @Autowired
    private BudgetRepository budgetRepository;    // The class under test's dependency

    /**
     * Setup and teardown methods to clear the database before and after each test.
     */
    @BeforeEach
    public void setup() {
        budgetRepository.deleteAll();
    }

    @AfterEach
    public void teardown() {
        budgetRepository.deleteAll();
    }

    /**
     * Test case for findAllBudgets method in BudgetService class - Successfully finds all budgets.
     */
    @Test
    public void findAllBudgetsTest_Success() {
        Budget budget1 = new Budget(1, 1, "category1", BigDecimal.valueOf(5000), true, LocalDate.now(), "notes1", LocalDateTime.now());
        Budget budget2 = new Budget(2, 1, "category2", BigDecimal.valueOf(2000), true, LocalDate.now(), "notes2", LocalDateTime.now());

        budgetRepository.save(budget1);
        budgetRepository.save(budget2);

        List<Budget> result = budgetService.findAllBudgets();

        assertTrue(result.size() == 2);    
        
        assertEquals(result.get(0).getCategory().equals(budget1.getCategory()), true);
        assertEquals(result.get(1).getNotes().equals(budget2.getNotes()), true);
    }

    /**
     * Test case for findBudgetsByUserId method in BudgetService class - Successfully finds all budgets by user ID.
     */
    @Test
    public void findBudgetsByUserIdTest_Success() {
        Budget budget1 = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());
        Budget budget2 = new Budget(2, 1, "Test Budget 2", new BigDecimal(200.00), true, LocalDate.now(), "notes1", LocalDateTime.now());

        budgetRepository.save(budget1);
        budgetRepository.save(budget2);

        List<Budget> result = budgetService.findBudgetsByUserId(1);

        assertTrue(result.size() == 2);
        assertEquals(result.get(0).getCategory().equals(budget1.getCategory()), true);
        assertEquals(result.get(1).getNotes().equals(budget2.getNotes()), true);
    }

    /**
     * Test case for findBudgetsByUserId method in BudgetService class - Does not find any budget by user ID.
     */
    @Test
    public void findBudgetsByUserIdTest_NotFound() {
        List<Budget> result = budgetService.findBudgetsByUserId(1);
        assertTrue(result.isEmpty());
    }

    /**
     * Test case for saveBudget method in BudgetService class - Successfully saves a budget.
     */
    @Test
    public void saveBudgetTest_Success() {
        Budget budget = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());

        Budget result = budgetService.saveBudget(budget, 1);

        assertNotNull(result);
    }   

    /**
     * Test case for editBudget method in BudgetService class - Successfully edits a budget.
     */
    @Test
    public void editBudgetTest_Success() {
        Budget budget = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());
        Budget editedBudget = new Budget(1, 1, "Test Budget 2", new BigDecimal(200.00), true, LocalDate.now(), "notes1", LocalDateTime.now());

        budgetRepository.save(budget);

        Budget result = budgetService.editBudget(1, editedBudget);

        assertNotNull(result);
        assertEquals(editedBudget.getCategory(), result.getCategory());
    }

    /**
     * Test case for deleteBudgetById method in BudgetService class - Successfully deletes a budget by ID.
     */
    @Test
    public void deleteBudgetByIdTest_Success() {
        Budget budget = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());

        budgetRepository.save(budget);

        budgetService.deleteBudgetById(1);

        assertTrue(budgetRepository.findById(1).isEmpty());
    }

    /**
     * Test case for deleteBudgetById method in BudgetService class - Does not find any budget by ID.
     */
    @Test
    public void deleteBudgetByIdTest_NotFound() {
        budgetService.deleteBudgetById(1);

        assertTrue(budgetRepository.findById(1).isEmpty());
    }

    /**
     * Test case for getBudgetsByMonthYearAndUserId method in BudgetService class - Successfully finds budgets by month, year, and user ID.
     */
    @Test
    public void getBudgetsByMonthYearAndUserId_Success() {
        Budget budget1 = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.of(2021, 1, 1), "notes1", LocalDateTime.now());
        Budget budget2 = new Budget(2, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.of(2022, 1, 1), "notes1", LocalDateTime.now());

        budgetRepository.save(budget1);
        budgetRepository.save(budget2);

        List<Budget> result = budgetService.getBudgetsByMonthYearAndUserId(LocalDate.of(2021, 1, 1), budget1.getUserId());

        assertTrue(result.size() == 1);
        assertEquals(result.get(0).getCategory().equals(budget1.getCategory()), true);
    }

    /**
     * Test case for getBudgetsByMonthYearAndUserId method in BudgetService class - Does not find any budgets by month, year, and user ID.
     */
    @Test
    public void getBudgetsByMonthYearAndUserId_NotFound() {
        Budget budget1 = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.of(2021, 1, 1), "notes1", LocalDateTime.now());
        budgetRepository.save(budget1);

        List<Budget> result = budgetService.getBudgetsByMonthYearAndUserId(LocalDate.of(2022,1,1), 0);

        assertTrue(result.isEmpty());
    }

    /**
     * Test case for deleteAllBudgetsByUserId method in BudgetService class - Successfully deletes all budgets by user ID.
     */
    @Test
    public void deleteAllBudgetsByUserId_Success() {
        Budget budget1 = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());
        Budget budget2 = new Budget(2, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());

        budgetRepository.save(budget1);
        budgetRepository.save(budget2);

        budgetService.deleteAllBudgetsByUserId(1);
        assertTrue(budgetRepository.findAll().isEmpty());
    }

    /**
     * Test case for deleteAllBudgetsByUserId method in BudgetService class - Does not find any budgets to be deleted by user ID.
     */
    @Test
    public void deleteAllBudgetsByUserId_NotFound() {
        Budget budget1 = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());
        budgetRepository.save(budget1);

        budgetService.deleteAllBudgetsByUserId(2);
        assertTrue(budgetRepository.findAll().size() == 1);
    }

}
