package com.skillstorm.budgetservice.integration.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillstorm.budgetservice.controllers.BucketsController;
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

    private AutoCloseable closeable;


    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // Register the JSR310 module
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
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
    public void testGetBucketByBucketIdUserIdNotFound() throws Exception {
        Buckets bucket = new Buckets(1, "hahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());
        when(bucketsService.getBucketByBucketId(anyInt())).thenReturn(Optional.of(bucket));

        mockMvc.perform(get("/buckets/bucket/{bucketId}", 1)
                .header("User-ID", "0"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testGetBucketByBucketIdBucketNotPresent() throws Exception {
        when(bucketsService.getBucketByBucketId(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/buckets/bucket/{bucketId}", 1)
                .header("User-ID", "0"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testGetBucketsByMonthYear() throws Exception {
        Buckets bucket = new Buckets(1, "hahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());        
        List<Buckets> buckets = Arrays.asList(bucket, bucket);
        when(bucketsService.getBudgetsByMonthYearAndUserId(any(LocalDate.class), anyInt())).thenReturn(buckets);

        mockMvc.perform(get("/buckets/monthyear/{monthYear}", "2024-09")
                .header("User-ID", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(buckets.size()));
    }

        @Test
    public void testAddBucket() throws Exception {
        Buckets bucket = new Buckets(1, "hahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());        
        when(bucketsService.saveBucket(bucket)).thenReturn(bucket);

        mockMvc.perform(post("/buckets/add")
                .header("USer-ID", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bucket)))
            .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateBucket() throws Exception {
        Buckets bucket = new Buckets(1, "hahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());
        Buckets bucket2 = new Buckets(1, "hahahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());

        when(bucketsService.getBucketByBucketId(anyInt())).thenReturn(Optional.of(bucket));

        mockMvc.perform(put("/buckets/update/{bucketId}", "0")
                .header("User-ID", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bucket2)))
            .andExpect(status().isOk());
    }
    
    @Test
    public void testUpdateBucketNotPresent() throws Exception {
        Buckets bucket2 = new Buckets(1, "hahahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());

        when(bucketsService.getBucketByBucketId(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(put("/buckets/update/{bucketId}", "0")
                .header("User-ID", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bucket2)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateBucketUserIdNotMatch() throws Exception {
        Buckets bucket = new Buckets(1, "hahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());
        Buckets bucket2 = new Buckets(0, "hahahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());

        when(bucketsService.getBucketByBucketId(anyInt())).thenReturn(Optional.of(bucket));

        mockMvc.perform(put("/buckets/update/{bucketId}", "0")
                .header("User-ID", 0)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bucket2)))
            .andExpect(status().isForbidden());
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

    @Test
    public void testDeleteBucketNotExist() throws Exception {
        when(bucketsService.getBucketByBucketId(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/buckets/delete/{bucketId}", 1)
                .header("User-ID", "1"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteBucketUserIdNotMatch() throws Exception {
        Buckets bucket = new Buckets(1, "hahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());
        when(bucketsService.getBucketByBucketId(anyInt())).thenReturn(Optional.of(bucket));

        mockMvc.perform(delete("/buckets/delete/{bucketId}", 1)
                .header("User-ID", "0"))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteAllBudgetsByUserId() throws Exception {
        
        doNothing().when(bucketsService).deleteAllBucketsByUserId(anyInt());

        mockMvc.perform(delete("/buckets/deleteAll/user")
                .header("User-ID", "1"))
                .andExpect(status().isOk());
    }
}
