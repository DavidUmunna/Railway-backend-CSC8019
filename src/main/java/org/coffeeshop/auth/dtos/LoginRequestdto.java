package org.coffeeshop.auth.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for username/password login.
 * @Author willian
 * @version 1.0
 * @modifiedBy Umunna David
 * changed from class to record to adhere to consistency and immutability best practices in DTO design
 */
public record LoginRequestdto(
	@NotBlank String username,
	@NotBlank String password
) {}
