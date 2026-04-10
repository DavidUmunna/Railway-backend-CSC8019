package org.coffeeshop.users.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "staff")
public  class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long staffId;

    // used for login
    @Column(nullable = false, unique = true)
    @NotNull
    private  String username;

    // basic identity
    @Column(name = "staff_firstname", nullable = false)
    @NotNull
    private String staffFirstName;

    @Column(name = "staff_lastname", nullable = false)
    @NotNull
    private String staffLastName;


    // e.g. "BARISTA", "MANAGER", "ADMIN"
    @Column(name = "staff_role", nullable = false)
    private String staffRole;

    // hashed password (never expose this in DTOs)
    @Column(name = "password_hash", nullable = false)
    @NotNull
    private String passwordHash;


    protected Staff() {

    }

    public Staff(String username, String staffFirstName, String staffLastName, String staffRole, String passwordHash) {
        this(null, username, staffFirstName, staffLastName, staffRole, passwordHash);
    }

    public Staff(Long staffId, String username, String staffFirstName, String staffLastName, String staffRole, String passwordHash) {
        this.staffId = staffId;
        this.username = username;
        this.staffFirstName = staffFirstName;
        this.staffLastName = staffLastName;
        this.staffRole = staffRole;
        this.passwordHash = passwordHash;
    }




    // === getters/setters ===

    public Long getStaffId() {
        return staffId;
    }

    public String getUsername() {
        return username;
    }



    public String getFirstName() {
        return staffFirstName;
    }



    public String getLastName() {
        return staffLastName;
    }





    public String getRole() {
        return staffRole;
    }


    public String getPasswordHash() {
        return passwordHash;
    }


    @Override
    public String toString() {
        return "staff id:"+getStaffId()+"\n"+
                "staff firstname:"+getFirstName()+"\n"
                + "staff lastname:"+getLastName()+"\n"
                + "staff role:"+getRole()+"\n";
    }
}
