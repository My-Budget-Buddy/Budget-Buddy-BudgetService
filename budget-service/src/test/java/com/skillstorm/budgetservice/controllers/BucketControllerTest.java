package com.skillstorm.budgetservice.controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.skillstorm.budgetservice.models.Buckets;
import com.skillstorm.budgetservice.services.BucketsService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(BucketsController.class)
public class BucketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BucketsService bucketsService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testGetAllBuckets() throws Exception {
        List<Buckets> buckets = Arrays.asList(new Buckets(), new Buckets());
        when(bucketsService.getAllBuckets()).thenReturn(buckets);

        mockMvc.perform(get("/buckets/all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(buckets.size()));
    }

    @Test
    public void testGetAllBucketsByUserId() throws Exception {
        List<Buckets> buckets = Arrays.asList(new Buckets(), new Buckets());
        when(bucketsService.getAllBucketsByUserId(anyInt())).thenReturn(buckets);

        mockMvc.perform(get("/buckets/user")
        .header("User-ID", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(buckets.size()));
    }

    @Test
    public void testGetBucketByBucketId() throws Exception {
        Buckets bucket = new Buckets(1, "hahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());
        when(bucketsService.getBucketByBucketId(anyInt())).thenReturn(Optional.of(bucket));

        mockMvc.perform(get("/buckets/bucket/{bucketId}", 1)
                .header("User-ID", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bucketName").value(bucket.getBucketName()));
    }

    @Test
    public void testDeleteBucket() throws Exception {
        Buckets bucket = new Buckets(1, "hahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());
        bucket.setUserId(1);
        when(bucketsService.getBucketByBucketId(anyInt())).thenReturn(Optional.of(bucket));

        mockMvc.perform(delete("/buckets/delete/{bucketId}", 1)
                .header("User-ID", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("The bucket is deleted"));
    }

}
