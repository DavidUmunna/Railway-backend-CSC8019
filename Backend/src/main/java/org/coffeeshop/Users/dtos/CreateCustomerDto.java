package org.coffeeshop.users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCustomerDto(

        @NotBlank
        @Size(min = 2, max = 50)
        String customerFirstName,
        @NotBlank
        @Size(min = 2, max = 50)
        String customerLastName,
        @NotBlank
        @Size(min = 10, max = 13)
        String customerPhoneNumber
) {}
