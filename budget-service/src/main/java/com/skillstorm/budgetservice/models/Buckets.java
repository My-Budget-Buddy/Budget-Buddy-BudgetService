package com.skillstorm.budgetservice.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "buckets")
public class Buckets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bucketId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // @Column(nullable = false)
    // private int userId;

    @Column(nullable = false)
    private String bucketName;

    @Column(nullable = false)
    private Double amountRequired;

    @Column(nullable = false)
    private Double amountAvailable;

    private String bucketMonth;
    // boolean

    private Boolean isCompleted;

    private LocalDateTime dateCreated;

    @PrePersist
    public void onCreate() {
        dateCreated = LocalDateTime.now();
    }

    public Buckets() {}

    public Buckets(User user, String bucketName, Double amountRequired, Double amountAvailable, String bucketMonth, Boolean isCompleted) {
        this.user = user;
        this.bucketName = bucketName;
        this.amountRequired = amountRequired;
        this.amountAvailable = amountAvailable;
        this.bucketMonth = bucketMonth;
        this.isCompleted = isCompleted;
    }


    public int getBucketId() {
        return bucketId;
    }

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }

    // public int getUserId() {
    //     return userId;
    // }

    // public void setUserId(int userId) {
    //     this.userId = userId;
    // }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public String getBucketMonth() {
        return bucketMonth;
    }

    public void setBucketMonth(String bucketMonth) {
        this.bucketMonth = bucketMonth;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

}
