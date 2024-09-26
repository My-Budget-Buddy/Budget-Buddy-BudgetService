package com.skillstorm.budgetservice.controllers.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.skillstorm.budgetservice.controllers.BudgetController;
import com.skillstorm.budgetservice.dto.TransactionDTO;
import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.services.BudgetService;

public class BudgetControllerTest {
    
    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }  

    @Test
    public void testFindAllBudgets() {
        List<Budget> budgets = Arrays.asList(new Budget(), new Budget());

        when(budgetService.findAllBudgets()).thenReturn(budgets);

        ResponseEntity<List<Budget>> response = budgetController.findAllBudgets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budgets, response.getBody());
    }

    @Test
    public void testGetBudgetsById() {
        int id = 1;
        List<Budget> budgets = Arrays.asList(new Budget(), new Budget());

        when(budgetService.findBudgetsByUserId(id)).thenReturn(budgets);

        ResponseEntity<List<Budget>> response = budgetController.getBudgetsById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budgets, response.getBody());
    }

    @Test
    public void testCreateBudget() {
        int headerUserId = 1;
        Budget budget = new Budget();

        when(budgetService.saveBudget(budget, headerUserId)).thenReturn(budget);

        ResponseEntity<Budget> response = budgetController.createBudget(budget, headerUserId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(budget, response.getBody());
    }

    @Test
    public void testEditBudget() {
        int id = 1;
        Integer headerUserId = 1;
        Budget budget = new Budget();

        when(budgetService.editBudget(id, budget)).thenReturn(budget);

        ResponseEntity<Budget> response = budgetController.editBudget(budget, id, headerUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budget, response.getBody());
    }

    @Test
    public void testDeleteBudget() {
        int id = 1;
        String headerUserId = "1";

        ResponseEntity<Budget> response = budgetController.deleteBudget(id, headerUserId);

        verify(budgetService).deleteBudgetById(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetBudgetsByMonthYear() {
        String monthYearString = "2021-01";
        LocalDate monthYear = LocalDate.of(2021, 1, 1);
        int headerUserId = 1;
        List<Budget> budgets = Arrays.asList(new Budget(), new Budget());

        when(budgetService.getBudgetsByMonthYearAndUserId(monthYear, headerUserId)).thenReturn(budgets);

        ResponseEntity<List<Budget>> response = budgetController.getBudgetsByMonthYear(monthYearString, headerUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budgets, response.getBody());
    }

    @Test
    public void testGetTransactionsByMonthYear() {
        String monthYearString = "2021-01";
        LocalDate monthYear = LocalDate.of(2021, 1, 1);
        Integer headerUserId = 1;
        List<TransactionDTO> transactionDTOs = Arrays.asList(new TransactionDTO(), new TransactionDTO());

        when(budgetService.findTransactionByMonthYear(monthYear, headerUserId)).thenReturn(transactionDTOs);

        ResponseEntity<List<TransactionDTO>> response = budgetController.getTransactionsByMonthYear(monthYearString, headerUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionDTOs, response.getBody());
    }

    @Test
    public void testDeleteAllBudgetsByUserId() {
        int userId = 1;

        ResponseEntity<Budget> response = budgetController.deleteAllBudgetsByUserId(userId);

        verify(budgetService).deleteAllBudgetsByUserId(userId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
