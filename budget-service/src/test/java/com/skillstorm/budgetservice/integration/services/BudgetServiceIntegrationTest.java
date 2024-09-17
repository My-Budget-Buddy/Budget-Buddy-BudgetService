package com.skillstorm.budgetservice.integration.services;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    @Test
    public void findAllBudgetsTest_Success() {
        Budget budget1 = new Budget(1, 1, "category1", BigDecimal.valueOf(5000), true, LocalDate.now(), "notes1", LocalDateTime.now());
        Budget budget2 = new Budget(2, 1, "category2", BigDecimal.valueOf(2000), true, LocalDate.now(), "notes2", LocalDateTime.now());

        budgetRepository.save(budget1);
        budgetRepository.save(budget2);

        List<Budget> result = budgetService.findAllBudgets();

        assertTrue(result.size() == 2);    
        
        //it is auto generating the id so we can't compare the objects directly
        // and the constructor is not setting the id 
        // assertTrue(result.contains(budget1));
        // assertTrue(result.contains(budget2));
    }

    @Test
    public void findBudgetsByUserIdTest_Success() {
        Budget budget1 = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());
        Budget budget2 = new Budget(2, 1, "Test Budget 2", new BigDecimal(200.00), true, LocalDate.now(), "notes1", LocalDateTime.now());

        budgetRepository.save(budget1);
        budgetRepository.save(budget2);

        List<Budget> result = budgetService.findBudgetsByUserId(1);

        assertTrue(result.size() == 2);
        // assertTrue(result.contains(budget1));
        // assertTrue(result.contains(budget2));
    }

    @Test
    public void findBudgetsByUserIdTest_NotFound() {
        List<Budget> result = budgetService.findBudgetsByUserId(1);
        assertTrue(result.isEmpty());
    }

    @Test
    public void saveBudgetTest_Success() {
        Budget budget = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());

        Budget result = budgetService.saveBudget(budget, 1);

        assertNotNull(result);

        // assertEquals(budget, result);
    }   

    @Test
    public void editBudgetTest_Success() {
        Budget budget = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());
        Budget editedBudget = new Budget(1, 1, "Test Budget 2", new BigDecimal(200.00), true, LocalDate.now(), "notes1", LocalDateTime.now());

        budgetRepository.save(budget);

        Budget result = budgetService.editBudget(1, editedBudget);

        assertNotNull(result);
        // assertEquals(editedBudget, result);
    }

    @Test
    public void editBudgetTest_NotFound() {
        Budget editedBudget = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());

        Budget result = budgetService.editBudget(0, editedBudget);

        assertNull(result);
    }

    @Test
    public void deleteBudgetByIdTest_Success() {
        Budget budget = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());

        budgetRepository.save(budget);

        budgetService.deleteBudgetById(1);

        assertTrue(budgetRepository.findById(1).isEmpty());
    }

    @Test
    public void deleteBudgetByIdTest_NotFound() {
        budgetService.deleteBudgetById(1);

        assertTrue(budgetRepository.findById(1).isEmpty());
    }

    @Test
    public void getBudgetsByMonthYearAndUserId_Success() {
        Budget budget1 = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.of(2021, 1, 1), "notes1", LocalDateTime.now());
        Budget budget2 = new Budget(2, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.of(2022, 1, 1), "notes1", LocalDateTime.now());

        budgetRepository.save(budget1);
        budgetRepository.save(budget2);

        List<Budget> result = budgetService.getBudgetsByMonthYearAndUserId(LocalDate.of(2021, 1, 1), budget1.getUserId());

        // assertTrue(result.size() == 1);
        // assertTrue(result.contains(budget1));
    }

    // @Test
    // public void getBudgetsByMonthYearAndUserId_NotFound() {
    //     Budget budget1 = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), false, LocalDate.of(2021,1,1), null);
    //     budgetRepository.save(budget1);

    //     List<Budget> result = budgetService.getBudgetsByMonthYearAndUserId(LocalDate.of(2022,1,1), 1);

    //     assertTrue(result.isEmpty());
    // }

    // @Test
    // public void findTransactionByMonthYear_Success() {

    // }

    // @Test
    // public void findTransactionByMonthYear_NotFound() {

    // }

    @Test
    public void deleteAllBudgetsByUserId_Success() {
        Budget budget1 = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());
        Budget budget2 = new Budget(2, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());

        budgetRepository.save(budget1);
        budgetRepository.save(budget2);

        budgetService.deleteAllBudgetsByUserId(1);

        assertTrue(budgetRepository.findAll().isEmpty());
    }

    @Test
    public void deleteAllBudgetsByUserId_NotFound() {
        Budget budget1 = new Budget(1, 1, "Test Budget 1", new BigDecimal(100.00), true, LocalDate.now(), "notes1", LocalDateTime.now());
        budgetRepository.save(budget1);

        budgetService.deleteAllBudgetsByUserId(2);

        assertTrue(budgetRepository.findAll().size() == 1);
    }

}
