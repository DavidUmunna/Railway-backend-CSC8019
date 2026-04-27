package org.coffeeshop.users.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * this is a data transfer object class for creating a new staff member
 * its main function is to ensure that only the necessary data is sent to the client and to provide a clear structure for the data being transferred
 * it is made up of Attributes in the staff entity 
 * 
 * @author Umunna David
 * @version 1.0 
 * @since 2026-04-12
 */
public record CreateStaffDto(
        @NotBlank
        @Size(min = 3, max = 50)
        String username,
        @NotBlank
        @Size(min = 1, max = 50)
        String firstName,
        @NotBlank
        @Size(min = 1, max = 50)
        String lastName,
        @NotBlank
        String role,
        @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank
        String password
) {

}
