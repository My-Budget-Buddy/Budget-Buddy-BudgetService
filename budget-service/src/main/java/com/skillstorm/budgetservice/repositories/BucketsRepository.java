package com.skillstorm.budgetservice.repositories;

import java.util.List;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.skillstorm.budgetservice.models.Buckets;

@Repository
public interface BucketsRepository extends JpaRepository<Buckets, Integer> {
    List<Buckets> findByUserId(int userId);

    @Query("SELECT bucket FROM Buckets bucket WHERE bucket.monthYear = :monthYear AND bucket.userId = :userId")
    List<Buckets> findByMonthYearAndUserId(@Param("monthYear") LocalDate monthYear, @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Buckets bucket WHERE bucket.userId = :userId")
    void deleteAllBucketsByUserId(@Param("userId") int userId);
}
