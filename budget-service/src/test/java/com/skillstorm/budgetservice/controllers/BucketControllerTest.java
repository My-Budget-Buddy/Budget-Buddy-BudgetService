package com.skillstorm.budgetservice.controllers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.skillstorm.budgetservice.models.Buckets;
import com.skillstorm.budgetservice.services.BucketsService;

public class BucketControllerTest {

    @Mock
    private BucketsService bucketsService;
    
    @InjectMocks
    private BucketsController bucketsController;
    
    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }  

    @Test
    public void testGetAllBuckets() {
        List<Buckets> buckets = Arrays.asList(new Buckets(), new Buckets());
        
        when(bucketsService.getAllBuckets()).thenReturn(buckets);

        ResponseEntity<List<Buckets>> response = bucketsController.getAllBuckets();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(buckets, response.getBody());
    }

    @Test
    public void testGetAllBucketsByUserId() {
        List<Buckets> buckets = Arrays.asList(new Buckets(), new Buckets());
        String headerUserId = "1";
        int userId = Integer.parseInt(headerUserId);
        
        when(bucketsService.getAllBucketsByUserId(userId)).thenReturn(buckets);

        ResponseEntity<List<Buckets>> response = bucketsController.getAllBucketsByUserId(headerUserId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(buckets, response.getBody());
    }

    @Test
    public void testGetBucketByBucketId() {
        Buckets bucket = new Buckets(1, "", BigDecimal.valueOf(0), BigDecimal.valueOf(0), LocalDate.now(), false, false, LocalDateTime.now());
        int bucketId = 1;
        String headerUserId = "1";
        
        when(bucketsService.getBucketByBucketId(bucketId)).thenReturn(Optional.of(bucket));

        ResponseEntity<Buckets> response = bucketsController.getBucketByBucketId(bucketId, headerUserId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bucket, response.getBody());
    }

    @Test
    public void testGetBucketByBucketIdUserIdNotMatch() {
        Buckets bucket = new Buckets();
        int bucketId = 1;
        String headerUserId = "1";
        
        when(bucketsService.getBucketByBucketId(bucketId)).thenReturn(Optional.of(bucket));

        ResponseEntity<Buckets> response = bucketsController.getBucketByBucketId(bucketId, headerUserId);
        
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testGetBucketByBucketIdisNotPresent() {
        Buckets bucket = new Buckets(1, "", BigDecimal.valueOf(0), BigDecimal.valueOf(0), LocalDate.now(), false, false, LocalDateTime.now());
        int bucketId = 1;
        String headerUserId = "1";
        
        when(bucketsService.getBucketByBucketId(bucketId)).thenReturn(Optional.empty());

        ResponseEntity<Buckets> response = bucketsController.getBucketByBucketId(bucketId, headerUserId);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testGetBudgetsByMonthYear() {
        List<Buckets> buckets = Arrays.asList(new Buckets(), new Buckets());
        LocalDate monthYear = LocalDate.of(2013, 1, 01);
        String monthYearString = "2013-01";
        String headerUserId = "1";
        int userId = Integer.parseInt(headerUserId);
        
        when(bucketsService.getBudgetsByMonthYearAndUserId(monthYear, userId)).thenReturn(buckets);

        ResponseEntity<List<Buckets>> response = bucketsController.getBudgetsByMonthYear(monthYearString, headerUserId);
        
        verify(bucketsService).validateRequestHeaderUserId(headerUserId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(buckets, response.getBody());
    }

    @Test
    public void testAddBucket() {
        Buckets bucket = new Buckets(1, "", BigDecimal.valueOf(0), BigDecimal.valueOf(0), LocalDate.now(), false, false, LocalDateTime.now());
        String headerUserId = "1";
        
        when(bucketsService.saveBucket(bucket)).thenReturn(bucket);

        ResponseEntity<Buckets> response = bucketsController.addBucket(bucket, headerUserId);
        
        verify(bucketsService).validateRequestHeaderUserId(headerUserId);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bucket, response.getBody());
    }

    @Test
    public void testUpdateBucket() {
        Buckets bucket = new Buckets(1, "", BigDecimal.valueOf(0), BigDecimal.valueOf(0), LocalDate.now(), false, false, LocalDateTime.now());
        int bucketId = 1;
        String headerUserId = "1";
        
        when(bucketsService.getBucketByBucketId(bucketId)).thenReturn(Optional.of(bucket));
        when(bucketsService.updateBucket(bucketId, bucket)).thenReturn(bucket);

        ResponseEntity<Buckets> response = bucketsController.updateBucket(bucketId, bucket, headerUserId);
        
        verify(bucketsService).validateRequestHeaderUserId(headerUserId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bucket, response.getBody());
    }

    @Test
    public void testUpdateBucketUserIdNotMatch() {
        Buckets bucket = new Buckets(0, "", BigDecimal.valueOf(0), BigDecimal.valueOf(0), LocalDate.now(), false, false, LocalDateTime.now());
        int bucketId = 1;
        String headerUserId = "1";
        
        when(bucketsService.getBucketByBucketId(bucketId)).thenReturn(Optional.of(bucket));
        when(bucketsService.updateBucket(bucketId, bucket)).thenReturn(bucket);

        ResponseEntity<Buckets> response = bucketsController.updateBucket(bucketId, bucket, headerUserId);
        
        verify(bucketsService).validateRequestHeaderUserId(headerUserId);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testUpdateBucketNotPresent() {
        Buckets bucket = new Buckets(1, "", BigDecimal.valueOf(0), BigDecimal.valueOf(0), LocalDate.now(), false, false, LocalDateTime.now());
        int bucketId = 1;
        String headerUserId = "1";
        
        when(bucketsService.getBucketByBucketId(bucketId)).thenReturn(Optional.empty());
        when(bucketsService.updateBucket(bucketId, bucket)).thenReturn(bucket);

        ResponseEntity<Buckets> response = bucketsController.updateBucket(bucketId, bucket, headerUserId);
        
        verify(bucketsService).validateRequestHeaderUserId(headerUserId);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testDeleteBucket() {
        Buckets bucket = new Buckets(1, "", BigDecimal.valueOf(0), BigDecimal.valueOf(0), LocalDate.now(), false, false, LocalDateTime.now());
        int bucketId = 1;
        String headerUserId = "1";
        
        when(bucketsService.getBucketByBucketId(bucketId)).thenReturn(Optional.of(bucket));

        ResponseEntity<String> response = bucketsController.deleteBucket(bucketId, headerUserId);
        
        verify(bucketsService).validateRequestHeaderUserId(headerUserId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The bucket is deleted", response.getBody());
    }

    @Test
    public void testDeleteBucketUserIdNotMatch() {
        Buckets bucket = new Buckets(0, "", BigDecimal.valueOf(0), BigDecimal.valueOf(0), LocalDate.now(), false, false, LocalDateTime.now());
        int bucketId = 1;
        String headerUserId = "1";
        
        when(bucketsService.getBucketByBucketId(bucketId)).thenReturn(Optional.of(bucket));

        ResponseEntity<String> response = bucketsController.deleteBucket(bucketId, headerUserId);
        
        verify(bucketsService).validateRequestHeaderUserId(headerUserId);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You do not have permission to delete this bucket", response.getBody());
    }

    @Test
    public void testDeleteBucketNotExists() {
        int bucketId = 1;
        String headerUserId = "1";
        
        when(bucketsService.getBucketByBucketId(bucketId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = bucketsController.deleteBucket(bucketId, headerUserId);
        
        verify(bucketsService).validateRequestHeaderUserId(headerUserId);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You do not have permission to delete this bucket", response.getBody());
    }

    @Test
    public void testDeleteAllBucketsByUserId() {
        String headerUserId = "1";
        int userId = Integer.parseInt(headerUserId);
        
        ResponseEntity<String> response = bucketsController.deleteAllBucketsByUserId(headerUserId);
        
        verify(bucketsService).validateRequestHeaderUserId(headerUserId);
        verify(bucketsService).deleteAllBucketsByUserId(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("All buckets related to the user are deleted", response.getBody());
    }

}
