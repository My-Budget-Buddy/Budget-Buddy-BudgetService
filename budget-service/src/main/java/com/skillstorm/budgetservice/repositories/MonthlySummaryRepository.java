package com.skillstorm.budgetservice.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.models.MonthlySummary;

import jakarta.transaction.Transactional;

@Repository
public interface MonthlySummaryRepository extends JpaRepository<MonthlySummary, Integer> {

    /**
     * Finds all MonthlySummaries associated with a specific user ID.
     * 
     * @param userId the ID of the user
     * @return a list of MonthlySummaries for the given user ID
     */
    List<MonthlySummary> findByUserId(int userId);

    /**
     * Custom query to find MonthlySummaries by month and year and user ID.
     * 
     * @param monthYear the month and year to filter by
     * @param userId    the ID of the user
     * @return a list of MonthlySummaries for the specified month, year, and user ID
     */
    @Query("SELECT summary FROM MonthlySummary summary WHERE summary.monthYear = :monthYear AND summary.userId = :userId")
    List<MonthlySummary> findByMonthYearAndUserId(@Param("monthYear") LocalDate monthYear, @Param("userId") int userId);

    /**
     * Deletes all MonthlySummarys associated with a specific user ID.
     * 
     * This method is annotated with @Modifying and @Transactional to indicate that
     * it
     * performs a modifying query that should be executed within a transaction.
     * 
     * @param userId the ID of the user
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM MonthlySummary summary WHERE summary.userId = :userId")
    void deleteAllSummarysByUserId(@Param("userId") int userId);

}
