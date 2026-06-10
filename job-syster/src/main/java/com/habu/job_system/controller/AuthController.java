package com.habu.job_system.controller;

import com.habu.job_system.entity.User;
import com.habu.job_system.repository.UserRepository;
import com.habu.job_system.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {

        Map<String, Object> res = new HashMap<>();

        // email check
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            res.put("status", "error");
            res.put("message", "Email already exists");
            return res;
        }

        // default role
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }

        userRepository.save(user);

        res.put("status", "success");
        res.put("message", "User registered successfully");
        res.put("role", user.getRole());

        return res;
    }


    // LOGIN korte JWT + ROLE CHECK

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {

        Map<String, Object> res = new HashMap<>();

        Optional<User> dbUser = userRepository.findByEmail(user.getEmail());

        if (dbUser.isPresent()) {

            User u = dbUser.get();

            // password check
            if (!u.getPassword().equals(user.getPassword())) {
                res.put("status", "error");
                res.put("message", "Invalid email or password");
                return res;
            }

            // JWT token generate
            String token = jwtUtil.generateToken(u.getEmail());

            res.put("status", "success");
            res.put("token", token);

            // safe user object (NO password)
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", u.getId());
            userData.put("name", u.getName());
            userData.put("email", u.getEmail());
            userData.put("role", u.getRole());
            userData.put("skills", u.getSkills());

            res.put("user", userData);

            return res;
        }

        res.put("status", "error");
        res.put("message", "User not found");

        return res;
    }
}