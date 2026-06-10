package com.habu.job_system.controller;

import com.habu.job_system.entity.User;
import com.habu.job_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")

@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // CREATE USER
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // ALL USERS
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // USER BY ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // USER BY EMAIL
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {

        Optional<User> user =
                userRepository.findByEmail(email);

        return user.orElse(null);
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,
                           @RequestBody User newUser) {

        Optional<User> optionalUser =
                userRepository.findById(id);

        if(optionalUser.isPresent()) {

            User user = optionalUser.get();

            user.setName(newUser.getName());

            user.setEmail(newUser.getEmail());

            user.setSkills(newUser.getSkills());

            return userRepository.save(user);
        }

        return null;
    }

    // UPDATE PROFILE
    @PutMapping("/profile/{email}")
    public User updateProfile(@PathVariable String email,
                              @RequestBody User updatedUser) {

        Optional<User> optionalUser =
                userRepository.findByEmail(email);

        if(optionalUser.isPresent()) {

            User user = optionalUser.get();

            user.setName(updatedUser.getName());

            user.setSkills(updatedUser.getSkills());

            return userRepository.save(user);
        }

        return null;
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {

        userRepository.deleteById(id);

        return "User deleted successfully";
    }

}