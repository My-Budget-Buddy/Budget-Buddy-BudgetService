package com.skillstorm.budgetservice.controllers.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.skillstorm.budgetservice.controllers.MonthlySummaryController;
import com.skillstorm.budgetservice.models.MonthlySummary;
import com.skillstorm.budgetservice.services.MonthlySummaryService;

public class MonthlySummaryControllerTest {
    
    @Mock
    private MonthlySummaryService monthlySummaryService;
    
    @InjectMocks
    private MonthlySummaryController monthlySummaryController;
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
    public void testFindAllSummarys() {
        List<MonthlySummary> summarys = Arrays.asList(new MonthlySummary(), new MonthlySummary());
        
        when(monthlySummaryService.findAllMonthlySummarys()).thenReturn(summarys);
        
        ResponseEntity<List<MonthlySummary>> response = monthlySummaryController.findAllSummarys();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(summarys, response.getBody());
    }

    @Test
    public void testGetSummarysById() {
        List<MonthlySummary> summarys = Arrays.asList(new MonthlySummary(), new MonthlySummary());
        Integer headerUserId = 1;
        
        when(monthlySummaryService.findMonthlySummarysByUserId(headerUserId)).thenReturn(summarys);
        
        ResponseEntity<List<MonthlySummary>> response = monthlySummaryController.getSummarysById(headerUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(summarys, response.getBody());
    }

    @Test
    public void testCreateMonthlySummary() {
        Integer id = 1; 
        MonthlySummary monthlySummary = new MonthlySummary();
        
        when(monthlySummaryService.saveMonthlySummary(monthlySummary, id)).thenReturn(monthlySummary);
        
        ResponseEntity<MonthlySummary> response = monthlySummaryController.createMonthlySummary(monthlySummary, id);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(monthlySummary, response.getBody());
    }

    @Test
    public void testEditMonthlySummary() {
        int id = 1;
        Integer userId = 1;
        MonthlySummary monthlySummary = new MonthlySummary();
        
        when(monthlySummaryService.editMonthlySummary(id, monthlySummary)).thenReturn(monthlySummary);
        
        ResponseEntity<MonthlySummary> response = monthlySummaryController.editMonthlySummary(id, monthlySummary, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(monthlySummary, response.getBody());
    }

    @Test
    public void testDeleteSummary() {
        int id = 1;
        
        ResponseEntity<MonthlySummary> response = monthlySummaryController.deleteSummary(id);

        verify(monthlySummaryService).deleteMonthlySummaryById(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetSummarysByMonthYear() {
        List<MonthlySummary> summarys = Arrays.asList(new MonthlySummary(), new MonthlySummary());
        Integer userId = 1;
        String monthYear = "2021-01";
        LocalDate date = LocalDate.parse(monthYear + "-01");
        
        when(monthlySummaryService.getMonthlySummarysByMonthYearAndUserId(date, userId)).thenReturn(summarys);
        
        ResponseEntity<List<MonthlySummary>> response = monthlySummaryController.getSummarysByMonthYear(monthYear, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(summarys, response.getBody());
    }

    @Test
    public void testDeleteAllSummarysByUserId() {
        Integer userId = 1;
        
        ResponseEntity<MonthlySummary> response = monthlySummaryController.deleteAllSummarysByUserId(userId);

        verify(monthlySummaryService).deleteAllSummarysByUserId(userId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
