package com.habu.job_system.controller;

import com.habu.job_system.entity.Company;
import com.habu.job_system.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/company")
@CrossOrigin(origins = "*")
public class CompanyAuthController {

    @Autowired
    private CompanyRepository repo;

    // =====================
    // REGISTER
    // =====================
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Company c) {
        Map<String, Object> res = new HashMap<>();

        if (repo.findByEmail(c.getEmail()).isPresent()) {
            res.put("status", "error");
            res.put("message", "Email already registered. Please login.");
            return res;
        }

        Company saved = repo.save(c);

        res.put("status", "success");
        res.put("message", "Company registered successfully");
        res.put("company", toSafe(saved));

        return res;
    }

    // =====================
    // LOGIN
    // =====================
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Company c) {
        Map<String, Object> res = new HashMap<>();

        Optional<Company> db = repo.findByEmail(c.getEmail());

        if (db.isPresent() && db.get().getPassword().equals(c.getPassword())) {
            res.put("status", "success");
            res.put("company", toSafe(db.get()));
        } else {
            res.put("status", "error");
            res.put("message", "Invalid email or password");
        }

        return res;
    }

    // =====================
    // GET PROFILE BY EMAIL
    // =====================
    @GetMapping("/profile/{email}")
    public Map<String, Object> getProfile(@PathVariable String email) {
        Map<String, Object> res = new HashMap<>();
        Optional<Company> db = repo.findByEmail(email);

        if (db.isPresent()) {
            res.put("status", "success");
            res.put("company", toSafe(db.get()));
        } else {
            res.put("status", "error");
            res.put("message", "Company not found");
        }
        return res;
    }

    // =====================
    // UPDATE PROFILE
    // =====================
    @PutMapping("/profile/{email}")
    public Map<String, Object> updateProfile(@PathVariable String email,
                                              @RequestBody Company updated) {
        Map<String, Object> res = new HashMap<>();
        Optional<Company> db = repo.findByEmail(email);

        if (db.isPresent()) {
            Company c = db.get();
            if (updated.getName() != null)        c.setName(updated.getName());
            if (updated.getIndustry() != null)    c.setIndustry(updated.getIndustry());
            if (updated.getDescription() != null) c.setDescription(updated.getDescription());
            if (updated.getPhone() != null)       c.setPhone(updated.getPhone());
            if (updated.getWebsite() != null)     c.setWebsite(updated.getWebsite());
            if (updated.getLocation() != null)    c.setLocation(updated.getLocation());

            Company saved = repo.save(c);
            res.put("status", "success");
            res.put("message", "Profile updated successfully");
            res.put("company", toSafe(saved));
        } else {
            res.put("status", "error");
            res.put("message", "Company not found");
        }
        return res;
    }

    // =====================
    // ALL COMPANIES (admin)
    // =====================
    @GetMapping("/all")
    public List<Company> getAllCompanies() {
        return repo.findAll();
    }

    // =====================
    // HELPER: safe map (no password)
    // =====================
    private Map<String, Object> toSafe(Company c) {
        Map<String, Object> m = new HashMap<>();
        m.put("id",          c.getId());
        m.put("name",        c.getName());
        m.put("email",       c.getEmail());
        m.put("role",        c.getRole());
        m.put("industry",    c.getIndustry());
        m.put("description", c.getDescription());
        m.put("phone",       c.getPhone());
        m.put("website",     c.getWebsite());
        m.put("location",    c.getLocation());
        return m;
    }
}