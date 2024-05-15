package com.skillstorm.budgetservice.models;

import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    @Column(name = "budget_name")
    private String budgetName;

    private int amount;

    @Column(name = "current_amount")
    private int currentAmount;

    @Column
    private String category;

    @Column(name = "budget_month")
    private String month;

    @Column(name = "created_timestamp")
    private LocalDateTime createdTimeStamp;

    @PrePersist
    protected void onCreate() {
        LocalDateTime xxx = LocalDateTime.now();
    }

}
