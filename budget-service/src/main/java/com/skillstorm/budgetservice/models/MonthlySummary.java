package com.skillstorm.budgetservice.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.skillstorm.budgetservice.utils.CustomLocalData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "monthly_summary")
@Data
public class MonthlySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "summary_id")
    private int summaryId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "projected_income")
    private BigDecimal projectedIncome;

    @JsonDeserialize(using = CustomLocalData.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    @Column(name = "month_year")
    private LocalDate monthYear;

    @Column(name = "total_budget_amount")
    private BigDecimal totalBudgetAmount;

    public MonthlySummary() {
    }

    public MonthlySummary(int summaryId, int userId, BigDecimal projectedIncome, LocalDate monthYear,
            BigDecimal totalBudgetAmount) {
        this.summaryId = summaryId;
        this.userId = userId;
        this.projectedIncome = projectedIncome;
        this.monthYear = monthYear;
        this.totalBudgetAmount = totalBudgetAmount;
    }

    public MonthlySummary(int userId, BigDecimal projectedIncome, LocalDate monthYear, BigDecimal totalBudgetAmount) {
        this.userId = userId;
        this.projectedIncome = projectedIncome;
        this.monthYear = monthYear;
        this.totalBudgetAmount = totalBudgetAmount;
    }

}
