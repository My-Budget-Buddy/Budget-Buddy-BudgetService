package com.skillstorm.budgetservice.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.format.DateTimeFormatter;

import com.skillstorm.budgetservice.models.Buckets;
import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.services.BucketsService;
import java.time.LocalDate;

@RestController
@RequestMapping("/buckets")
public class BucketsController {

    private final BucketsService bucketsService;

    public BucketsController(BucketsService bucketsService) {
        this.bucketsService = bucketsService;
    }

    /**
     * Retrieves all Bucket objects.
     *
     * @return a ResponseEntity containing the list of all Buckets and the HTTP status code
     */
    @GetMapping("/all")
    public ResponseEntity<List<Buckets>> getAllBuckets() {
        List<Buckets> buckets = bucketsService.getAllBuckets();
        return new ResponseEntity<List<Buckets>>(buckets, HttpStatus.OK);
    }

    /**
     * Retrieves all Bucket objects associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @param headerUserId the user ID from the request header
     * @return a ResponseEntity containing the list of Buckets for the specified user and the HTTP status code
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Buckets>> getAllBucketsByUserId(@PathVariable int userId, @RequestHeader(name = "User-ID") String headerUserId){
        bucketsService.compareHeaderIdWithRequestedDataId(userId, headerUserId);
        List<Buckets> buckets = bucketsService.getAllBucketsByUserId(userId);
        return ResponseEntity.ok(buckets);
    }

    /**
     * Retrieves a specific Bucket object by its bucket ID.
     *
     * @param bucketId the ID of the bucket
     * @return a ResponseEntity containing the Bucket object and the HTTP status code
     */
    @GetMapping("/bucket/{bucketId}")
    public ResponseEntity<Buckets> getBucketByBucketId(@PathVariable int bucketId) {
        Optional<Buckets> bucket = bucketsService.getBucketByBucketId(bucketId);

        if (bucket.isPresent()) {
            return ResponseEntity.ok(bucket.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

     /**
     * Retrieves Bucket objects associated with a specific user ID and month-year.
     *
     * @param monthYear the month-year to retrieve the Buckets for
     * @param userId the ID of the user
     * @param headerUserId the user ID from the request header
     * @return a ResponseEntity containing the list of Buckets for the specified month-year and user, and the HTTP status code
     */
    @GetMapping("/monthyear/{monthYear}/user/{userId}")
    public ResponseEntity<List<Buckets>> getBudgetsByMonthYear(@PathVariable String monthYear, @PathVariable int userId, @RequestHeader(name = "User-ID") String headerUserId) {
        bucketsService.compareHeaderIdWithRequestedDataId(userId, headerUserId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(monthYear + "-01", formatter); // This is to make sure the date is in the format of yyyy-MM-dd for local date
        List<Buckets> budgets = bucketsService.getBudgetsByMonthYearAndUserId(date, userId);
        return ResponseEntity.ok(budgets);
    }

    /**
     * Adds a new Bucket object.
     *
     * @param bucket the Bucket object to add
     * @return a ResponseEntity containing the added Bucket object and the HTTP status code
     */
    @PostMapping("/add")
    public ResponseEntity<Buckets> addBucket(@RequestBody Buckets bucket) {
        Buckets newBucket = bucketsService.saveBucket(bucket);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBucket);
    }

    /**
     * Updates an existing Bucket object.
     *
     * @param bucketId the ID of the bucket to update
     * @param bucketDetails the updated details of the bucket
     * @param headerUserId the user ID from the request header
     * @return a ResponseEntity containing the updated Bucket object and the HTTP status code
     */
    @PutMapping("/update/{bucketId}")
    public ResponseEntity<Buckets> updateBucket(@PathVariable int bucketId, @RequestBody Buckets bucketDetails, @RequestHeader(name = "User-ID") String headerUserId) {
        try {
            bucketsService.compareHeaderIdWithRequestedDataId(bucketDetails.getUserId(), headerUserId);
            Buckets updatedBucket = bucketsService.updateBucket(bucketId, bucketDetails);
            return ResponseEntity.ok(updatedBucket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

     /**
     * Deletes a specific Bucket object by its bucket ID.
     *
     * @param bucketId the ID of the bucket to delete
     * @return a ResponseEntity containing a message indicating the bucket is deleted and the HTTP status code
     */
    @DeleteMapping("/delete/{bucketId}")
    public ResponseEntity<String> deleteBucket(@PathVariable int bucketId) {
        bucketsService.deleteBucket(bucketId);
        return ResponseEntity.ok("The bucket is deleted");
    }

    /**
     * Deletes all Bucket objects associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @param headerUserId the user ID from the request header
     * @return a ResponseEntity containing a message indicating the buckets are deleted and the HTTP status code
     */
    @DeleteMapping("/deleteAll/user/{userId}")
    public ResponseEntity<String> deleteAllBucketsByUserId(@PathVariable int userId, @RequestHeader(name = "User-ID") String headerUserId) {
        bucketsService.compareHeaderIdWithRequestedDataId(userId, headerUserId);
        bucketsService.deleteAllBucketsByUserId(userId);
        return ResponseEntity.ok("All buckets related to the user are deleted");
    }

}
