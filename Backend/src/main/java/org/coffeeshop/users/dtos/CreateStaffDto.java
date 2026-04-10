package org.coffeeshop.users.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateStaffDto(
        @NotBlank
        @Size(min = 3, max = 50)
        @JsonProperty("username") String username,
        @NotBlank
        @Size(min = 1, max = 50)
        @JsonProperty("firstName") String firstName,
        @NotBlank
        @Size(min = 1, max = 50)
        @JsonProperty("lastName") String lastName,
        @NotBlank
        @JsonProperty("role") String role,
        @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank
        @Size(min = 8, max = 100) String password
) {

    @Override
    public String toString() {
        return "staff firstname : " + firstName + "\n"
                + "staff lastname : " + lastName + "\n"
                + "staff username : " + username + "\n"
                + "staff role : " + role;
    }
}
