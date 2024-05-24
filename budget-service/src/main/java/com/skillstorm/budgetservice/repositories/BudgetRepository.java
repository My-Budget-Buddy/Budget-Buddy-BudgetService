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
    List<Budget> findByUserId(int userId);

    @Query("SELECT budget FROM Budget budget WHERE budget.monthYear = :monthYear AND budget.userId = :userId")
    List<Budget> findByMonthYearAndUserId(@Param("monthYear") LocalDate monthYear, @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Budget budget WHERE budget.userId = :userId")
    void deleteAllBudgetsByUserId(@Param("userId") int userId);

}
