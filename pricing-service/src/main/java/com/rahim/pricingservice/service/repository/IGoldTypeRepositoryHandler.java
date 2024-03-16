package com.rahim.pricingservice.service.repository;

import com.rahim.pricingservice.model.GoldType;

import java.util.List;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
public interface IGoldTypeRepositoryHandler {

    /**
     * Retrieves a list of all GoldType IDs.
     * This method fetches all GoldType entities from the repository and extracts their IDs.
     * It returns a list containing the IDs of all GoldType entities.
     *
     * @return A list of Integer representing the IDs of all GoldType entities.
     * @throws RuntimeException if an error occurs while fetching the GoldType IDs.
     */
    List<Integer> allGoldTypeIds();

    /**
     * Retrieves a GoldType entity by its ID.
     * This method retrieves a GoldType entity from the repository using the provided goldTypeId.
     * If the entity is found, it returns an Optional containing the GoldType.
     * If the entity is not found or an error occurs during the retrieval process, it returns an empty Optional.
     *
     * @param goldTypeId The ID of the GoldType entity to retrieve.
     * @return An Optional containing the GoldType entity if found, or an empty Optional if not found or an error occurs.
     */
    Optional<GoldType> findById(Integer goldId);
}
