package com.rahim.userservice.service;

import com.rahim.userservice.model.User;
import com.rahim.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Override
    public void save(User user) {
        logger.info("Saving user: {}", user);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(int id) {
        logger.debug("Finding user by ID: {}", id);
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAllUsers() {
        logger.info("Finding all users");
        return userRepository.findAll();
    }
}
