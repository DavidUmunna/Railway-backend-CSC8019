package org.coffeeshop.UserTests.StaffTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.coffeeshop.users.dtos.CreateStaffDto;
import org.coffeeshop.users.dtos.StaffDto;
import org.coffeeshop.users.models.Staff;
import org.coffeeshop.users.repositories.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for StaffController.
 * It uses Spring Boot's testing support to perform integration tests on the StaffController endpoints.
 * The tests cover creating, retrieving, updating, and deleting staff members.
 * 
 * @author Umunna David
 * @version 1.0
 * @since 2026-04-12
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
class StaffControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StaffRepository staffRepository;

    @BeforeEach
    void setUp() {
        staffRepository.deleteAll();
    }

    /**
     * Test for creating a staff member via the controller.
     * 
     * @throws Exception
     */

    @Test
    void createStaff_persistsToDb() throws Exception {
        CreateStaffDto request = new CreateStaffDto(
                "staff.create@example.com",
                "Davina",
                "Odili",
                "staff_user",
                "secret123"
        );

        String requestJson = objectMapper.writeValueAsString(Map.of(
                "username", request.username(),
                "firstName", request.firstName(),
                "lastName", request.lastName(),
                "role", request.role(),
                "password", request.password()
        ));

        mockMvc.perform(post("/api/v1/staff/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value("staff.create@example.com"))
                .andExpect(jsonPath("$.firstName").value("Davina"))
                .andExpect(jsonPath("$.lastName").value("Odili"))
                .andExpect(jsonPath("$.role").value("staff_user"));

        assertTrue(staffRepository.existsByUsername("staff.create@example.com"));
    }

    @Test
    void getStaff_returnsStaff() throws Exception {
        Staff savedStaff = staffRepository.save(
                new Staff("barista1@example.com", "Alex", "Brown", "staff_user", "encoded-password")
        );

        mockMvc.perform(get("/api/v1/staff/{id}", savedStaff.getStaffId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedStaff.getStaffId()))
                .andExpect(jsonPath("$.username").value("barista1@example.com"))
                .andExpect(jsonPath("$.firstName").value("Alex"))
                .andExpect(jsonPath("$.lastName").value("Brown"))
                .andExpect(jsonPath("$.role").value("staff_user"));

        assertTrue(staffRepository.existsByUsername("barista1@example.com"));
        assertTrue(staffRepository.existsById(savedStaff.getStaffId()));
    }

    @Test
    void getAllStaff_returnsAllStaff() throws Exception {
        staffRepository.save(new Staff("staff.one@example.com", "Alex", "Brown", "staff_user", "encoded-password-1"));
        staffRepository.save(new Staff("staff.two@example.com", "Jamie", "Smith", "Admin", "encoded-password-2"));

        mockMvc.perform(get("/api/v1/staff/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].username", containsInAnyOrder("staff.one@example.com", "staff.two@example.com")));
    }

    @Test
    void updateStaff_returnsUpdatedStaff() throws Exception {
        Staff savedStaff = staffRepository.save(
                new Staff("staff.update@example.com", "Alex", "Brown", "staff_user", "encoded-password")
        );

        StaffDto updatedStaff = new StaffDto(
                savedStaff.getStaffId(),
                "staff.updated@example.com",
                "John",
                "Umunna",
                "Admin",
                "newpassword123"
        );

        String json = objectMapper.writeValueAsString(updatedStaff);
        mockMvc.perform(put("/api/v1/staff/update/{id}", updatedStaff.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedStaff.getStaffId()))
                .andExpect(jsonPath("$.username").value("staff.updated@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Umunna"))
                .andExpect(jsonPath("$.role").value("Admin"));

        assertTrue(staffRepository.findById(savedStaff.getStaffId())
                .map(staff -> "staff.updated@example.com".equals(staff.getUsername()))
                .orElse(false));
    }

    @Test
    void deleteStaff_returnsStringMessage() throws Exception {
        Staff savedStaff = staffRepository.save(
                new Staff("staff.delete@example.com", "Delete", "Me", "staff_user", "encoded-password")
        );

        mockMvc.perform(delete("/api/v1/staff/{id}", savedStaff.getStaffId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Staff deleted"));

        assertFalse(staffRepository.existsById(savedStaff.getStaffId()));
    }

    /**
     * this test asserts that the request body id does not override the path id 
     * during an update operation. the aim is to ensure that the path id remains authoritative 
     * and that the staff record identified by the path id is the one that gets updated, even if the body contains a different id.
     * @throws Exception
     */
    @Test
    void updateStaff_pathIdIsAuthoritative_whenBodyIdDiffers() throws Exception {
        // Create two staff members
        Staff target = staffRepository.save(
                new Staff("staff.target@example.com", "Target", "User", "staff_user", "encoded-password")
        );
        Staff other = staffRepository.save(
                new Staff("staff.other@example.com", "Other", "User", "staff_user", "encoded-password")
        );

        // Body contains the id of 'other', but path has the id of 'target'
        StaffDto bodyWithDifferentId = new StaffDto(
                other.getStaffId(),
                "staff.updated@example.com",
                "Updated",
                "Name",
                "Admin",
                "newpassword123"
        );

        String json = objectMapper.writeValueAsString(bodyWithDifferentId);
        mockMvc.perform(put("/api/v1/staff/update/{id}", target.getStaffId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(target.getStaffId()))
                .andExpect(jsonPath("$.username").value("staff.updated@example.com"));

        // target should be updated, other should be unchanged
        assertTrue(staffRepository.findById(target.getStaffId())
                .map(staff -> "staff.updated@example.com".equals(staff.getUsername()))
                .orElse(false));
        assertTrue(staffRepository.findById(other.getStaffId())
                .map(staff -> "staff.other@example.com".equals(staff.getUsername()))
                .orElse(false));
    }
}
