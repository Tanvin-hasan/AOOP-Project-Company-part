package com.habu.job_system.controller;

import com.habu.job_system.entity.Job;
import com.habu.job_system.repository.JobRepository;
import com.habu.job_system.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    @Autowired
    private JobRepository repo;

    @Autowired
    private JobService service;

    // POST A NEW JOB
    @PostMapping("/post")
    public Map<String, Object> postJob(@RequestBody Job job) {
        Map<String, Object> res = new HashMap<>();
        job.setStatus("ACTIVE");
        Job saved = repo.save(job);
        service.processJob(saved);
        res.put("status", "success");
        res.put("job", saved);
        return res;
    }

    // GET ALL JOBS FOR A COMPANY
    @GetMapping("/company/{name}")
    public List<Job> getCompanyJobs(@PathVariable String name) {
        return repo.findByCompanyName(name);
    }

    // GET ALL JOBS (public)
    @GetMapping("/all")
    public List<Job> getAllJobs() {
        return repo.findAll();
    }

    // GET SINGLE JOB BY ID
    @GetMapping("/{id}")
    public Job getJobById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    // DELETE A JOB
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteJob(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        if (repo.existsById(id)) {
            repo.deleteById(id);
            res.put("status", "success");
            res.put("message", "Job deleted successfully");
        } else {
            res.put("status", "error");
            res.put("message", "Job not found");
        }
        return res;
    }

    // GET STATS FOR A COMPANY
    @GetMapping("/company/{name}/stats")
    public Map<String, Object> getCompanyStats(@PathVariable String name) {
        Map<String, Object> res = new HashMap<>();
        List<Job> jobs = repo.findByCompanyName(name);
        long active  = jobs.stream().filter(j -> "ACTIVE".equals(j.getStatus())).count();
        long closed  = jobs.stream().filter(j -> "CLOSED".equals(j.getStatus())).count();
        res.put("total",  jobs.size());
        res.put("active", active);
        res.put("closed", closed);
        return res;
    }
}