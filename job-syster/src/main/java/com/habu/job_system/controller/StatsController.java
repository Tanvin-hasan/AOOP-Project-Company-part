package com.habu.job_system.controller;

import com.habu.job_system.repository.ApplicationRepository;
import com.habu.job_system.repository.JobRepository;
import com.habu.job_system.repository.UserRepository;
import com.habu.job_system.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stats")
@CrossOrigin(origins = "*")
public class StatsController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping
    public Map<String, Object> getStats() {
        Map<String, Object> res = new HashMap<>();

        long totalJobs      = jobRepository.count();
        long totalUsers     = userRepository.count();
        long totalCompanies = companyRepository.count();
        long totalApps      = applicationRepository.count();

        long accepted = applicationRepository.findAll()
                .stream().filter(a -> "ACCEPTED".equals(a.getStatus())).count();
        long rejected = applicationRepository.findAll()
                .stream().filter(a -> "REJECTED".equals(a.getStatus())).count();
        long pending  = applicationRepository.findAll()
                .stream().filter(a -> "PENDING".equals(a.getStatus())).count();

        res.put("totalJobs",       totalJobs);
        res.put("totalUsers",      totalUsers);
        res.put("totalCompanies",  totalCompanies);
        res.put("totalApplications", totalApps);
        res.put("accepted",        accepted);
        res.put("rejected",        rejected);
        res.put("pending",         pending);

        return res;
    }
}