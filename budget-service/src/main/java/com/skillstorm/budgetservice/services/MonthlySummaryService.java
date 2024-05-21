package com.skillstorm.budgetservice.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.models.MonthlySummary;
import com.skillstorm.budgetservice.repositories.MonthlySummaryRepository;

@Service
public class MonthlySummaryService {

    @Autowired
    MonthlySummaryRepository monthlySummaryRepository;

    public List<MonthlySummary> findAllMonthlySummarys() {
        return monthlySummaryRepository.findAll();
    }

    /**
     * Find monthly summarys by user ID.
     *
     * @param id the ID of the user to find monthly summary by
     * @return the Monthly Summary with the specified ID, or null if no Monthly
     *         Summary is found
     */
    public List<MonthlySummary> findMonthlySummarysByUserId(int id) {
        return monthlySummaryRepository.findByUserId(id);
    }

    /**
     * Saves the given monthly summary into the repository.
     *
     * @param user The monthly summary to be saved.
     * @return The saved monthly summary.
     */
    public MonthlySummary saveMonthlySummary(MonthlySummary monthlySummary) {
        return monthlySummaryRepository.save(monthlySummary);
    }

    /**
     * Edits an existing monthly summary in the system with the specified ID.
     *
     * @param id     The ID of the monthly summary to edit.
     * @param budget The updated monthly summary object
     * @return The edited monthly summary object.
     */
    public MonthlySummary editMonthlySummary(int id, MonthlySummary monthlySummary) {
        Optional<MonthlySummary> existingMonthlySummaryOptional = monthlySummaryRepository.findById(id);

        if (existingMonthlySummaryOptional.isPresent()) {
            MonthlySummary existingMonthlySummary = existingMonthlySummaryOptional.get();

            // Check if fields in the incoming summary are not null, then update
            if (monthlySummary.getUserId() != 0) {
                existingMonthlySummary.setUserId(monthlySummary.getUserId());
            }
            if (monthlySummary.getProjectedIncome() != null) {
                existingMonthlySummary.setProjectedIncome(monthlySummary.getProjectedIncome());
            }
            if (monthlySummary.getMonthYear() != null) {
                existingMonthlySummary.setMonthYear(monthlySummary.getMonthYear());
            }
            if (monthlySummary.getTotalBudgetAmount() != null) {
                existingMonthlySummary.setTotalBudgetAmount(monthlySummary.getTotalBudgetAmount());
            }

            return monthlySummaryRepository.save(existingMonthlySummary);
        } else {
            // If the summary does not exist, create a new one
            return monthlySummaryRepository.save(monthlySummary);
        }
    }

    /**
     * Deletes a monthly summary by their ID.
     *
     * @param id the ID of the monthly summary to be deleted
     */
    public void deleteMonthlySummaryById(int id) {
        monthlySummaryRepository.deleteById(id);
    }

    public List<MonthlySummary> getMonthlySummarysByMonthYearAndUserId(LocalDate date, int userId) {
        return monthlySummaryRepository.findByMonthYearAndUserId(date, userId);
    }

}
