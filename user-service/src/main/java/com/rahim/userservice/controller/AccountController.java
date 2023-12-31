package com.rahim.userservice.controller;

import com.rahim.userservice.dto.UserDTO;
import com.rahim.userservice.model.Account;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.service.account.IAccountCreation;
import com.rahim.userservice.service.account.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gold/user-service/user")
public class UserController {
    private final IUserService userService;
    private final IAccountCreation accountCreation;
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        try {
            accountCreation.createAccount(userRequest);
            LOG.info("Successfully Created Account: {}", userRequest.getProfile().getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body("Account and Account Profile created successfully");
        } catch (Exception e) {
            LOG.error("Error creating Account and Account Profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Creating Account and Profile");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findUserById(@PathVariable int userId) {
        try {
            Optional<Account> userOptional = userService.findUserById(userId);

            if (userOptional.isPresent()) {
                Account account = userOptional.get();
                UserDTO userDTO = new UserDTO(account);

                LOG.info("Account found with ID: {}", userId);
                return ResponseEntity.ok(userDTO);
            } else {
                LOG.info("Account not found with ID: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
            }
        } catch (Exception e) {
            LOG.error("Error finding user with ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding user");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable int userId, @RequestBody Map<String, String> updatedData) {
        try {
            userService.updateUser(userId, updatedData);
            return ResponseEntity.ok("Account updated successfully");
        } catch (Exception e) {
            LOG.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user");
        }
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<Account> accounts = userService.findAllUsers();
        List<UserDTO> userDTOs = accounts.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        try {
            boolean deleted = userService.deleteUserRequest(userId);

            if (deleted) {
                return ResponseEntity.ok("Successfully Requested to Delete Account with ID: " + userId);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for ID: " + userId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding user: " + e.getMessage());
        }
    }
}