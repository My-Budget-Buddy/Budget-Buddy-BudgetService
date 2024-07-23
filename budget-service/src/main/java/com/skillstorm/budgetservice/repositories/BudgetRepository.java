package com.skillstorm.budgetservice.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillstorm.budgetservice.models.Buckets;
import com.skillstorm.budgetservice.models.Budget;

import jakarta.transaction.Transactional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    /**
     * Finds all Budgets associated with a specific user ID.
     * 
     * @param userId the ID of the user
     * @return a list of Budgets for the given user ID
     */
    List<Budget> findByUserId(int userId);

    /**
     * Custom query to find Budgets by month and year and user ID.
     * 
     * @param monthYear the month and year to filter by
     * @param userId    the ID of the user
     * @return a list of Budgets for the specified month, year, and user ID
     */
    @Query("SELECT budget FROM Budget budget WHERE budget.monthYear = :monthYear AND budget.userId = :userId")
    List<Budget> findByMonthYearAndUserId(@Param("monthYear") LocalDate monthYear, @Param("userId") int userId);

    /**
     * Deletes all Budgets associated with a specific user ID.
     * 
     * This method is annotated with @Modifying and @Transactional to indicate that
     * it
     * performs a modifying query that should be executed within a transaction.
     * 
     * @param userId the ID of the user
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Budget budget WHERE budget.userId = :userId")
    void deleteAllBudgetsByUserId(@Param("userId") int userId);

}
