package org.coffeeshop.users.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * this is adata transfer object class for the staff entity
 * its main function is to ensure that only the necessary data is sent to the client and to provide a clear structure for the data being transferred
 * it is made up of Attributes in the staff entity 
 * 
 * @author Umunna David
 * @version 1.0 
 * @since 2026-04-12
 */
public record StaffDto(
        @JsonProperty("id") Long id,
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
