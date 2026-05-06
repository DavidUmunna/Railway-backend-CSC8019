package org.coffeeshop.securitytests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.coffeeshop.users.models.Staff;
import org.coffeeshop.users.repositories.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for authentication and authorization of the API.
 * Tests are grouped by concern using @Nested classes.
 *
 * @author William Liang
 * @version 1.0
 * @since 2026-04-15
 * @modifiedby Umunna David
 * @since 2026-04-17
 * @modifiedby Kulagina Tatiana
 * @since 2026-04-28
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthSecurityIntegrationTests {
    private static final Long FAULTY_ID = 999L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Resets the staff table and seeds an ADMIN user before each test
     * to ensure a consistent authentication baseline.
     */
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

    /**
     * Tests for POST /api/v1/auth/login — authentication scenarios
     * including valid credentials, invalid password, non-existent username,
     * and missing request fields.
     */
    @Nested
    class LoginTests {
        /**
         * Verifies that logging in with valid admin credentials returns 200 OK
         * with a JWT token, token type, username, and role in the response.
         *
         * @throws Exception if the MockMvc request fails
         */
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

        /**
         * Verifies that logging in with a valid username but wrong password
         * returns 401 Unauthorized.
         *
         * @throws Exception if the MockMvc request fails
         */
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

        /**
         * Verifies that logging in with a username that does not exist
         * in the database returns 401 Unauthorized.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void login_rejectsNonExistentUsername() throws Exception {
            String requestJson = """
                    {
                      "username": "nobody@example.com",
                      "password": "whatever"
                    }
                    """;

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isUnauthorized());
        }

        /**
         * Verifies that logging in without a username field returns 400 Bad Request,
         * as the @NotBlank constraint on LoginRequestdto.username rejects null values.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void login_returns400WhenUsernameIsNull() throws Exception {
            String requestJson = """
                    {
                      "password": "Admin123!"
                    }
                    """;

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());
        }

        /**
         * Verifies that logging in without a password field returns 400 Bad Request,
         * as the @NotBlank constraint on LoginRequestdto.password rejects null values.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void login_returns400WhenPasswordIsNull() throws Exception {
            String requestJson = """
                    {
                      "username": "admin@example.com"
                    }
                    """;

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());
        }
    }

    /**
     * Tests for JWT token validation — verifies that malformed or invalid tokens
     * are rejected with 401 Unauthorized.
     */
    @Nested
    class InvalidTokenTests {
        /**
         * Verifies that sending a malformed JWT token to an ADMIN-only endpoint
         * returns 401 Unauthorized.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void staffEndpoint_returns401ForMalformedJwtToken() throws Exception {
            mockMvc.perform(get("/api/v1/staff/all")
                            .header("Authorization", "Bearer invalid.jwt.token"))
                    .andExpect(status().isUnauthorized());
        }

        /**
         * Verifies that sending a malformed JWT token to a non-ADMIN protected endpoint
         * returns 401 Unauthorized.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void protectedEndpoint_returns401ForMalformedJwtToken() throws Exception {
            mockMvc.perform(get("/api/v1/stations")
                            .header("Authorization", "Bearer invalid.jwt.token"))
                    .andExpect(status().isUnauthorized());
        }
    }

    /**
     * Tests for POST /api/v1/staff/create — authorization scenarios
     * including unauthenticated access, admin access, and non-admin rejection.
     */
    @Nested
    class CreateStaffSecurityTests {
        /**
         * Verifies that creating a staff member without authentication
         * returns 401 Unauthorized with an error message.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void createStaff_requiresAuthentication() throws Exception {
            mockMvc.perform(post("/api/v1/staff/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createStaffRequestJson()))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").exists());
        }

        /**
         * Verifies that creating a staff member with a valid ADMIN JWT token
         * returns 201 Created with the correct fields, and that the staff member
         * is persisted in the repository.
         *
         * @throws Exception if the MockMvc request fails
         */
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

        /**
         * Verifies that creating a staff member with a STAFF_USER JWT token
         * returns 403 Forbidden with "Access denied" message.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void createStaff_forbidsNonAdminToken() throws Exception {
            saveBaristaUser("barista@example.com", "Barista123!");
            String token = loginAndGetToken("barista@example.com", "Barista123!");

            mockMvc.perform(post("/api/v1/staff/create")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createStaffRequestJson()))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value("Access denied"));
        }
    }

    /**
     * Tests for GET /api/v1/staff/all — authorization scenarios
     * including unauthenticated access and non-admin rejection.
     */
    @Nested
    class GetAllStaffSecurityTests {
        /**
         * Verifies that retrieving all staff members without authentication
         * returns 401 Unauthorized with an error message.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void getAllStaff_requiresAuthentication() throws Exception {
            mockMvc.perform(get("/api/v1/staff/all"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").exists());
        }

        /**
         * Verifies that retrieving all staff members with a STAFF_USER JWT token
         * returns 403 Forbidden with "Access denied" message.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void getAllStaff_forbidsNonAdminToken() throws Exception {
            saveBaristaUser("barista2@example.com", "Barista123!");
            String token = loginAndGetToken("barista2@example.com", "Barista123!");

            mockMvc.perform(get("/api/v1/staff/all")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value("Access denied"));
        }
    }

    /**
     * Tests for GET /api/v1/staff/{id} — authorization scenarios
     * verifying that the endpoint requires ADMIN role.
     */
    @Nested
    class GetStaffByIdSecurityTests {
        /**
         * Verifies that retrieving a staff member by ID without authentication
         * returns 401 Unauthorized with an error message.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void getStaffById_requiresAuthentication() throws Exception {
            mockMvc.perform(get("/api/v1/staff/{id}", FAULTY_ID))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").exists());
        }

        /**
         * Verifies that retrieving a staff member by ID with a STAFF_USER JWT token
         * returns 403 Forbidden with "Access denied" message.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void getStaffById_forbidsNonAdminToken() throws Exception {
            saveBaristaUser("barista3@example.com", "Barista123!");
            String token = loginAndGetToken("barista3@example.com", "Barista123!");

            mockMvc.perform(get("/api/v1/staff/{id}", FAULTY_ID)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value("Access denied"));
        }
    }

    /**
     * Tests for PUT /api/v1/staff/update/{id} — authorization scenarios
     * verifying that the endpoint requires ADMIN role.
     */
    @Nested
    class UpdateStaffSecurityTests {
        /**
         * Verifies that updating a staff member without authentication
         * returns 401 Unauthorized with an error message.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void updateStaff_requiresAuthentication() throws Exception {
            mockMvc.perform(put("/api/v1/staff/update/{id}", FAULTY_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").exists());
        }

        /**
         * Verifies that updating a staff member with a STAFF_USER JWT token
         * returns 403 Forbidden with "Access denied" message.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void updateStaff_forbidsNonAdminToken() throws Exception {
            saveBaristaUser("barista4@example.com", "Barista123!");
            String token = loginAndGetToken("barista4@example.com", "Barista123!");

            mockMvc.perform(put("/api/v1/staff/update/{id}", FAULTY_ID)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value("Access denied"));
        }
    }

    /**
     * Tests for DELETE /api/v1/staff/{id} — authorization scenarios
     * verifying that the endpoint requires ADMIN role.
     */
    @Nested
    class DeleteStaffSecurityTests {
        /**
         * Verifies that deleting a staff member without authentication
         * returns 401 Unauthorized with an error message.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void deleteStaff_requiresAuthentication() throws Exception {
            mockMvc.perform(delete("/api/v1/staff/{id}", FAULTY_ID))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").exists());
        }

        /**
         * Verifies that deleting a staff member with a STAFF_USER JWT token
         * returns 403 Forbidden with "Access denied" message.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void deleteStaff_forbidsNonAdminToken() throws Exception {
            saveBaristaUser("barista5@example.com", "Barista123!");
            String token = loginAndGetToken("barista5@example.com", "Barista123!");

            mockMvc.perform(delete("/api/v1/staff/{id}", FAULTY_ID)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value("Access denied"));
        }
    }

    /**
     * Tests for GET /api/v1/customers/** — verifies that customer endpoints
     * are publicly accessible without authentication.
     */
    @Nested
    class CustomerPublicAccessTests {
        /**
         * Verifies that retrieving all customers without authentication
         * returns 200 OK, as customer endpoints are configured as permitAll.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void getAllCustomers_isPubliclyAccessible() throws Exception {
            mockMvc.perform(get("/api/v1/customers/all"))
                    .andExpect(status().isOk());
        }

        /**
         * Verifies that creating a customer without authentication
         * returns 201 Created, as customer endpoints are configured as permitAll.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void createCustomer_isPubliclyAccessible() throws Exception {
            String requestJson = """
                    {
                      "customerFirstName": "John",
                      "customerLastName": "Doe",
                      "customerPhoneNumber": "07123456789"
                    }
                    """;

            mockMvc.perform(post("/api/v1/customers/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isCreated());
        }
    }

    /**
     * Tests for endpoints under the anyRequest().authenticated() rule —
     * verifies that stations, menu items, and purchase orders require
     * authentication even though they are not ADMIN-only.
     */
    @Nested
    class ProtectedEndpointSecurityTests {
        /**
         * Verifies that accessing stations without authentication
         * returns 401 Unauthorized, as stations fall under anyRequest().authenticated().
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void getStations_requiresAuthentication() throws Exception {
            mockMvc.perform(get("/api/v1/stations"))
                    .andExpect(status().isUnauthorized());
        }

        /**
         * Verifies that accessing menu items without authentication
         * returns 401 Unauthorized, as menu items fall under anyRequest().authenticated().
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void getMenuItems_requiresAuthentication() throws Exception {
            mockMvc.perform(get("/api/v1/menu-items"))
                    .andExpect(status().isUnauthorized());
        }

        /**
         * Verifies that accessing purchase orders without authentication
         * returns 401 Unauthorized, as orders fall under anyRequest().authenticated().
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void getOrders_requiresAuthentication() throws Exception {
            mockMvc.perform(get("/api/v1/orders"))
                    .andExpect(status().isUnauthorized());
        }
    }

    /**
     * Tests that a non-admin authenticated user (STAFF_USER role) can access
     * endpoints protected by anyRequest().authenticated() but not ADMIN-only endpoints.
     */
    @Nested
    class NonAdminAccessTests {
        /**
         * Verifies that a STAFF_USER can access the stations endpoint
         * and receives 200 OK, as stations require authentication but not ADMIN role.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void nonAdmin_canAccessStations() throws Exception {
            saveBaristaUser("barista6@example.com", "Barista123!");
            String token = loginAndGetToken("barista6@example.com", "Barista123!");

            mockMvc.perform(get("/api/v1/stations")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());
        }

        /**
         * Verifies that a STAFF_USER can access the menu items endpoint
         * and receives 200 OK, as menu items require authentication but not ADMIN role.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void nonAdmin_canAccessMenuItems() throws Exception {
            saveBaristaUser("barista7@example.com", "Barista123!");
            String token = loginAndGetToken("barista7@example.com", "Barista123!");

            mockMvc.perform(get("/api/v1/menu-items")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk());
        }

        /**
         * Verifies that a STAFF_USER cannot access the staff endpoint
         * and receives 403 Forbidden, as staff endpoints require ADMIN role.
         *
         * @throws Exception if the MockMvc request fails
         */
        @Test
        void nonAdmin_cannotAccessStaffEndpoints() throws Exception {
            saveBaristaUser("barista8@example.com", "Barista123!");
            String token = loginAndGetToken("barista8@example.com", "Barista123!");

            mockMvc.perform(get("/api/v1/staff/all")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden());
        }
    }

    /**
     * Authenticates with the given credentials and returns the JWT token from the response.
     *
     * @param username the username to log in with
     * @param password the password to log in with
     * @return the JWT token string
     * @throws Exception if the MockMvc request fails
     */
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

    /**
     * Saves a STAFF_USER with the given username and password for authorization tests.
     *
     * @param username the username for the test staff user
     * @param password the raw password (will be encoded before saving)
     */
    private void saveBaristaUser(String username, String password) {
        staffRepository.save(new Staff(
                username,
                "Barista",
                "User",
                "STAFF_USER",
                passwordEncoder.encode(password)
        ));
    }

    /** Internal record for serializing login request bodies. */
    private record LoginRequest(String username, String password) {}

    /**
     * Returns a JSON string representing a valid CreateStaffDto request body.
     *
     * @return the JSON request body
     */
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
