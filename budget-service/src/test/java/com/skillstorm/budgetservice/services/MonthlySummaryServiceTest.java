package com.skillstorm.budgetservice.services;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.skillstorm.budgetservice.models.MonthlySummary;
import com.skillstorm.budgetservice.repositories.MonthlySummaryRepository;
import com.skillstorm.budgetservice.services.MonthlySummaryService;

public class MonthlySummaryServiceTest {

    @Mock
    private MonthlySummaryRepository monthlySummaryRepository;

    @InjectMocks
    private MonthlySummaryService monthlySummaryService;
    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }
    
    @AfterEach
    public void teardown() throws Exception {
        closeable.close();
    }

    @Test
    public void testFindAllMonthlySummarys() {
        List<MonthlySummary> monthlySummaries = Arrays.asList(new MonthlySummary(), new MonthlySummary());

        when(monthlySummaryRepository.findAll()).thenReturn(monthlySummaries);

        List<MonthlySummary> reponse = monthlySummaryService.findAllMonthlySummarys();

        assertEquals(monthlySummaries, reponse);
    }

    @Test
    public void testFindMonthlySummarysByUserId() {
        List<MonthlySummary> monthlySummaries = Arrays.asList(new MonthlySummary(), new MonthlySummary());

        when(monthlySummaryRepository.findByUserId(anyInt())).thenReturn(monthlySummaries);

        List<MonthlySummary> reponse = monthlySummaryService.findMonthlySummarysByUserId(1);

        assertEquals(monthlySummaries, reponse);
    }
    
    @Test
    public void testGetMonthlySummarysByMonthYearAndUserId() {
        List<MonthlySummary> monthlySummaries = Arrays.asList(new MonthlySummary(), new MonthlySummary());

        when(monthlySummaryRepository.findByMonthYearAndUserId(any(LocalDate.class), anyInt())).thenReturn(monthlySummaries);

        List<MonthlySummary> reponse = monthlySummaryService.getMonthlySummarysByMonthYearAndUserId(LocalDate.now(), 1);

        assertEquals(monthlySummaries, reponse);
    }

    @Test
    public void testSaveMonthlySummary() {
        MonthlySummary monthlySummary = new MonthlySummary(1, 1, BigDecimal.valueOf(5000), LocalDate.of(2023, 5, 1), BigDecimal.valueOf(3000));

        when(monthlySummaryRepository.save(any(MonthlySummary.class))).thenReturn(monthlySummary);

        MonthlySummary response = monthlySummaryService.saveMonthlySummary(monthlySummary, 1);

        assertEquals(monthlySummary, response);
    }

    @Test
    public void testSaveMonthlySummaryHasNullFields() {
        MonthlySummary monthlySummary = new MonthlySummary(1, 1, null, null, null);

        when(monthlySummaryRepository.save(any(MonthlySummary.class))).thenReturn(monthlySummary);

        MonthlySummary response = monthlySummaryService.saveMonthlySummary(monthlySummary, 1);

        assertEquals(monthlySummary, response);
    }

    @Test
    public void testEditMonthlySummary() {
        MonthlySummary monthlySummary = new MonthlySummary(1, 1, BigDecimal.valueOf(5000), LocalDate.of(2023, 5, 1), BigDecimal.valueOf(3000));

        when(monthlySummaryRepository.findById(anyInt())).thenReturn(Optional.of(monthlySummary));
        when(monthlySummaryRepository.save(any(MonthlySummary.class))).thenReturn(monthlySummary);

        MonthlySummary response = monthlySummaryService.editMonthlySummary(1, monthlySummary);

        assertEquals(monthlySummary, response);
    }

    @Test
    public void testEditMonthlySummaryHasNullFields() {
        MonthlySummary monthlySummary = new MonthlySummary(1, 0, null, null, null);

        when(monthlySummaryRepository.findById(anyInt())).thenReturn(Optional.of(monthlySummary));
        when(monthlySummaryRepository.save(any(MonthlySummary.class))).thenReturn(monthlySummary);

        MonthlySummary response = monthlySummaryService.editMonthlySummary(1, monthlySummary);

        assertEquals(monthlySummary, response);
    }

    @Test
    public void testEditMonthlySummaryNotPresent() {
        MonthlySummary monthlySummary = new MonthlySummary(1, 1, BigDecimal.valueOf(5000), LocalDate.of(2023, 5, 1), BigDecimal.valueOf(3000));

        when(monthlySummaryRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(monthlySummaryRepository.save(any(MonthlySummary.class))).thenReturn(monthlySummary);

        MonthlySummary response = monthlySummaryService.editMonthlySummary(1, monthlySummary);

        assertEquals(monthlySummary, response);
    }

    @Test
    public void testDeleteMonthlySummaryById() {
        doNothing().when(monthlySummaryRepository).deleteById(anyInt());

        monthlySummaryService.deleteMonthlySummaryById(0);

        verify(monthlySummaryRepository).deleteById(0);
    }

    @Test
    public void testDeleteAllSummarysByUserId() {
        doNothing().when(monthlySummaryRepository).deleteAllSummarysByUserId(anyInt());

        monthlySummaryService.deleteAllSummarysByUserId(0);

        verify(monthlySummaryRepository).deleteAllSummarysByUserId(0);
    }
}
