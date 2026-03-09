package org.coffeeshop.Users.controllers;

import org.coffeeshop.Users.dtos.StaffDto;
import org.coffeeshop.Users.service.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff/create")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    public ResponseEntity<StaffDto> createStaff(@RequestBody StaffDto staffDto) {
        StaffDto created = staffService.create(staffDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
