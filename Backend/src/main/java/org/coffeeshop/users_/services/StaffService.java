package org.coffeeshop.users.services;

// import org.coffeeshop.users.dtos.StaffDto;
// import org.coffeeshop.users.models.Staff;
// import org.coffeeshop.users.repositories.StaffRepository;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// @Service
// public class StaffService {

//     private final StaffRepository repo;
//     private final PasswordEncoder passwordEncoder;

//     public StaffService(StaffRepository repo,PasswordEncoder passwordEncoder) {
//         this.repo = repo;
//         this.passwordEncoder = passwordEncoder;
//     }

//     // create staff
//     public StaffDto create(StaffDto dto) {
//         Staff staff=fromDto(dto);
//         Staff saved=repo.save(staff);

//         return toDto(saved);
//     }

//     // get staff by id
//     public StaffDto getById(Long id) {

//         Staff staff = repo.findById(id)
//                 .orElseThrow(() -> new IllegalArgumentException("Staff not found"));

//         return toDto(staff);
//     }

//     private StaffDto toDto(Staff staff) {
//         StaffDto dto = new StaffDto();
//         dto.setId(staff.getStaffId());
//         dto.setUsername(staff.getUsername());
//         dto.setFirstName(staff.getFirstName());
//         dto.setLastName(staff.getLastName());

//         dto.setRole(staff.getRole());
//         dto.setActive(staff.isActive());
//         // password is write-only, so not set here
//         return dto;
//     }

//     private Staff fromDto(StaffDto dto) {
//         Staff staff = new Staff();
//         staff.setUsername(dto.getUsername());
//         staff.setFirstName(dto.getFirstName());
//         staff.setLastName(dto.getLastName());

//         staff.setRole(dto.getRole());
//         staff.setActive(dto.isActive());
//         staff.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
//         return staff;
//     }


// }
