package com.skillstorm.budgetservice.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.repositories.BudgetRepository;

public class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepo;      //mock object

    @InjectMocks
    private BudgetService budgetService;     // the tested class which the mock object will be injected to
    private AutoCloseable closeable;        // used to manage mock objects (open and close them)

    /**
     * Opening all mock objects
     */
    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deleteAllBudgetsByUserId() {
        int userId = 1;

        List<Budget> deletedBudget = Arrays.asList(new Budget(), new Budget());

        when(budgetRepo.findByUserId(userId)).thenReturn(deletedBudget);

        assertAll(() -> budgetService.deleteAllBudgetsByUserId(userId));


    }

    /**
     * Closes all mock objects
     * @throws Exception
     */
    @AfterEach
    public void teardown() throws Exception{
        closeable.close();
    }
}