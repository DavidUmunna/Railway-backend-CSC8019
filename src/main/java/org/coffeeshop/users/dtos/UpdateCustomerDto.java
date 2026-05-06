package org.coffeeshop.users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Kulagina Tatiana
 * @version 1.0
 * @since 2026-04-27
 */
public record UpdateCustomerDto(
    @NotNull @NotBlank String customerFirstName,
    @NotNull @NotBlank String customerLastName,
    @NotNull @NotBlank String customerPhoneNumber
) {}
