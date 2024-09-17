package com.skillstorm.budgetservice.integration.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.budgetservice.models.MonthlySummary;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MonthlySummaryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
    void testFindAllSummarys() throws Exception {

        mockMvc.perform(get("/summarys"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void testGetSummarysById() throws Exception {
        
        mockMvc.perform(get("/summarys/userSummarys")
                .header("User-ID", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].summaryId").value(1))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].projectedIncome").value(7777.00))
                .andExpect(jsonPath("$[0].monthYear").value("2024-05"))
                .andExpect(jsonPath("$[0].totalBudgetAmount").value(1111.00));
    }

    @Test
    void testCreateMonthlySummary() throws Exception {
        MonthlySummary newSummary = new MonthlySummary(4, 1, BigDecimal.valueOf(5000), LocalDate.of(2023, 5, 1), BigDecimal.valueOf(3000));

        mockMvc.perform(post("/summarys")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-ID", 1)
                .content(asJsonString(newSummary)))
                .andExpect(status().isCreated())
                // .andExpect(jsonPath("$.summaryId").value(4))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.projectedIncome").value(5000.00))
                .andExpect(jsonPath("$.monthYear").value("2023-05"))
                .andExpect(jsonPath("$.totalBudgetAmount").value(3000.00));
    }

    @Test
    void testEditMonthlySummary() throws Exception {
        MonthlySummary updatedSummary = new MonthlySummary(1, 1, BigDecimal.valueOf(5000), LocalDate.of(2023, 5, 1),
                BigDecimal.valueOf(3000));

        mockMvc.perform(put("/summarys/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-ID", 1)
                .content(asJsonString(updatedSummary)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summaryId").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.projectedIncome").value(5000))
                .andExpect(jsonPath("$.monthYear").value("2023-05"))
                .andExpect(jsonPath("$.totalBudgetAmount").value(3000));
    }

    @Test
    void testDeleteSummary() throws Exception {
        mockMvc.perform(delete("/summarys/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetSummarysByMonthYear() throws Exception {

        mockMvc.perform(get("/summarys/monthyear/2024-05")
                .header("User-ID", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].summaryId").value(1))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].projectedIncome").value(7777))
                .andExpect(jsonPath("$[0].monthYear").value("2024-05"))
                .andExpect(jsonPath("$[0].totalBudgetAmount").value(1111));
    }

    @Test
    void testDeleteAllSummarysByUserId() throws Exception {
        mockMvc.perform(delete("/summarys/deleteAll/user")
                .header("User-ID", 1))
                .andExpect(status().isNoContent());
    }

}
