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

    public List<Buckets> getAllBuckets() {
        return bucketsRepository.findAll();
    }

    public List<Buckets> getAllBucketsByUserId(int userId) {
        return bucketsRepository.findByUserId(userId);
    }

    public Optional<Buckets> getBucketByBucketId(int bucketId) {
        return bucketsRepository.findById(bucketId);
    }

    public List<Buckets> getBudgetsByMonthYearAndUserId(LocalDate monthYear, int userId) {
        return bucketsRepository.findByMonthYearAndUserId(monthYear, userId);
    }

    public Buckets saveBucket(Buckets bucket) {
        return bucketsRepository.save(bucket);
    }

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

            if (bucketDetails.getAmountAvailable() != null) {
                bucket.setAmountAvailable(bucketDetails.getAmountAvailable());
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

    public void deleteBucket(int bucketId) {
        bucketsRepository.deleteById(bucketId);
    }
}
