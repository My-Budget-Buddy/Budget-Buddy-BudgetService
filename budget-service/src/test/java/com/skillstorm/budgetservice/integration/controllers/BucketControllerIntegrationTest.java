package com.skillstorm.budgetservice.integration.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillstorm.budgetservice.models.Buckets;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BucketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // Register the JSR310 module
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetAllBuckets() throws Exception {

        mockMvc.perform(get("/buckets/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6));
    }

    @Test
    public void testGetAllBucketsByUserId() throws Exception {

        mockMvc.perform(get("/buckets/user")
                .header("User-ID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetBucketByBucketId() throws Exception {

        mockMvc.perform(get("/buckets/bucket/{bucketId}", 1)
                .header("User-ID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bucketName").value("Vacation Fund"));
    }

    @Test
    public void testGetBucketByBucketIdUserIdNotFound() throws Exception {

        mockMvc.perform(get("/buckets/bucket/{bucketId}", 1)
                .header("User-ID", "-1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetBucketByBucketIdBucketNotPresent() throws Exception {

        mockMvc.perform(get("/buckets/bucket/{bucketId}", -1)
                .header("User-ID", "0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetBucketsByMonthYear() throws Exception {
    
        mockMvc.perform(get("/buckets/monthyear/{monthYear}", "2024-05")
                .header("User-ID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testAddBucket() throws Exception {
        Buckets bucket = new Buckets(1, "hahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());        

        mockMvc.perform(post("/buckets/add")
                .header("User-ID", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bucket)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bucketName").value("hahaha"));
    }

    @Test
    public void testUpdateBucket() throws Exception {
        Buckets bucket2 = new Buckets(1, "hahahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());


        mockMvc.perform(put("/buckets/update/{bucketId}", "1")
                .header("User-ID", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bucket2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bucketName").value("hahahaha"));
    }
    
    @Test
    public void testUpdateBucketNotPresent() throws Exception {
        Buckets bucket2 = new Buckets(1, "hahahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());

        mockMvc.perform(put("/buckets/update/{bucketId}", "0")
                .header("User-ID", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bucket2)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateBucketUserIdNotMatch() throws Exception {
        Buckets bucket2 = new Buckets(1, "hahahaha", BigDecimal.valueOf(1111), BigDecimal.valueOf(333), LocalDate.now(), true, true, LocalDateTime.now());

        mockMvc.perform(put("/buckets/update/{bucketId}", "1")
                .header("User-ID", 0)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bucket2)))
                .andExpect(status().isForbidden());
    }
    
    @Test
    public void testDeleteBucket() throws Exception {

        mockMvc.perform(delete("/buckets/delete/{bucketId}", 1)
                .header("User-ID", "1"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("The bucket is deleted"));
    }

    @Test
    public void testDeleteBucketNotExist() throws Exception {

        mockMvc.perform(delete("/buckets/delete/{bucketId}", 0)
                .header("User-ID", "1"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").value("You do not have permission to delete this bucket"));
    }

    @Test
    public void testDeleteBucketUserIdNotMatch() throws Exception {
        mockMvc.perform(delete("/buckets/delete/{bucketId}", 1)
                .header("User-ID", "0"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").value("You do not have permission to delete this bucket"));
    }

    @Test
    public void testDeleteAllBudgetsByUserId() throws Exception {

        mockMvc.perform(delete("/buckets/deleteAll/user")
                .header("User-ID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("All buckets related to the user are deleted"));
    }
}
