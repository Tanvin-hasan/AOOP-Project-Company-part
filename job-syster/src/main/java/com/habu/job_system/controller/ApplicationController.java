package com.habu.job_system.controller;

import com.habu.job_system.entity.Application;
import com.habu.job_system.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/application")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationRepository repository;

    // APPLY FOR A JOB
    @PostMapping("/apply")
    public Map<String, Object> apply(@RequestBody Application app) {
        Map<String, Object> res = new HashMap<>();
        Application saved = repository.save(app);
        res.put("status", "success");
        res.put("application", saved);
        return res;
    }

    // ALL APPLICATIONS (admin)
    @GetMapping("/all")
    public List<Application> all() {
        return repository.findAll();
    }

    // APPLICATIONS BY USER EMAIL
    @GetMapping("/user/{email}")
    public List<Application> userApplications(@PathVariable String email) {
        return repository.findByUserEmail(email);
    }

    // APPLICATIONS FOR A COMPANY (by company name)
    @GetMapping("/company/{companyName}")
    public List<Application> companyApplications(@PathVariable String companyName) {
        return repository.findByCompanyName(companyName);
    }

    // UPDATE APPLICATION STATUS (ACCEPTED / REJECTED)
    @PutMapping("/{id}/status")
    public Map<String, Object> updateStatus(@PathVariable Long id,
                                             @RequestBody Map<String, String> body) {
        Map<String, Object> res = new HashMap<>();
        Optional<Application> opt = repository.findById(id);

        if (opt.isPresent()) {
            Application app = opt.get();
            String newStatus = body.get("status");
            if (newStatus != null && !newStatus.isBlank()) {
                app.setStatus(newStatus.toUpperCase());
                repository.save(app);
                res.put("status", "success");
                res.put("message", "Application status updated to " + newStatus.toUpperCase());
                res.put("application", app);
            } else {
                res.put("status", "error");
                res.put("message", "Status value is missing");
            }
        } else {
            res.put("status", "error");
            res.put("message", "Application not found");
        }
        return res;
    }

    // COMPANY APPLICATION STATS
    @GetMapping("/company/{companyName}/stats")
    public Map<String, Object> companyStats(@PathVariable String companyName) {
        Map<String, Object> res = new HashMap<>();
        List<Application> apps = repository.findByCompanyName(companyName);
        long pending  = apps.stream().filter(a -> "PENDING".equals(a.getStatus())).count();
        long accepted = apps.stream().filter(a -> "ACCEPTED".equals(a.getStatus())).count();
        long rejected = apps.stream().filter(a -> "REJECTED".equals(a.getStatus())).count();
        res.put("total",    apps.size());
        res.put("pending",  pending);
        res.put("accepted", accepted);
        res.put("rejected", rejected);
        return res;
    }
}