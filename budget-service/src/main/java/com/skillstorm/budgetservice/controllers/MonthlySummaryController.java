package com.skillstorm.budgetservice.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.budgetservice.models.Budget;
import com.skillstorm.budgetservice.models.MonthlySummary;
import com.skillstorm.budgetservice.services.MonthlySummaryService;

@RestController
@RequestMapping("/summarys")
public class MonthlySummaryController {

    @Autowired
    MonthlySummaryService monthlySummaryService;

    /**
     * Retrieves all the monthly summarys from the monthly summary repository.
     *
     * @return a ResponseEntity object containing a list of Monthly Summary objects
     *         representing all the monthly summarys found and the HTTP status code
     */
    @GetMapping
    public ResponseEntity<List<MonthlySummary>> findAllSummarys() {
        List<MonthlySummary> summarys = monthlySummaryService.findAllMonthlySummarys();
        return new ResponseEntity<List<MonthlySummary>>(summarys, HttpStatus.OK);
    }

    /**
     * Retrieves the Summary objects associated with the given ID.
     *
     * @param id the ID of the user
     * @return a ResponseEntity containing the Summarys list and the HTTP status
     *         code
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<MonthlySummary>> getSummarysById(@PathVariable int id,
            @RequestHeader(name = "ID") String headerUserId) {

        monthlySummaryService.compareHeaderIdWithRequestedDataId(id, headerUserId);
        List<MonthlySummary> summarys = monthlySummaryService.findMonthlySummarysByUserId(id);
        return new ResponseEntity<List<MonthlySummary>>(summarys, HttpStatus.OK);
    }

    /**
     * Creates a new summary by saving the provided Monthly Summary object.
     *
     * @param budget The Summary object to be created.
     * @return A ResponseEntity object containing the created summary and the HTTP
     *         status code.
     */
    @PostMapping
    public ResponseEntity<MonthlySummary> createMonthlySummary(@RequestBody MonthlySummary monthlySummary) {
        MonthlySummary newSummary = monthlySummaryService.saveMonthlySummary(monthlySummary);
        return new ResponseEntity<MonthlySummary>(newSummary, HttpStatus.CREATED);
    }

    /**
     * Edits the details of a summary with the given ID.
     *
     * @param id  the ID of the summary to edit
     * @param the updated summary object containing the new details
     * @return the updated Summary object
     */
    @PutMapping("/{id}")
    public ResponseEntity<MonthlySummary> editMonthlySummary(@PathVariable int id,
            @RequestBody MonthlySummary monthlySummary, @RequestHeader(name = "ID") String headerUserId) {

        monthlySummaryService.compareHeaderIdWithRequestedDataId(id, headerUserId);

        MonthlySummary updatedMonthlySummary = monthlySummaryService.editMonthlySummary(id, monthlySummary);
        return new ResponseEntity<MonthlySummary>(updatedMonthlySummary, HttpStatus.OK);
    }

    /**
     * Deletes a monthly summary from the database by their ID.
     *
     * @param id the ID of the monthly summary to be deleted
     * @return a ResponseEntity object with no content and the corresponding HTTP
     *         status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MonthlySummary> deleteSummary(@PathVariable int id) {
        monthlySummaryService.deleteMonthlySummaryById(id);
        return ResponseEntity.noContent().build();
    }

    /*
     * Controller to recieve the summarys a user has for a specific month and year
     */
    @GetMapping("/monthyear/{monthYear}/user/{userId}")
    public ResponseEntity<List<MonthlySummary>> getSummarysByMonthYear(@PathVariable String monthYear,
            @PathVariable int userId, @RequestHeader(name = "ID") String headerUserId) {

        monthlySummaryService.compareHeaderIdWithRequestedDataId(userId, headerUserId);

        // Define a DateTimeFormatter to parse the date in the format yyyy-MM-dd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parse the monthYear string to a LocalDate object by appending "-01"
        // This ensures the date is in the format yyyy-MM-dd for LocalDate parsing
        LocalDate date = LocalDate.parse(monthYear + "-01", formatter);
        List<MonthlySummary> summarys = monthlySummaryService.getMonthlySummarysByMonthYearAndUserId(date, userId);

        return new ResponseEntity<List<MonthlySummary>>(summarys, HttpStatus.OK);
    }

    /*
     * Controller to delete all monthly summarys associated with a userId
     * 
     */
    @DeleteMapping("deleteAll/user/{id}")
    public ResponseEntity<MonthlySummary> deleteAllSummarysByUserId(@PathVariable int id,
            @RequestHeader(name = "ID") String headerUserId) {

        monthlySummaryService.compareHeaderIdWithRequestedDataId(id, headerUserId);

        monthlySummaryService.deleteAllSummarysByUserId(id);
        return ResponseEntity.noContent().build();
    }

}
