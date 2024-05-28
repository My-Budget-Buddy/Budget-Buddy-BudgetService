package com.skillstorm.budgetservice.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MonthlySummaryTest {

    private MonthlySummary monthlySummary;

    @BeforeEach
    void setUp() {
        monthlySummary = new MonthlySummary();
    }

    @Test
    void testDefaultConstructor() {
        MonthlySummary summary = new MonthlySummary();
        assertNotNull(summary);
    }

    @Test
    void testConstructorWithAllFields() {
        MonthlySummary summary = new MonthlySummary(1, 1001, BigDecimal.valueOf(5000), LocalDate.of(2023, 5, 1),
                BigDecimal.valueOf(3000));
        assertEquals(1, summary.getSummaryId());
        assertEquals(1001, summary.getUserId());
        assertEquals(BigDecimal.valueOf(5000), summary.getProjectedIncome());
        assertEquals(LocalDate.of(2023, 5, 1), summary.getMonthYear());
        assertEquals(BigDecimal.valueOf(3000), summary.getTotalBudgetAmount());
    }

    @Test
    void testConstructorWithoutId() {
        MonthlySummary summary = new MonthlySummary(1001, BigDecimal.valueOf(5000), LocalDate.of(2023, 5, 1),
                BigDecimal.valueOf(3000));
        assertEquals(1001, summary.getUserId());
        assertEquals(BigDecimal.valueOf(5000), summary.getProjectedIncome());
        assertEquals(LocalDate.of(2023, 5, 1), summary.getMonthYear());
        assertEquals(BigDecimal.valueOf(3000), summary.getTotalBudgetAmount());
    }

    @Test
    void testSettersAndGetters() {
        monthlySummary.setSummaryId(1);
        monthlySummary.setUserId(1001);
        monthlySummary.setProjectedIncome(BigDecimal.valueOf(5000));
        monthlySummary.setMonthYear(LocalDate.of(2023, 5, 1));
        monthlySummary.setTotalBudgetAmount(BigDecimal.valueOf(3000));

        assertEquals(1, monthlySummary.getSummaryId());
        assertEquals(1001, monthlySummary.getUserId());
        assertEquals(BigDecimal.valueOf(5000), monthlySummary.getProjectedIncome());
        assertEquals(LocalDate.of(2023, 5, 1), monthlySummary.getMonthYear());
        assertEquals(BigDecimal.valueOf(3000), monthlySummary.getTotalBudgetAmount());
    }

}
