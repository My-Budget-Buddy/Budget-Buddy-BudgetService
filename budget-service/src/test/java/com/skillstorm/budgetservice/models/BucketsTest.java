package com.skillstorm.budgetservice.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class BucketsTest {
    private Buckets buckets;

    @BeforeEach
    public void setUp() {
        buckets = new Buckets();
    }

    @Test
    public void testGettersAndSetters() {
        buckets.setBucketId(998);
        buckets.setUserId(777);
        buckets.setBucketName("Buy Deloitte Stock");
        buckets.setAmountRequired(BigDecimal.valueOf(100000));
        buckets.setAmountAvailable(BigDecimal.valueOf(100000));
        buckets.setMonthYear(LocalDate.of(2024, 5, 1));
        buckets.setIsReserved(true);
        buckets.setIsActive(true);

        LocalDateTime rightNow = LocalDateTime.now();
        buckets.setDateCreated(rightNow);

        assertEquals(998, buckets.getBucketId());
        assertEquals(777, buckets.getUserId());
        assertEquals("Buy Deloitte Stock", buckets.getBucketName());
        assertEquals(BigDecimal.valueOf(100000), buckets.getAmountRequired());
        assertEquals(BigDecimal.valueOf(100000), buckets.getAmountAvailable());
        assertEquals(LocalDate.of(2024, 5, 1), buckets.getMonthYear());
        assertEquals(true, buckets.getIsReserved());
        assertEquals(true, buckets.getIsActive());
        assertEquals(rightNow, buckets.getDateCreated());
    }

}
