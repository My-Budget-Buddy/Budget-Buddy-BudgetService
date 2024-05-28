package com.skillstorm.budgetservice.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.budgetservice.models.MonthlySummary;
import com.skillstorm.budgetservice.services.MonthlySummaryService;

@WebMvcTest(MonthlySummaryController.class)
public class MonthlySummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MonthlySummaryService monthlySummaryService;

    @Autowired
    private WebApplicationContext context;

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
        List<MonthlySummary> summaryList = Arrays.asList(
                new MonthlySummary(1, 1, BigDecimal.valueOf(5000), LocalDate.of(2023, 5, 1), BigDecimal.valueOf(3000)));

        when(monthlySummaryService.findAllMonthlySummarys()).thenReturn(summaryList);

        mockMvc.perform(get("/summarys"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].summaryId").value(1))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].projectedIncome").value(5000))
                .andExpect(jsonPath("$[0].monthYear").value("2023-05"))
                .andExpect(jsonPath("$[0].totalBudgetAmount").value(3000));
    }

    @Test
    void testGetSummarysById() throws Exception {
        List<MonthlySummary> summaryList = Arrays.asList(
                new MonthlySummary(1, 1, BigDecimal.valueOf(5000), LocalDate.of(2023, 5, 1), BigDecimal.valueOf(3000)));

        when(monthlySummaryService.findMonthlySummarysByUserId(1)).thenReturn(summaryList);

        mockMvc.perform(get("/summarys/userSummarys")
                .header("User-ID", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].summaryId").value(1))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].projectedIncome").value(5000))
                .andExpect(jsonPath("$[0].monthYear").value("2023-05"))
                .andExpect(jsonPath("$[0].totalBudgetAmount").value(3000));
    }

    @Test
    void testCreateMonthlySummary() throws Exception {
        MonthlySummary newSummary = new MonthlySummary(1, 1, BigDecimal.valueOf(5000), LocalDate.of(2023, 5, 1),
                BigDecimal.valueOf(3000));

        when(monthlySummaryService.saveMonthlySummary(any(MonthlySummary.class), eq(1))).thenReturn(newSummary);

        mockMvc.perform(post("/summarys")
                .contentType(MediaType.APPLICATION_JSON)
                .header("User-ID", 1)
                .content(asJsonString(newSummary)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.summaryId").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.projectedIncome").value(5000))
                .andExpect(jsonPath("$.monthYear").value("2023-05"))
                .andExpect(jsonPath("$.totalBudgetAmount").value(3000));
    }

    @Test
    void testEditMonthlySummary() throws Exception {
        MonthlySummary updatedSummary = new MonthlySummary(1, 1, BigDecimal.valueOf(5000), LocalDate.of(2023, 5, 1),
                BigDecimal.valueOf(3000));

        when(monthlySummaryService.editMonthlySummary(eq(1), any(MonthlySummary.class))).thenReturn(updatedSummary);

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
        List<MonthlySummary> summaryList = Arrays.asList(
                new MonthlySummary(1, 1, BigDecimal.valueOf(5000), LocalDate.of(2023, 5, 1), BigDecimal.valueOf(3000)));

        when(monthlySummaryService.getMonthlySummarysByMonthYearAndUserId(any(LocalDate.class), eq(1)))
                .thenReturn(summaryList);

        mockMvc.perform(get("/summarys/monthyear/2023-05")
                .header("User-ID", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].summaryId").value(1))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].projectedIncome").value(5000))
                .andExpect(jsonPath("$[0].monthYear").value("2023-05"))
                .andExpect(jsonPath("$[0].totalBudgetAmount").value(3000));
    }

    @Test
    void testDeleteAllSummarysByUserId() throws Exception {
        mockMvc.perform(delete("/summarys/deleteAll/user")
                .header("User-ID", 1))
                .andExpect(status().isNoContent());
    }

}
