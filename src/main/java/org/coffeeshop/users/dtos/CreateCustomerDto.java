package org.coffeeshop.users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCustomerDto(
    @NotNull @NotBlank @Size(min = 1, max = 50) String customerFirstName,
    @NotNull @NotBlank @Size(min = 1, max = 50) String customerLastName,
    @NotNull @NotBlank @Size(min = 10, max = 15) String customerPhoneNumber
) {}
