package com.skillstorm.budgetservice.models;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "buckets")
public class Buckets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bucketId;

    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private User user;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private String bucketName;

    @Column(nullable = false)
    private Double amountRequired;

    @Column(nullable = false)
    private Double amountAvailable;

    @JsonDeserialize(using = CustomLocalData.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private LocalDate monthYear;
    // boolean

    private Boolean isReserved;

    private Boolean isActive;

    private LocalDateTime dateCreated;

    @PrePersist
    public void onCreate() {
        dateCreated = LocalDateTime.now();
    }

    public Buckets() {
    }

    public Buckets(int userId, String bucketName, Double amountRequired, Double amountAvailable, LocalDate monthYear,
            Boolean isReserved, Boolean isActive) {
        this.userId = userId;
        this.bucketName = bucketName;
        this.amountRequired = amountRequired;
        this.amountAvailable = amountAvailable;
        this.monthYear = monthYear;
        this.isReserved = isReserved;
        this.isActive = isActive;
    }

    public int getBucketId() {
        return bucketId;
    }

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // public User getUser() {
    // return user;
    // }

    // public void setUser(User user) {
    // this.user = user;
    // }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public Double getAmountRequired() {
        return amountRequired;
    }

    public void setAmountRequired(Double amountRequired) {
        this.amountRequired = amountRequired;
    }

    public Double getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(Double amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public LocalDate getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(LocalDate monthYear) {
        this.monthYear = monthYear;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Boolean getIsReserved() {
        return isReserved;
    }

    public void setIsReserved(Boolean isReserved) {
        this.isReserved = isReserved;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
