package org.coffeeshop.securitytests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.coffeeshop.users.models.Staff;
import org.coffeeshop.users.repositories.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



/**
 * Integration tests for authentication and authorization of the API.
 * @author willian
 * @version 1.0
 * @since 15/04/2026
 * @ModifiedBy Umunna David
 * @since 17/04/2026
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthSecurityIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        staffRepository.deleteAll();
        staffRepository.save(new Staff(
                "admin@example.com",
                "System",
                "Admin",
                "ADMIN",
                passwordEncoder.encode("Admin123!")
        ));
    }

    @Test
    void login_returnsJwtForValidCredentials() throws Exception {
        String requestJson = """
                {
                  "username": "admin@example.com",
                  "password": "Admin123!"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.username").value("admin@example.com"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }
    @Test
    void login_rejectsInvalidCredentials() throws Exception {
        String requestJson = """
                {
                "username": "admin@example.com",
                "password": "wrongpassword"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createStaff_requiresAuthentication() throws Exception {
        mockMvc.perform(post("/api/v1/staff/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createStaffRequestJson()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createStaff_succeedsWithValidAdminToken() throws Exception {
        String token = loginAndGetToken("admin@example.com", "Admin123!");

        mockMvc.perform(post("/api/v1/staff/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createStaffRequestJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("staff.create@example.com"))
                .andExpect(jsonPath("$.role").value("STAFF_USER"));

        assertTrue(staffRepository.existsByUsername("staff.create@example.com"));
    }

    @Test
    void createStaff_forbidsNonAdminToken() throws Exception {
        staffRepository.save(new Staff(
                "barista@example.com",
                "Barista",
                "User",
                "STAFF_USER",
                passwordEncoder.encode("Barista123!")
        ));

        String token = loginAndGetToken("barista@example.com", "Barista123!");

        mockMvc.perform(post("/api/v1/staff/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createStaffRequestJson()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void getAllStaff_requiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/staff/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getAllStaff_forbidsNonAdminToken() throws Exception {
        staffRepository.save(new Staff(
                "barista2@example.com",
                "Barista",
                "User",
                "STAFF_USER",
                passwordEncoder.encode("Barista123!")
        ));

        String token = loginAndGetToken("barista2@example.com", "Barista123!");

        mockMvc.perform(get("/api/v1/staff/all")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    private String loginAndGetToken(String username, String password) throws Exception {
        String requestJson = objectMapper.writeValueAsString(new LoginRequest(username, password));

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("token").asText();
    }

    private record LoginRequest(String username, String password) {
    }

    private String createStaffRequestJson() {
        return """
                {
                  "username": "staff.create@example.com",
                  "firstName": "Davina",
                  "lastName": "Odili",
                  "role": "STAFF_USER",
                  "password": "secret123"
                }
                """;
    }
}
