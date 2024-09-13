package com.skillstorm.budgetservice.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.skillstorm.budgetservice.services.BucketsService;

public class BucketControllerTest {

    @Mock
    private BucketsService bucketsService;
    
    @InjectMocks
    private BucketsController bucketsController;
    
    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }  

    @Test
    public void testGetAllBuckets() {
    
    }
}
