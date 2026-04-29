package org.coffeeshop.users.models;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

/**
 * this class is a representation of the customer entity in the database
 * it is made up of  the atributes inside the customer entity and it is annotated with @Entity to indicate that it is a JPA entity
 * the @Table annotation is used to specify the name of the table in the database that this
 * 
 * @author Umunna David
 * @version 1.0
 * @since 2026-04-12
 */
@Entity
@Table(name = "customer")
public  class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_firstname", nullable = false)
    private String customerFirstName;

    @Column(name = "customer_lastname", nullable = false)
    private String customerLastName;

    @Pattern(regexp = "^[0-9+]{10,15}$")
    @Column(name = "customer_phone_number",unique = true, nullable = false)
    private String customerPhoneNumber;



    protected Customer() {

    }

    public Customer(String customerFirstName, String customerLastName, String customerPhoneNumber) {
        this(null, customerFirstName, customerLastName, customerPhoneNumber);
    }

    public Customer(Long customerId, String customerFirstName, String customerLastName, String customerPhoneNumber) {
        this.customerId = customerId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerPhoneNumber = customerPhoneNumber;
    }


    public Long getId() { return customerId;}
    public String getCustomerFirstName() { return customerFirstName;}
    public String getCustomerLastName() { return customerLastName;}
    public String getCustomerPhoneNumber() { return customerPhoneNumber;}
    public  String toString() {
        return "customer id: " + this.getId()+"\n"+
                "customer firstname:"+this.getCustomerFirstName()+"\n"
                + "customer lastname:"+this.getCustomerLastName()+"\n"
                + "customer phone number:"+this.getCustomerPhoneNumber();
    }

}
