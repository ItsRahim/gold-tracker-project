package com.rahim.accountservice.service.account;

/**
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
public interface IInternalAccountService {

    /**
     * Runs the cleanup job for the service. This method is triggered by a Kafka message from the schedule service.
     * The cleanup job involves finding all inactive users, processing them, and then processing users pending deletion.
     *
     * @throws RuntimeException If an error occurs while running the cleanup job.
     */
    void runCleanupJob();
}