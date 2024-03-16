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

    /**
     * Retrieves all GoldTypes from the database.
     *
     * @return A list of GoldType objects.
     */
    List<GoldType> getAllGoldTypes();

    /**
     * Saves a GoldType entity to the database.
     * This method saves the provided GoldPrice entity to the database using the goldTypeRepository.
     * It performs validation to ensure that the provided goldType is not null and contains valid properties.
     *
     * @param goldPrice The GoldType entity to be saved.
     * @throws IllegalArgumentException if the provided goldPrice is null or contains null properties.
     * @throws DataIntegrityViolationException if an error occurs while saving the gold type to the database.
     */
    void saveGoldType(GoldType goldType);
}
