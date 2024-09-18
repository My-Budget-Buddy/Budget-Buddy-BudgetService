package com.skillstorm.budgetservice.services.unit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.skillstorm.budgetservice.models.Buckets;
import com.skillstorm.budgetservice.repositories.BucketsRepository;
import com.skillstorm.budgetservice.services.BucketsService;

public class BucketsServiceTest {

    @Mock
    private BucketsRepository bucketsRepo;      //mock object

    @InjectMocks
    private BucketsService bucketsService;     // the tested class which the mock object will be injected to
    private AutoCloseable closeable;        // used to manage mock objects (open and close them)

    /**
     * Opening all mock objects
     */
    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllBucketsTest() {
       List<Buckets> expectedWH = Arrays.asList(new Buckets(), new Buckets());

        when(bucketsRepo.findAll()).thenReturn(expectedWH);

        List<Buckets> response = bucketsService.getAllBuckets();

        assertEquals(response, expectedWH);
    }

    @Test
    public void getAllBucketsByUserIdTest() {
        int userId = 1;
        List<Buckets> expectedWH = Arrays.asList(new Buckets(), new Buckets());

        when(bucketsRepo.findByUserId(userId)).thenReturn(expectedWH);

        List<Buckets> response = bucketsService.getAllBucketsByUserId(userId);

        assertEquals(response, expectedWH);
    }

    @Test
    public void getBucketByBucketIdTest() {
        int bucketId = 1;
        Buckets expectedWH = new Buckets();

        when(bucketsRepo.findById(bucketId)).thenReturn(Optional.ofNullable(expectedWH));

        Optional<Buckets> response = bucketsService.getBucketByBucketId(bucketId);

        assertEquals(response.get(), expectedWH);
    }

    @Test
    public void getBudgetsByMonthYearAndUserIdTest() {
        LocalDate now = LocalDate.now();
        int userId = 1;

        Buckets bct1 = new Buckets();
        Buckets bct2 = new Buckets();

        bct1.setUserId(userId);
        bct1.setMonthYear(now);

        bct2.setUserId(userId);
        bct2.setMonthYear(now);

        List<Buckets> expectedWH = Arrays.asList(bct1, bct2);

        when(bucketsRepo.findByMonthYearAndUserId(now, userId)).thenReturn(expectedWH);

        List<Buckets> response = bucketsService.getBudgetsByMonthYearAndUserId(now, userId);

        assertEquals(response, expectedWH);
    }

    @Test
    public void saveBucketTest() {
        Buckets inputBucket = new Buckets();
        Buckets savedBucket = new Buckets();

        when(bucketsRepo.save(inputBucket)).thenReturn(savedBucket);

        Buckets response = bucketsService.saveBucket(inputBucket);

        assertEquals(response, savedBucket);
    }

    @Test
    public void updateBucketTest() {
        int bucketId = 1;
        int bucketId2 = 2;

        int userId = 1;

        LocalDate ld = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        Buckets inputBucket = new Buckets(userId, "Vacation", new BigDecimal(10000.00), new BigDecimal(5000.00),
        ld, true, true, now);
        Buckets savedBucket = new Buckets(userId, "Vacation", new BigDecimal(20000.00), new BigDecimal(8000.00),
        ld, true, true, now);

        Buckets inputBucket2 = new Buckets();
        Buckets savedBucket2 = new Buckets();

        inputBucket.setBucketId(bucketId);
        savedBucket.setBucketId(bucketId);

        when(bucketsRepo.findById(bucketId)).thenReturn(Optional.ofNullable(inputBucket));
        when(bucketsRepo.save(inputBucket)).thenReturn(savedBucket);

        when(bucketsRepo.findById(bucketId2)).thenReturn(Optional.ofNullable(inputBucket2));
        when(bucketsRepo.save(inputBucket2)).thenReturn(savedBucket2);

        Buckets response = bucketsService.updateBucket(bucketId, inputBucket);
        Buckets response2 = bucketsService.updateBucket(bucketId2, inputBucket2);

        assertEquals(response, savedBucket);    // test for buckets without null values for their properties
        assertEquals(response2, savedBucket2);  // test for buckets with null values for their properties
        assertThrows(RuntimeException.class, () -> bucketsService.updateBucket(0, inputBucket));   //test for when it can't find the bucket to update 

    }

    @Test
    public void deleteBucketTest() {
        int bucketId = 1;
        Buckets deletedBucket = new Buckets();

        when(bucketsRepo.findById(bucketId)).thenReturn(Optional.ofNullable(deletedBucket));

        assertAll(() -> bucketsService.deleteBucket(bucketId));
    }

    @Test
    public void deleteAllBucketsByUserIdTest() {
        int userId = 1;

        List<Buckets> deletedBuckets = Arrays.asList(new Buckets(), new Buckets());

        when(bucketsRepo.findByUserId(userId)).thenReturn(deletedBuckets);

        assertAll(() -> bucketsService.deleteAllBucketsByUserId(userId));
    }

    @Test 
    public void validateRequestHeaderUserIdTest() {
        String header1 = "";
        String header2 = null;
        String header3 = "header";

        assertThrows(RuntimeException.class, () -> bucketsService.validateRequestHeaderUserId(header1));    
        assertThrows(RuntimeException.class, () -> bucketsService.validateRequestHeaderUserId(header2));    
        assertDoesNotThrow(() -> bucketsService.validateRequestHeaderUserId(header3));
    }

    /**
     * Closes all mock objects
     * @throws Exception
     */
    @AfterEach
    public void teardown() throws Exception{
        closeable.close();
    }

}
