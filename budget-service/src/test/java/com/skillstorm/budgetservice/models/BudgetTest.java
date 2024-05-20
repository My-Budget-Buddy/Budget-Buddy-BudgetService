package com.skillstorm.budgetservice.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BudgetTest {

    private Budget budget;

    @BeforeEach
    void setUp() {
        budget = new Budget();
    }

    @Test
    void testBudgetId() {
        budget.setBudgetId(1);
        assertEquals(1, budget.getBudgetId());
    }

    @Test
    void testUserId() {
        budget.setUserId(101);
        assertEquals(101, budget.getUserId());
    }

    @Test
    void testCategory() {
        budget.setCategory("Food");
        assertEquals("Food", budget.getCategory());
    }

    @Test
    void testSpentAmount() {
        budget.setSpentAmount(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), budget.getSpentAmount());
    }

    @Test
    void testIsReserved() {
        budget.setIsReserved(true);
        assertTrue(budget.getIsReserved());
    }

    @Test
    void testMonthYear() {
        LocalDate date = LocalDate.of(2024, 5, 1);
        budget.setMonthYear(date);
        assertEquals(date, budget.getMonthYear());
    }

    @Test
    void testNotes() {
        budget.setNotes("Monthly budget for food");
        assertEquals("Monthly budget for food", budget.getNotes());
    }

    @Test
    void testCreatedTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        budget.setCreatedTimeStamp(now);
        assertEquals(now, budget.getCreatedTimeStamp());
    }

    @Test
    void testPrePersist() {
        budget.onCreate();
        assertNotNull(budget.getCreatedTimeStamp());
    }

    @Test
    void testNonNullSpentAmount() {
        assertThrows(NullPointerException.class, () -> budget.setSpentAmount(null));
    }
}
