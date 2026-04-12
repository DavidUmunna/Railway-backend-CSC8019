package org.coffeeshop.Users.service;

import org.coffeeshop.Users.dtos.StaffDto;
import org.coffeeshop.Users.models.Staff;
import org.coffeeshop.Users.repositorys.StaffRepository;
import org.coffeeshop.security.SecurityRoleUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StaffService {

    private final StaffRepository repo;
    private final PasswordEncoder passwordEncoder;

    public StaffService(StaffRepository repo,PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    // create staff
    public StaffDto create(StaffDto dto) {
        enforceCreatePermissions(dto);

        if (repo.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        Staff staff=fromDto(dto);
        Staff saved=repo.save(staff);

        return toDto(saved);
    }

    // get staff by id
    public StaffDto getById(Long id) {

        Staff staff = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));

        return toDto(staff);
    }

    private StaffDto toDto(Staff staff) {
        StaffDto dto = new StaffDto();
        dto.setId(staff.getStaffId());
        dto.setUsername(staff.getUsername());
        dto.setFirstName(staff.getFirstName());
        dto.setLastName(staff.getLastName());

        dto.setRole(staff.getRole());
        dto.setActive(staff.isActive());
        // password is write-only, so not set here
        return dto;
    }

    private Staff fromDto(StaffDto dto) {
        Staff staff = new Staff();
        staff.setUsername(dto.getUsername());
        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());

        staff.setRole(dto.getRole().trim());
        staff.setActive(dto.isActive());
        staff.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        return staff;
    }

    private void enforceCreatePermissions(StaffDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            throw new AccessDeniedException("Authentication required");
        }

        Set<String> requesterAuthorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (requesterAuthorities.contains("ROLE_ADMIN")) {
            return;
        }

        if (requesterAuthorities.contains("ROLE_MANAGER")) {
            String requestedRole = SecurityRoleUtils.toAuthority(dto.getRole());
            if ("ROLE_ADMIN".equals(requestedRole) || "ROLE_MANAGER".equals(requestedRole)) {
                throw new AccessDeniedException("Managers cannot create admin or manager accounts");
            }
            if (!dto.isActive()) {
                throw new AccessDeniedException("Managers cannot create inactive accounts");
            }
            return;
        }

        throw new AccessDeniedException("Insufficient permissions to create staff accounts");
    }


}
