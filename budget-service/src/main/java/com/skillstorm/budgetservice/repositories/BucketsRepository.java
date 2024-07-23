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
    /**
     * Finds all Buckets associated with a specific user ID.
     * 
     * @param userId the ID of the user
     * @return a list of Buckets for the given user ID
     */
    List<Buckets> findByUserId(int userId);

    /**
     * Custom query to find Buckets by month and year and user ID.
     * 
     * @param monthYear the month and year to filter by
     * @param userId    the ID of the user
     * @return a list of Buckets for the specified month, year, and user ID
     */
    @Query("SELECT bucket FROM Buckets bucket WHERE bucket.monthYear = :monthYear AND bucket.userId = :userId")
    List<Buckets> findByMonthYearAndUserId(@Param("monthYear") LocalDate monthYear, @Param("userId") int userId);

    /**
     * Deletes all Buckets associated with a specific user ID.
     * 
     * This method is annotated with @Modifying and @Transactional to indicate that
     * it
     * performs a modifying query that should be executed within a transaction.
     * 
     * @param userId the ID of the user
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Buckets bucket WHERE bucket.userId = :userId")
    void deleteAllBucketsByUserId(@Param("userId") int userId);
}
