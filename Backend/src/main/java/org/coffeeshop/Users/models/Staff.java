package org.coffeeshop.Users.models;

import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // used for login
    @Column(nullable = false, unique = true)
    private String username;

    // basic identity
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;



    // e.g. "BARISTA", "MANAGER", "ADMIN"
    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private boolean active = true;

    // hashed password (never expose this in DTOs)
    @Column(nullable = false)
    private String passwordHash;

    // === getters/setters ===

    public Long getStaffId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
