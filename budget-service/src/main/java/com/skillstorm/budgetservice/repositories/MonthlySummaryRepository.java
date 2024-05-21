package com.skillstorm.budgetservice.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.models.MonthlySummary;

@Repository
public interface MonthlySummaryRepository extends JpaRepository<MonthlySummary, Integer> {
    List<MonthlySummary> findByUserId(int userId);

    @Query("SELECT summary FROM MonthlySummary summary WHERE summary.monthYear = :monthYear AND summary.userId = :userId")
    List<MonthlySummary> findByMonthYearAndUserId(@Param("monthYear") LocalDate monthYear, @Param("userId") int userId);

}
