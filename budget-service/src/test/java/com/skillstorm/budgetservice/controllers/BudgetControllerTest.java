package com.skillstorm.budgetservice.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.services.BudgetService;

public class BudgetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(budgetController).build();
    }

    @Test
    void testFindAllBudgets() throws Exception {
        List<Budget> budgets = Arrays.asList(
                new Budget(1, 1, "Food", BigDecimal.valueOf(100), true, LocalDate.of(2023, 5, 1), "Note 1", null),
                new Budget(2, 2, "Travel", BigDecimal.valueOf(200), false, LocalDate.of(2023, 6, 1), "Note 2", null));

        when(budgetService.findAllBudgets()).thenReturn(budgets);

        mockMvc.perform(get("/budgets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].budgetId").value(1))
                .andExpect(jsonPath("$[0].category").value("Food"))
                .andExpect(jsonPath("$[1].budgetId").value(2))
                .andExpect(jsonPath("$[1].category").value("Travel"));
    }

}
