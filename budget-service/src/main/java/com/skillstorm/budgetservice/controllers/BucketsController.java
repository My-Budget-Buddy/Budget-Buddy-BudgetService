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

    @GetMapping("/all")
    public ResponseEntity<List<Buckets>> getAllBuckets() {
        List<Buckets> buckets = bucketsService.getAllBuckets();
        return new ResponseEntity<List<Buckets>>(buckets, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Buckets>> getAllBucketsByUserId(@PathVariable int userId) {
        List<Buckets> buckets = bucketsService.getAllBucketsByUserId(userId);
        return ResponseEntity.ok(buckets);
    }

    @GetMapping("/bucket/{bucketId}")
    public ResponseEntity<Buckets> getBucketByBucketId(@PathVariable int bucketId) {
        Optional<Buckets> bucket = bucketsService.getBucketByBucketId(bucketId);

        if (bucket.isPresent()) {
            return ResponseEntity.ok(bucket.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/monthyear/{monthYear}/user/{userId}")
    public ResponseEntity<List<Buckets>> getBudgetsByMonthYear(@PathVariable String monthYear, @PathVariable int userId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(monthYear + "-01", formatter); // This is to make sure the date is in the format of yyyy-MM-dd for local date
        List<Buckets> budgets = bucketsService.getBudgetsByMonthYearAndUserId(date, userId);
        return ResponseEntity.ok(budgets);
    }

    @PostMapping("/add")
    public ResponseEntity<Buckets> addBucket(@RequestBody Buckets bucket) {
        Buckets newBucket = bucketsService.saveBucket(bucket);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBucket);
    }

    @PutMapping("/update/{bucketId}")
    public ResponseEntity<Buckets> updateBucket(@PathVariable int bucketId, @RequestBody Buckets bucketDetails) {
        try {
            Buckets updatedBucket = bucketsService.updateBucket(bucketId, bucketDetails);
            return ResponseEntity.ok(updatedBucket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{bucketId}")
    public ResponseEntity<String> deleteBucket(@PathVariable int bucketId) {
        bucketsService.deleteBucket(bucketId);
        return ResponseEntity.ok("The bucket is deleted");
    }

}
