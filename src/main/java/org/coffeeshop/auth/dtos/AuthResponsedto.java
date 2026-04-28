package org.coffeeshop.auth.dtos;

/**
 * Response payload returned after successful authentication.
 * @Author willian
 * @version 1.0
 * @modifiedBy Umunna David
 * changed from class to record to adhere to consistency and immutability best practices in DTO design
 */
public record AuthResponsedto(
	String token,
	String tokenType,
	String username,
	Long id,
	String role
) {
	public AuthResponsedto(String token, String username, Long id, String role) {
		this(token, "Bearer", username, id, role);
	}
}
