package com.skillstorm.budgetservice.integration.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.budgetservice.dto.TransactionDTO;
import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.services.TransactionService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BudgetControllerIntegrationTest {

	@Autowired
    private WebApplicationContext context;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransactionService transactionService;

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

	@Test
	void testFindAllBudgets() throws Exception {

		mockMvc.perform(get("/budgets"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(6));
	};

	@Test
	void testGetBudgetsById() throws Exception {

		mockMvc.perform(get("/budgets/userBudgets")
				.header("User-ID", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2));
	}

	@Test
	void testCreateBudget() throws Exception {

		Budget budget = new Budget(7, 1, "Food", BigDecimal.valueOf(100), true, LocalDate.of(2023, 5, 1), "Note 1", null);

		mockMvc.perform(post("/budgets")
				.contentType(MediaType.APPLICATION_JSON)
				.header("User-ID", 1)
				.content(asJsonString(budget)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.userId").value(1))
				.andExpect(jsonPath("$.category").value("Food"))
				.andExpect(jsonPath("$.totalAmount").value(100))
				.andExpect(jsonPath("$.isReserved").value(true))
				.andExpect(jsonPath("$.monthYear").value("2023-05"))
				.andExpect(jsonPath("$.notes").value("Note 1"));

	}

	@Test
	void testEditBudget() throws Exception {
		Budget budget = new Budget(1, 1, "Food", BigDecimal.valueOf(100), true, LocalDate.of(2023, 5, 1), "Note 1", null);

		mockMvc.perform(put("/budgets/1")
				.contentType(MediaType.APPLICATION_JSON)
				.header("User-ID", 1)
				.content(asJsonString(budget)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.budgetId").value(1))
				.andExpect(jsonPath("$.category").value("Food"))
				.andExpect(jsonPath("$.totalAmount").value(100))
				.andExpect(jsonPath("$.isReserved").value(true))
				.andExpect(jsonPath("$.monthYear").value("2023-05"))
				.andExpect(jsonPath("$.notes").value("Note 1"));
	}

	@Test
	void testDeleteBudget() throws Exception {
		mockMvc.perform(delete("/budgets/1")
				.header("User-ID", "1"))
				.andExpect(status().isNoContent());
	}

	@Test
	void testGetBudgetsByMonthYear() throws Exception {

		mockMvc.perform(get("/budgets/monthyear/2024-05")
				.header("User-ID", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2));
	}

	@Test
	void testGetTransactionsByMonthYear() throws Exception {
		
		// set up mockMvc for TransactionService
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

		TransactionDTO transactionDto = new TransactionDTO();
		transactionDto.setDate(LocalDate.of(2024, 5, 1));
		List<TransactionDTO> transactionDtos = Arrays.asList(transactionDto, transactionDto);

		// if not mocked, return java.lang.IllegalStateException: No instances available for transaction_service
		when(transactionService.getTransactionsExcludingIncome(1)).thenReturn(transactionDtos);

		mockMvc.perform(get("/budgets/transactions/2024-05")
				.header("User-ID", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2));
	}

	@Test
	void testDeleteAllBudgetsByUserId() throws Exception {
		mockMvc.perform(delete("/budgets/deleteAll/user")
				.header("User-ID", 1))
				.andExpect(status().isNoContent());
	}

}