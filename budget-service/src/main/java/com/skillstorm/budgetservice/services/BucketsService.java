package com.skillstorm.budgetservice.services;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.skillstorm.budgetservice.models.Buckets;
import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.repositories.BucketsRepository;

@Service
public class BucketsService {

    private final BucketsRepository bucketsRepository;

    public BucketsService(BucketsRepository bucketsRepository) {
        this.bucketsRepository = bucketsRepository;
    }

    /**
     * Retrieves all Bucket objects.
     *
     * @return a list of all Bucket objects
     */
    public List<Buckets> getAllBuckets() {
        return bucketsRepository.findAll();
    }

    /**
     * Retrieves all Bucket objects associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return a list of Bucket objects for the specified user
     */
    public List<Buckets> getAllBucketsByUserId(int userId) {
        return bucketsRepository.findByUserId(userId);
    }

     /**
     * Retrieves a specific Bucket object by its bucket ID.
     *
     * @param bucketId the ID of the bucket
     * @return an Optional containing the Bucket object, if found
     */
    public Optional<Buckets> getBucketByBucketId(int bucketId) {
        return bucketsRepository.findById(bucketId);
    }

    /**
     * Retrieves Bucket objects associated with a specific user ID and month-year.
     *
     * @param monthYear the month-year in LocalDate format
     * @param userId the ID of the user
     * @return a list of Bucket objects for the specified month-year and user
     */
    public List<Buckets> getBudgetsByMonthYearAndUserId(LocalDate monthYear, int userId) {
        return bucketsRepository.findByMonthYearAndUserId(monthYear, userId);
    }

    /**
     * Saves a new Bucket object.
     *
     * @param bucket the Bucket object to save
     * @return the saved Bucket object
     */
    public Buckets saveBucket(Buckets bucket) {
        return bucketsRepository.save(bucket);
    }

    /**
     * Updates an existing Bucket object.
     *
     * @param bucketId the ID of the bucket to update
     * @param bucketDetails the updated details of the bucket
     * @return the updated Bucket object
     */
    public Buckets updateBucket(int bucketId, Buckets bucketDetails) {
        Optional<Buckets> existingBucket = bucketsRepository.findById(bucketId);

        if (existingBucket.isPresent()) {
            Buckets bucket = existingBucket.get();

            if (bucketDetails.getBucketName() != null) {
                bucket.setBucketName(bucketDetails.getBucketName());
            }

            if (bucketDetails.getAmountRequired() != null) {
                bucket.setAmountRequired(bucketDetails.getAmountRequired());
            }

            if (bucketDetails.getAmountReserved() != null) {
                bucket.setAmountReserved(bucketDetails.getAmountReserved());
            }

            if (bucketDetails.getMonthYear() != null) {
                bucket.setMonthYear(bucketDetails.getMonthYear());
            }

            if (bucketDetails.getUserId() != 0) {
                bucket.setUserId(bucketDetails.getUserId());
            }

            if (bucketDetails.getIsActive() != null) {
                bucket.setIsActive(bucketDetails.getIsActive());
            }

            if (bucketDetails.getIsReserved() != null) {
                bucket.setIsReserved(bucketDetails.getIsReserved());
            }

            return bucketsRepository.save(bucket);
        } else {
            throw new RuntimeException("Bucket not found");
        }

    }

    /**
     * Deletes a specific Bucket object by its bucket ID.
     *
     * @param bucketId the ID of the bucket to delete
     */
    public void deleteBucket(int bucketId) {
        bucketsRepository.deleteById(bucketId);
    }

    /**
     * Deletes all Bucket objects associated with a specific user ID.
     *
     * @param userId the ID of the user
     */
    public void deleteAllBucketsByUserId(int userId) {
        bucketsRepository.deleteAllBucketsByUserId(userId);
    }

    /**
     * Compares the user ID.
     *
     * @param userId the ID of the user in the request
     * @param headerUserId the ID of the user in the header
     * @throws RuntimeException if the user IDs do not match
     */
    public void compareHeaderIdWithRequestedDataId(int userId, String headerUserId) {

        if (userId != Integer.valueOf(headerUserId)) {
            throw new RuntimeException("User ID in header does not match!");
        }
    }
}
