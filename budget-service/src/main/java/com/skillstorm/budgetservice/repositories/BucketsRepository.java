package com.skillstorm.budgetservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillstorm.budgetservice.models.Buckets;

public interface BucketsRepository extends JpaRepository<Buckets, Integer> {
    List<Buckets> findByUserId(int userId);
}
