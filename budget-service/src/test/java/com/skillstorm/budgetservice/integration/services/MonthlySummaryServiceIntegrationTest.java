package com.skillstorm.budgetservice.integration.services;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.skillstorm.budgetservice.models.MonthlySummary;
import com.skillstorm.budgetservice.repositories.MonthlySummaryRepository;
import com.skillstorm.budgetservice.services.MonthlySummaryService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class MonthlySummaryServiceIntegrationTest {
    
    @Autowired
    private MonthlySummaryService monthlySumSvc;          // The class under test

    @Autowired
    private MonthlySummaryRepository monthlySumRepository;    // The class under test's dependency

    /**
     * Setup and teardown methods to clear the database before and after each test.
     */
    @BeforeEach
    public void setup() {
        monthlySumRepository.deleteAll();
    }

    @AfterEach
    public void teardown() {
        monthlySumRepository.deleteAll();
    }

    /**
     * Test case for findAllMonthlySummarys method in MonthlySummaryService class - Successfully finds all monthly summarys.
     */
    @Test
    public void findAllMonthlySummarysTest_Success() {
        MonthlySummary monthlySummary1 = new MonthlySummary(1, BigDecimal.valueOf(80000.00), LocalDate.of(2021, 1, 1), new BigDecimal(5000));
        MonthlySummary monthlySummary2 = new MonthlySummary(2, BigDecimal.valueOf(100000.00), LocalDate.of(2022, 1, 1), new BigDecimal(8000));

        monthlySumRepository.save(monthlySummary1);
        monthlySumRepository.save(monthlySummary2);

        System.out.println(monthlySummary1);

        List<MonthlySummary> result = monthlySumSvc.findAllMonthlySummarys();

        assertTrue(result.size() == 2);    
        assertTrue(result.contains(monthlySummary1));
        assertTrue(result.contains(monthlySummary2));
    }

    /**
     * Test case for findMonthlySummarysByUserId method in MonthlySummaryService class - Successfully finds monthly summarys by user ID.
     */
    @Test
    public void findMonthlySummarysByUserIdTest_Success() {
        MonthlySummary monthlySummary1 = new MonthlySummary(1, BigDecimal.valueOf(80000.00), LocalDate.of(2021, 1, 1), new BigDecimal(5000));
        MonthlySummary monthlySummary2 = new MonthlySummary(2, BigDecimal.valueOf(100000.00), LocalDate.of(2022, 1, 1), new BigDecimal(8000));

        monthlySumRepository.save(monthlySummary1);
        monthlySumRepository.save(monthlySummary2);

        List<MonthlySummary> result = monthlySumSvc.findMonthlySummarysByUserId(1);

        assertTrue(result.size() == 1);    
        assertTrue(result.contains(monthlySummary1));
    }

    /**
     * Test case for saveMonthlySummary method in MonthlySummaryService class - Successfully saves a monthly summary.
     */
    @Test
    public void saveMonthlySummaryTest_Success() {
        MonthlySummary monthlySummary = new MonthlySummary(1, BigDecimal.valueOf(80000.00), LocalDate.of(2021, 1, 1), new BigDecimal(5000));

        MonthlySummary result = monthlySumSvc.saveMonthlySummary(monthlySummary, 1);

        assertTrue(result != null);
        assertTrue(result.getUserId() == 1);
        assertTrue(result.getProjectedIncome().equals(BigDecimal.valueOf(80000.00)));
        assertTrue(result.getMonthYear().equals(LocalDate.of(2021, 1, 1)));
        assertTrue(result.getTotalBudgetAmount().equals(BigDecimal.valueOf(5000)));
    }

    /*
     * Test case for editMonthlySummary method in MonthlySummaryService class - Successfully edits a monthly summary.
     */
    @Test
    public void editMonthlySummaryTest_Success() {
        MonthlySummary monthlySummary = new MonthlySummary(1, BigDecimal.valueOf(80000.00), LocalDate.of(2021, 1, 1), new BigDecimal(5000));

        MonthlySummary result = monthlySumSvc.saveMonthlySummary(monthlySummary, 1);

        assertTrue(result != null);
        assertTrue(result.getUserId() == 1);
        assertTrue(result.getProjectedIncome().equals(BigDecimal.valueOf(80000.00)));
        assertTrue(result.getMonthYear().equals(LocalDate.of(2021, 1, 1)));
        assertTrue(result.getTotalBudgetAmount().equals(BigDecimal.valueOf(5000)));
    }

    /*
     * Test case for deleteMonthlySummaryById method in MonthlySummaryService class - Successfully deletes a monthly summary by its id.
     */
    @Test
    public void deleteMonthlySummaryTest_Success() {
        MonthlySummary monthlySummary = new MonthlySummary(1, BigDecimal.valueOf(80000.00), LocalDate.of(2021, 1, 1), new BigDecimal(5000));

        monthlySumRepository.save(monthlySummary);

        monthlySumSvc.deleteMonthlySummaryById(monthlySummary.getSummaryId());

        assertTrue(monthlySumRepository.findAll().isEmpty());
    }

    /*
     * Test case for deleteMonthlySummaryById method in MonthlySummaryService class - Does not find a monthly summary to be deleted by its id.
     */
    @Test
    public void deleteMonthlySummaryTest_NotFound() {
        MonthlySummary monthlySummary = new MonthlySummary(1, BigDecimal.valueOf(80000.00), LocalDate.of(2021, 1, 1), new BigDecimal(5000));

        monthlySumRepository.save(monthlySummary);

        monthlySumSvc.deleteMonthlySummaryById(2);

        assertTrue(monthlySumRepository.findAll().size() == 1);
    }

    /**
     * Test case for getMonthlySummarysByMonthYearAndUserId method in MonthlySummaryService class - Successfully finds monthly summarys by month, year, and user ID.
     */
    @Test
    public void getMonthlySummarysByMonthYearAndUserIdTest_Success() {
        MonthlySummary monthlySummary1 = new MonthlySummary(1, BigDecimal.valueOf(80000.00), LocalDate.of(2021, 1, 1), new BigDecimal(5000));
        MonthlySummary monthlySummary2 = new MonthlySummary(2, BigDecimal.valueOf(100000.00), LocalDate.of(2022, 1, 1), new BigDecimal(8000));

        monthlySumRepository.save(monthlySummary1);
        monthlySumRepository.save(monthlySummary2);

        List<MonthlySummary> result = monthlySumSvc.getMonthlySummarysByMonthYearAndUserId(LocalDate.of(2021, 1, 1), 1);

        assertTrue(result.size() == 1);
        assertTrue(result.contains(monthlySummary1));
    }

    /**
     * Test case for getMonthlySummarysByMonthYearAndUserId method in MonthlySummaryService class - Does not find any monthly summarys by month, year, and user ID.
     */
    @Test
    public void getMonthlySummarysByMonthYearAndUserIdTest_NotFound() {
        MonthlySummary monthlySummary1 = new MonthlySummary(1, BigDecimal.valueOf(80000.00), LocalDate.of(2021, 1, 1), new BigDecimal(5000));
        monthlySumRepository.save(monthlySummary1);

        List<MonthlySummary> result = monthlySumSvc.getMonthlySummarysByMonthYearAndUserId(LocalDate.of(2022, 1, 1), 1);

        assertTrue(result.isEmpty());
    }

    /**
     * Test case for deleteAllSummarysByUserId method in MonthlySummaryService class - Successfully deletes all monthly summarys by user ID.
     */
    @Test
    public void deleteAllSummarysByUserIdTest_Success() {
        MonthlySummary monthlySummary1 = new MonthlySummary(1, BigDecimal.valueOf(80000.00), LocalDate.of(2021, 1, 1), new BigDecimal(5000));
        MonthlySummary monthlySummary2 = new MonthlySummary(1, BigDecimal.valueOf(100000.00), LocalDate.of(2022, 1, 1), new BigDecimal(8000));

        monthlySumRepository.save(monthlySummary1);
        monthlySumRepository.save(monthlySummary2);

        monthlySumSvc.deleteAllSummarysByUserId(1);

        assertTrue(monthlySumRepository.findAll().isEmpty());
    }

    /**
     * Test case for deleteAllSummarysByUserId method in MonthlySummaryService class - Does not find any monthly summarys to be deleted by user ID.
     */
    @Test
    public void deleteAllSummarysByUserIdTest_NotFound() {
        MonthlySummary monthlySummary1 = new MonthlySummary(1, BigDecimal.valueOf(80000.00), LocalDate.of(2021, 1, 1), new BigDecimal(5000));
        monthlySumRepository.save(monthlySummary1);

        monthlySumSvc.deleteAllSummarysByUserId(2);

        assertTrue(monthlySumRepository.findAll().size() == 1);
    }

}
