package com.skillstorm.budgetservice.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.skillstorm.budgetservice.utils.CustomLocalData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private int budgetId;

    private int userId;

    @Column
    private String category;

    @NonNull
    @Column(name = "spent_amount")
    private BigDecimal spentAmount;

    @Column(name = "is_reserved")
    private Boolean isReserved;

    @JsonDeserialize(using = CustomLocalData.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private LocalDate monthYear;

    private String notes;

    @Column(name = "created_timestamp")
    private LocalDateTime createdTimeStamp;

    @PrePersist
    protected void onCreate() {
        createdTimeStamp = LocalDateTime.now();
    }

}
