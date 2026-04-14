package org.coffeeshop.users.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.coffeeshop.users.dtos.CreateStaffDto;
import org.coffeeshop.users.dtos.StaffDto;
import org.coffeeshop.users.services.StaffService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Handles HTTP requests related to staff management.
 * <p>
 * This controller exposes the staff API used by the frontend or external clients.
 * It delegates business logic to {@link StaffService} and converts service results into HTTP responses.
 * 
 * @author Umunna David
 * @version 1.0
 * @since 2026-04-12
 */
@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final StaffService staffService;

    /**
     * Creates a new controller instance.
     *
     * @param staffService the service used to manage staff data
     */
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    /**
     * Creates a new staff member from the request body.
     * <p>
     * The request should include the information needed to register a staff user,
     * such as login details and role data. On success, the created staff record is
     * returned with HTTP 201 Created.
     *
     * @param staffDto the staff creation data
     * @return the created staff record
     */
    @PostMapping("/create")
    public ResponseEntity<StaffDto> createStaff(@Valid @RequestBody CreateStaffDto staffDto) {
        StaffDto created = staffService.create(staffDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Retrieves all staff members currently stored in the system.
     *
     * @return the full list of staff records
     */
    @GetMapping("/all")
    public ResponseEntity<List<StaffDto>> getAllStaff() {
        List<StaffDto> allStaff = staffService.getAllStaff();
        return new ResponseEntity<>(allStaff, HttpStatus.OK);
    }

    /**
     * Retrieves a staff member by ID.
     * <p>
     * The service call is asynchronous, so this method returns a {@link CompletableFuture}
     * that completes when the staff record is available.
     *
     * @param id the ID of the staff member to retrieve
     * @return a response containing the matching staff record
     */
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<StaffDto>> getStaffById(@PathVariable("id") Long id) {
        CompletableFuture<StaffDto> staffData = staffService.getStaffById(id);
        return staffData.thenApply(ResponseEntity::ok);
    }

    /**
     * Updates an existing staff member.
     * <p>
     * The staff ID is provided in the path, and the updated details are supplied in the request body.
     * The service performs the update asynchronously and returns the updated record.
     *
     * @param id the ID of the staff member to update
     * @param staffDto the updated staff details
     * @return a response containing the updated staff record
     */
    @PutMapping("/update/{id}")
    public CompletableFuture<ResponseEntity<StaffDto>> updateStaff(@PathVariable("id") Long id, @Valid @RequestBody StaffDto staffDto) {
        CompletableFuture<StaffDto> staffData = staffService.updateStaff(id, staffDto);
        return staffData.thenApply(ResponseEntity::ok);
    }

    /**
     * Deletes a staff member by ID.
     * <p>
     * Instead of returning an empty response, this endpoint sends a simple JSON message
     * so the client receives clear confirmation that the deletion was successful.
     *
     * @param id the ID of the staff member to delete
     * @return a response containing a confirmation message
     */
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Map<String, String>>> deleteStaff(@PathVariable("id") Long id) {
        return staffService.deleteStaff(id)
                .thenApply(msg -> {
                    Map<String, String> body = Map.of("message", msg);
                    return ResponseEntity.ok(body);
                });
    }

}
