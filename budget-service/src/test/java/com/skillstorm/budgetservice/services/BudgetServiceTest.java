package com.skillstorm.budgetservice.services;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.skillstorm.budgetservice.dto.TransactionDTO;
import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.repositories.BudgetRepository;

public class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private TranscationService transcationService;

    @InjectMocks
    private BudgetService budgetService;
    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }
    
    @AfterEach
    public void teardown() throws Exception {
        closeable.close();
    }

    @Test
    public void testFindAllBudgets() {
        List<Budget> budgets = Arrays.asList(new Budget(), new Budget());
        
        when(budgetRepository.findAll()).thenReturn(budgets);
        
        List<Budget> response = budgetService.findAllBudgets();

        assertEquals(budgets, response);
    }

    @Test
    public void testFindBudgetsByUserId() {
        List<Budget> budgets = Arrays.asList(new Budget(), new Budget());
        
        when(budgetRepository.findByUserId(anyInt())).thenReturn(budgets);
        
        List<Budget> response = budgetService.findBudgetsByUserId(1);

        assertEquals(budgets, response);
    }

    @Test
    public void testSaveBudget() {
        Budget budget = new Budget(1, 1, "category", BigDecimal.valueOf(5000), true, LocalDate.now(), "notes", LocalDateTime.now());

        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget response = budgetService.saveBudget(budget, 1);

        assertEquals(budget, response);
    }

    @Test
    public void testSaveBudgetHasNullFields() {
        Budget budget = new Budget(1, 1, null, BigDecimal.valueOf(0), null, null, null, null);
        
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget response = budgetService.saveBudget(budget, 1);

        assertEquals(budget, response);
    }

    @Test
    public void testEditBudget() {
        Budget budget = new Budget(1, 1, "category", BigDecimal.valueOf(5000), true, LocalDate.now(), "notes", LocalDateTime.now());

        when(budgetRepository.findById(anyInt())).thenReturn(Optional.of(budget));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget response = budgetService.editBudget(1, budget);

        assertEquals(budget, response);
    }

    @Test
    public void testEditBudgetHasNullFields() {
        Budget budget = new Budget(1, 1, null, BigDecimal.valueOf(0), null, null, null, null);

        when(budgetRepository.findById(anyInt())).thenReturn(Optional.of(budget));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget response = budgetService.editBudget(1, budget);

        assertEquals(budget, response);
    }

    @Test
    public void testEditBudgetNotPresent() {
        Budget budget = new Budget(1, 1, "category", BigDecimal.valueOf(5000), true, LocalDate.now(), "notes", LocalDateTime.now());

        when(budgetRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget response = budgetService.editBudget(1, budget);

        assertEquals(budget, response);
    }

    @Test 
    public void testDeleteBudgetById() {
        doNothing().when(budgetRepository).deleteById(anyInt());
        
        budgetService.deleteBudgetById(0);

        verify(budgetRepository).deleteById(anyInt());
    }

    @Test
    public void testGetBudgetsByMonthYearAndUserId() {
        List<Budget> budgets = Arrays.asList(new Budget(), new Budget());
        
        when(budgetRepository.findByMonthYearAndUserId(any(LocalDate.class), anyInt())).thenReturn(budgets);
        
        List<Budget> response = budgetService.getBudgetsByMonthYearAndUserId(LocalDate.now(), 1);

        assertEquals(budgets, response);
    }

    @Test
    public void testFindTransactionByMonthYear() {
        TransactionDTO transactionDTO1 = new TransactionDTO(0, 0, 0, null, 0, null, null, LocalDate.now());
        TransactionDTO transactionDTO2 = new TransactionDTO(0, 0, 0, null, 0, null, null, LocalDate.of(2000, 1, 1));
        TransactionDTO transactionDTO3 = new TransactionDTO(0, 0, 0, null, 0, null, null, LocalDate.of(LocalDate.now().getMonthValue(), 1, 1));

        List<TransactionDTO> transactionDTOs = Arrays.asList(transactionDTO1, transactionDTO2, transactionDTO1, transactionDTO3);

        when(transcationService.getTransactionsExcludingIncome(anyInt())).thenReturn(transactionDTOs);
        
        List<TransactionDTO> response = budgetService.findTransactionByMonthYear(LocalDate.now(), 0);

        assertEquals(2, response.size());
        assertEquals(transactionDTO1, response.get(0));
        assertEquals(transactionDTO1, response.get(1));
    }

    @Test
    public void testDeleteAllBudgetsByUserId() {

        budgetService.deleteAllBudgetsByUserId(0);

        verify(budgetRepository).deleteAllBudgetsByUserId(anyInt());
    }

    // @Test
    // public void testFindTransactionByMonthYear() {
    //     int userId = 1;
    //     List<TransactionDTO> transactionDTOs = Arrays.asList(new TransactionDTO(), new TransactionDTO());
        
    //     ArgumentCaptor<String> requestCaptor = ArgumentCaptor.forClass(String.class);
    //     ArgumentCaptor<Integer> messageCaptor = ArgumentCaptor.forClass(Integer.class);

    //     List<TransactionDTO> response = budgetService.findTransactionByMonthYear(LocalDate.now(), userId);
        
    //     verify(rabbitTemplate).convertAndSend(requestCaptor.capture(), messageCaptor.capture(), any(MessagePostProcessor.class));
        
    //     assertEquals(requestCaptor.getValue(), "budget-request");
    //     assertEquals(messageCaptor.getValue(), userId);
    // }
}
