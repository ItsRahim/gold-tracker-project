package com.rahim.userservice.controller;

import com.rahim.userservice.model.User;
import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.service.IUserProfileService;
import com.rahim.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;
    private final IUserProfileService userProfileService;

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody User user, @RequestBody UserProfile userProfile) {
        userService.save(user);

        userProfile.setUser(user);
        userProfileService.save(userProfile);

        return ResponseEntity.status(HttpStatus.CREATED).body("User and UserProfile created successfully.");
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Object> findAccount(@PathVariable int id) {
        Optional<User> userOptional = userService.findById(id);

        return userOptional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/account")
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = userService.findAllUsers();

        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<List<UserProfile>> findAllProfiles() {
        List<UserProfile> userProfiles = userProfileService.findAllUserProfiles();

        if (!userProfiles.isEmpty()) {
            return ResponseEntity.ok(userProfiles);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<Object> findProfile(@PathVariable int id) {
        Optional<UserProfile> userOptional = userProfileService.findById(id);

        return userOptional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
