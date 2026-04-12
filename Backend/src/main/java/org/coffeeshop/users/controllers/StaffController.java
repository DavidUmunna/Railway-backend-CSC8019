package org.coffeeshop.Users.controllers;

import jakarta.validation.Valid;
import org.coffeeshop.Users.dtos.StaffDto;
import org.coffeeshop.Users.service.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff/create")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<StaffDto> createStaff(@Valid @RequestBody StaffDto staffDto) {
        StaffDto created = staffService.create(staffDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
