package com.rahim.userservice.service.repository.implementation;

import com.rahim.userservice.model.User;
import com.rahim.userservice.repository.UserRepository;
import com.rahim.userservice.service.repository.IUserRepositoryHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserRepositoryHandlerService implements IUserRepositoryHandler {
    private static final Logger LOG = LoggerFactory.getLogger(UserDeletionService.class);
    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(int userId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if(userOptional.isPresent()) {
                LOG.info("Found user account with ID: {}", userId);
            } else {
                LOG.info("User not found for ID: {}", userId);
            }

            return userOptional;
        } catch (Exception e) {
            LOG.error("An error occurred while retrieving user with ID: {}", userId, e);
            return Optional.empty();
        }
    }

    @Override
    public void saveUserAccount(User user) {
        if(!ObjectUtils.anyNull(user)) {
            try {
                userRepository.save(user);
            } catch (DataException e) {
                LOG.error("Error saving user account to the database", e);
                throw new DataIntegrityViolationException("Error saving user account to database", e);
            }
        } else {
            LOG.error("User information provided is null or contains null properties. Unable to save");
            throw new IllegalArgumentException("User information provided is null or contains null properties. Unable to save");
        }
    }

    @Override
    public void deleteUserAccount(int userId) {
        findById(userId).ifPresent(userAccount -> {
            try {
                LOG.info("Deleting user account with ID: {}", userId);
                userRepository.deleteById(userId);
                LOG.info("User account with ID {} deleted successfully", userId);
            } catch (Exception e) {
                LOG.warn("Attempted to delete non-existing user account with ID: {}", userId);
                throw new EntityNotFoundException("User account with ID " + userId + " not found");
            }
        });
    }
}
