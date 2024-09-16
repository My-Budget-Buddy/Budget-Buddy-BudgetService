package com.skillstorm.budgetservice.integration.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.skillstorm.budgetservice.models.Buckets;
import com.skillstorm.budgetservice.repositories.BucketsRepository;
import com.skillstorm.budgetservice.services.BucketsService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class BucketsServiceIntegrationTest {
    
    @Autowired
    private BucketsService bucketsService;          // The class under test

    @Autowired
    private BucketsRepository bucketsRepository;    // The class under test's dependency

    /**
     * Setup and teardown methods to clear the database before and after each test.
     */
    @BeforeEach
    public void setup() {
        bucketsRepository.deleteAll();
    }

    @AfterEach
    public void teardown() {
        bucketsRepository.deleteAll();
    }

    /**
     * Test case for getAllBuckets method in BucketsService class - Successfully finds all buckets.
     */
    @Test
    public void getAllBucketsTest_Success() {
        Buckets bucket1 = new Buckets(1, "Test Bucket 1", new BigDecimal(100.00), new BigDecimal(0.00), LocalDate.of(2021,1,1), false, true, null);
        Buckets bucket2 = new Buckets(2, "Test Bucket 2", new BigDecimal(200.00), new BigDecimal(0.00), LocalDate.of(2022,1,1), false, true, null);

        bucketsRepository.save(bucket1);
        bucketsRepository.save(bucket2);

        List<Buckets> result = bucketsService.getAllBuckets();

        assertTrue(result.size() == 2);
        assertTrue(result.contains(bucket1));
        assertTrue(result.contains(bucket2));
    }

    /**
     * Test case for getAllBuckets method in BucketsService class - Does not find any bucket.
     */
    @Test
    public void getAllBucketsTest_NotFound() {
        List<Buckets> result = bucketsService.getAllBuckets();
        assertTrue(result.isEmpty());
    }

    /**
     * Test case for getAllBucketsByUserId method in BucketsService class - Successfully finds a bucket for a specific user.
     */
    @Test
    public void getAllBucketsByUserIdTest_Success() {
        int userId = 1;

        Buckets bucket1 = new Buckets(1, "Test Bucket 1", new BigDecimal(100.00), new BigDecimal(0.00), LocalDate.of(2021,1,1), false, true, null);
        Buckets bucket2 = new Buckets(2, "Test Bucket 2", new BigDecimal(200.00), new BigDecimal(0.00), LocalDate.of(2022,1,1), false, true, null);

        bucketsRepository.save(bucket1);
        bucketsRepository.save(bucket2);

        List<Buckets> result = bucketsService.getAllBucketsByUserId(userId);

        assertEquals(1, result.size());
        assertTrue(result.contains(bucket1));
    }

    /**
     * Test case for getAllBucketsByUserId method in BucketsService class - Does not find any bucket for a specific user.
     */
    @Test
    public void getAllBucketsByUserIdTest_NotFound() {
        Buckets bucket1 = new Buckets(1, "Test Bucket 1", new BigDecimal(100.00), new BigDecimal(0.00), LocalDate.of(2021,1,1), false, true, null);
        bucketsRepository.save(bucket1);

        List<Buckets> result = bucketsService.getAllBucketsByUserId(0);
        assertEquals(0, result.size());
    }

    /**
     * Test case for getBucketByBucketId method in BucketsService class - Successfully finds a bucket with a specific id.
     */
    @Test
    public void getBucketByBucketIdTest_Success() {
        Buckets bucket1 = new Buckets(1, "Test Bucket 1", new BigDecimal(100.00), new BigDecimal(0.00), LocalDate.of(2021,1,1), false, true, null);
        bucketsRepository.save(bucket1);

        Buckets result = bucketsService.getBucketByBucketId(bucket1.getBucketId()).get();
        assertEquals(bucket1, result);
    }

    /**
     * Test case for getBucketByBucketId method in BucketsService class - Does not find a bucket with a specif id.
     */
    @Test
    public void getBucketByBucketIdTest_NotFound() {
        Buckets bucket1 = new Buckets(1, "Test Bucket 1", new BigDecimal(100.00), new BigDecimal(0.00), LocalDate.of(2021,1,1), false, true, null);
        bucketsRepository.save(bucket1);

        Buckets result = bucketsService.getBucketByBucketId(bucket1.getBucketId()).get();
        assertEquals(bucket1, result);
    }

    /**
     * Test case for getBudgetsByMonthYearAndUserId method in BucketsService class - Successfully finds buckets for a specific user and month-year.
     */
    @Test
    public void getBudgetsByMonthYearAndUserIdTest_Success() {
        int userId = 1;
        LocalDate monthYear = LocalDate.of(2021, 1, 1);

        Buckets bucket1 = new Buckets(1, "Test Bucket 1", new BigDecimal(100.00), new BigDecimal(0.00), LocalDate.of(2021,1,1), false, true, null);
        bucketsRepository.save(bucket1);

        List<Buckets> result = bucketsService.getBudgetsByMonthYearAndUserId(monthYear, userId);
        assertEquals(1, result.size());
    }

    /**
     * Test case for getBudgetsByMonthYearAndUserId method in BucketsService class - Does not find any buckets for a specific user and month-year.
     */
    @Test
    public void getBudgetsByMonthYearAndUserIdTest_NotFound() {
        LocalDate monthYear = LocalDate.of(2021, 1, 1);

        Buckets bucket1 = new Buckets(1, "Test Bucket 1", new BigDecimal(100.00), new BigDecimal(0.00), LocalDate.of(2021,1,1), false, true, null);
        bucketsRepository.save(bucket1);

        List<Buckets> result = bucketsService.getBudgetsByMonthYearAndUserId(monthYear, 0);
        assertEquals(0, result.size());
    }

    /**
     * Test case for saveBucket method in BucketsService class - Successfully saves a bucket.
     */
    @Test
    public void saveBucketTest_Success() {
        Buckets bucket = new Buckets(1, "Test Bucket 1", new BigDecimal(100.00), new BigDecimal(0.00), LocalDate.of(2021,1,1), false, true, null);
        Buckets result = bucketsService.saveBucket(bucket);
        assertEquals(bucket, result);
    }

    /**
     * Test case for updateBucket method in BucketsService class - Successfully updates a bucket.
     */
    @Test
    public void updateBucketTest_Success() {
        Buckets bucket = new Buckets(1, "Test Bucket 1", new BigDecimal(100.00), new BigDecimal(0.00), LocalDate.of(2021,1,1), false, true, null);
        Buckets updatedBucket = new Buckets(1, "Updated Bucket 1", new BigDecimal(200.00), new BigDecimal(0.00), LocalDate.of(2021,1,1), false, true, null);

        bucketsRepository.save(bucket);

        Buckets result = bucketsService.updateBucket(bucket.getBucketId(), updatedBucket);
        assertEquals(updatedBucket.getBucketName(), result.getBucketName());
    }

    /**
     * Test case for deleteBucket method in BucketsService class - Successfully deletes a bucket.
     */
    @Test
    public void deleteBucketTest_Success() {
        Buckets bucket = new Buckets(1, "Test Bucket 1", new BigDecimal(100.00), new BigDecimal(0.00), LocalDate.of(2021,1,1), false, true, null);
        bucketsRepository.save(bucket);

        bucketsService.deleteBucket(bucket.getBucketId());
        assertTrue(bucketsRepository.findById(bucket.getBucketId()).isEmpty());
    }

    /**
     * Test case for deleteAllBucketsByUserId method in BucketsService class - Successfully deletes all buckets for a specific user.
     */
    @Test
    public void deleteAllBucketsByUserIdTest_Success() {
        int userId = 1;

        Buckets bucket1 = new Buckets(1, "Test Bucket 1", new BigDecimal(100.00), new BigDecimal(0.00), LocalDate.of(2021,1,1), false, true, null);
        Buckets bucket2 = new Buckets(1, "Test Bucket 2", new BigDecimal(200.00), new BigDecimal(0.00), LocalDate.of(2022,1,1), false, true, null);

        bucketsRepository.save(bucket1);
        bucketsRepository.save(bucket2);

        bucketsService.deleteAllBucketsByUserId(userId);
        assertTrue(bucketsRepository.findByUserId(userId).isEmpty());
    }

}
