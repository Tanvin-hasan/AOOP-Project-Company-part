package com.habu.job_system.controller;

import com.habu.job_system.entity.Report;
import com.habu.job_system.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportRepository repo;

    // USER SEND REPORT
    @PostMapping("/send")
    public Report sendReport(@RequestBody Report report) {

        report.setStatus("PENDING");
        return repo.save(report);
    }

    // ADMIN - ALL REPORTS DEKHBE
    @GetMapping("/all")
    public List<Report> getAllReports() {
        return repo.findAll();
    }

    // ADMIN - STATUS UPDATE (RESOLVED / REJECTED)
    @PutMapping("/update/{id}")
    public Report updateReportStatus(
            @PathVariable Long id,
            @RequestBody Report updated
    ) {
        Report r = repo.findById(id).orElseThrow();

        r.setStatus(updated.getStatus());

        return repo.save(r);
    }
}