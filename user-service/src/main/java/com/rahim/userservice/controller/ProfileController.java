package com.rahim.userservice.controller;

import com.rahim.userservice.entity.Profile;
import com.rahim.userservice.request.profile.ProfileUpdateRequest;
import com.rahim.userservice.service.profile.IProfileUpdateService;
import com.rahim.userservice.service.repository.IProfileRepositoryHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.rahim.userservice.constant.ProfileControllerEndpoint.*;

/**
 * @author Rahim Ahmed
 * @created 05/11/2023
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
@Tag(name = "Profile Management", description = "Endpoints for managing user profiles")
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);
    private final IProfileUpdateService profileUpdateService;
    private final IProfileRepositoryHandler profileRepositoryHandler;

    @Operation(summary = "Get all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all profiles", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Profile.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllProfiles() {
        log.info("Received request to retrieve all profiles");
        List<Profile> profiles = profileRepositoryHandler.getAllProfiles();
        return ResponseEntity.status(HttpStatus.OK).body(profiles);
    }

    @Operation(summary = "Update an existing profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
    })
    @PutMapping(value = PROFILE_ID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profile> updateUserProfile(
            @Parameter(description = "ID of the profile to be updated", required = true) @PathVariable int profileId,
            @Parameter(description = "Map of updated profile data", required = true) @RequestBody ProfileUpdateRequest updatedData) {
        log.info("Received request to update profile");
        Profile profile = profileUpdateService.updateProfile(profileId, updatedData);
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }


    @Operation(summary = "Find profile by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping(value = USERNAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findProfileByUsername(@Parameter(description = "Username of the profile to be found", required = true) @PathVariable String username) {
        log.info("Received request to find profile by username");
        Profile profile = profileRepositoryHandler.getProfileByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(profile);
    }
}