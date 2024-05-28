package com.skillstorm.budgetservice.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.budgetservice.dto.TransactionDTO;
import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.services.BudgetService;

public class BudgetControllerTest {

        private MockMvc mockMvc;

        @Mock
        private BudgetService budgetService;

        @InjectMocks
        private BudgetController budgetController;

        // Utility method to convert an object to a JSON string
        private static String asJsonString(final Object obj) {
                try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.registerModule(new JavaTimeModule()); // Register the JSR310 module
                        return mapper.writeValueAsString(obj);
                } catch (Exception e) {
                        throw new RuntimeException(e);
                }
        }

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                mockMvc = MockMvcBuilders.standaloneSetup(budgetController).build();
        }

        @Test
        void testFindAllBudgets() throws Exception {
                List<Budget> budgets = Arrays.asList(
                                new Budget(1, 1, "Food", BigDecimal.valueOf(100), true, LocalDate.of(2023, 5, 1),
                                                "Note 1", null),
                                new Budget(2, 2, "Travel", BigDecimal.valueOf(200), false, LocalDate.of(2023, 6, 1),
                                                "Note 2", null));

                when(budgetService.findAllBudgets()).thenReturn(budgets);

                mockMvc.perform(get("/budgets"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].budgetId").value(1))
                                .andExpect(jsonPath("$[0].category").value("Food"))
                                .andExpect(jsonPath("$[1].budgetId").value(2))
                                .andExpect(jsonPath("$[1].category").value("Travel"));
        };

        @Test
        void testGetBudgetsById() throws Exception {
                List<Budget> budgets = Arrays.asList(
                                new Budget(1, 1, "Food", BigDecimal.valueOf(100), true, LocalDate.of(2023, 5, 1),
                                                "Note 1", null));

                when(budgetService.findBudgetsByUserId(1)).thenReturn(budgets);

                mockMvc.perform(get("/budgets/userBudgets")
                                .header("User-ID", 1))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].budgetId").value(1))
                                .andExpect(jsonPath("$[0].category").value("Food"));
        }

        @Test
        void testCreateBudget() throws Exception {

                Budget budget = new Budget(1, 1, "Food", BigDecimal.valueOf(100), true, LocalDate.of(2023, 5, 1),
                                "Note 1", null);

                when(budgetService.saveBudget(any(Budget.class), anyInt())).thenReturn(budget);

                mockMvc.perform(post("/budgets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("User-ID", 1)
                                .content(asJsonString(budget)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.budgetId").value(1))
                                .andExpect(jsonPath("$.category").value("Food"));
        }

        @Test
        void testEditBudget() throws Exception {
                Budget budget = new Budget(1, 1, "Food", BigDecimal.valueOf(100), true, LocalDate.of(2023, 5, 1),
                                "Note 1", null);

                when(budgetService.editBudget(eq(1), any(Budget.class))).thenReturn(budget);

                mockMvc.perform(put("/budgets/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("User-ID", 1)
                                .content(asJsonString(budget)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.budgetId").value(1))
                                .andExpect(jsonPath("$.category").value("Food"));
        }

        @Test
        void testDeleteBudget() throws Exception {
                mockMvc.perform(delete("/budgets/1")
                                .header("User-ID", "1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void testGetBudgetsByMonthYear() throws Exception {
                List<Budget> budgets = Arrays.asList(
                                new Budget(1, 1, "Food", BigDecimal.valueOf(100), true, LocalDate.of(2023, 5, 1),
                                                "Note 1", null));

                when(budgetService.getBudgetsByMonthYearAndUserId(any(LocalDate.class), eq(1))).thenReturn(budgets);

                mockMvc.perform(get("/budgets/monthyear/2023-05")
                                .header("User-ID", 1))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].budgetId").value(1))
                                .andExpect(jsonPath("$[0].category").value("Food"));
        }

        @Test
        void testGetTranscationsByMonthYear() throws Exception {
                List<TransactionDTO> transactions = Arrays.asList(
                                new TransactionDTO(1, 1, 1, "Vendor1", 100.0, "Category1", "Description1",
                                                LocalDate.of(2023, 5, 1)));

                when(budgetService.findTransactionByMonthYear(any(LocalDate.class), eq(1))).thenReturn(transactions);

                mockMvc.perform(get("/budgets/transactions/2023-05")
                                .header("User-ID", 1))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].transactionId").value(1))
                                .andExpect(jsonPath("$[0].category").value("Category1"));
        }

        @Test
        void testDeleteAllBudgetsByUserId() throws Exception {
                mockMvc.perform(delete("/budgets/deleteAll/user")
                                .header("User-ID", 1))
                                .andExpect(status().isNoContent());
        }

}