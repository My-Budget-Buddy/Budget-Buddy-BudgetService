package com.skillstorm.budgetservice.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanVerifier;

public class BudgetTest {

    private Budget budget;

    @BeforeEach
    void setUp() {
        budget = new Budget();
    }

    @Test
    public void testBean() {
        BeanVerifier.verifyBean(Budget.class);
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
        budget.setTotalAmount(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), budget.getTotalAmount());
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
        assertThrows(NullPointerException.class, () -> budget.setTotalAmount(null));
    }

    @Test
    void testEqualsAndHashCode() {
        Budget budget1 = new Budget();
        budget1.setBudgetId(1);
        budget1.setUserId(1);
        budget1.setCategory("");
        budget1.setTotalAmount(BigDecimal.valueOf(0));
        budget1.setIsReserved(false);
        budget1.setMonthYear(null);
        budget1.setNotes("");

        Budget budget2 = new Budget(0, 0, null, BigDecimal.valueOf(0), null, null, null, null);
        budget2.setBudgetId(1);
        budget2.setUserId(1);
        budget2.setCategory("");
        budget2.setIsReserved(false);
        budget2.setMonthYear(null);
        budget2.setNotes("");
        
        assertEquals(budget1, budget2);
        assertEquals(budget1.hashCode(), budget2.hashCode());
    }

    @Test
    void testNotEquals() {
        Budget budget1 = new Budget();
        Budget budget2 = new Budget(1, 1, "category", BigDecimal.valueOf(0), true, LocalDate.now(), "notes", LocalDateTime.now());
        
        assertNotEquals(budget1, budget2);
        assertNotEquals(budget1.hashCode(), budget2.hashCode());
    }
}
