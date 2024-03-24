package com.rahim.pricingservice.service.repository;

import com.rahim.pricingservice.dto.GoldPriceDTO;
import com.rahim.pricingservice.model.GoldPrice;

import java.util.List;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
public interface IGoldPriceRepositoryHandler {

    /**
     * Saves a GoldPrice entity to the database.
     * This method saves the provided GoldPrice entity to the database using the goldPriceRepository.
     * It performs validation to ensure that the provided goldPrice is not null and contains valid properties.
     *
     * @param goldPrice The GoldPrice entity to be saved.
     * @throws IllegalArgumentException if the provided goldPrice is null or contains null properties.
     * @throws DataIntegrityViolationException if an error occurs while saving the gold price to the database.
     */
    void saveGoldPrice(GoldPrice goldPrice);

    /**
     * Retrieves a GoldPrice entity by its ID.
     * This method retrieves a GoldPrice entity from the repository using the provided goldId.
     * If the entity is found, it returns an Optional containing the GoldPrice.
     * If the entity is not found or an error occurs during the retrieval process, it returns an empty Optional.
     *
     * @param goldId The ID of the GoldPrice entity to retrieve.
     * @return An Optional containing the GoldPrice entity if found, or an empty Optional if not found or an error occurs.
     */
    Optional<GoldPrice> findById(int goldId);

    /**
     * Retrieves a GoldType entity by its ID.
     * This method retrieves a GoldType entity from the repository using the provided goldTypeId.
     * If the entity is found, it returns an Optional containing the GoldType.
     * If the entity is not found or an error occurs during the retrieval process, it returns an empty Optional.
     *
     * @param goldTypeId The ID of the GoldType entity to retrieve.
     * @return An Optional containing the GoldPrice entity if found, or an empty Optional if not found or an error occurs.
     */
    List<GoldPrice> findByTypeId (int goldTypeId);

    /**
     * Retrieves a GoldPriceDTO by its ID.
     * This method retrieves a GoldPrice entity from the repository using the provided goldId.
     * If the entity is found, it maps it to a GoldPriceDTO object and returns it.
     * If the entity is not found, it returns an empty Optional.
     * If an error occurs during the retrieval process, it throws a RuntimeException.
     *
     * @param goldId The ID of the GoldPrice entity to retrieve.
     * @return An Optional containing the GoldPriceDTO if found, or an empty Optional if not found.
     * @throws RuntimeException if an error occurs while retrieving or mapping the entity.
     */
    Optional<GoldPriceDTO> getGoldPrice(int goldId);

    /**
     * Retrieves all GoldPriceDTOs.
     * This method retrieves all GoldPrice entities from the repository and maps them to GoldPriceDTOs.
     *
     * @return A list of GoldPriceDTOs.
     * @throws RuntimeException if an error occurs while retrieving or mapping the entities.
     */
    List<GoldPriceDTO> getAllGoldPrices();

    /**
     * Deletes the gold price associated with the given gold type ID.
     * This method deletes the gold price associated with the given gold type ID from the repository.
     * If a gold price is found and deleted successfully, it logs an info message.
     * If the gold type is not found, it logs a warning message.
     * If an error occurs during the deletion process, it throws a RuntimeException.
     *
     * @param goldTypeId The ID of the gold type for which the associated price should be deleted.
     * @throws RuntimeException if an error occurs during the deletion process.
     */
    void deleteGoldPrice(int goldTypeId);
}
