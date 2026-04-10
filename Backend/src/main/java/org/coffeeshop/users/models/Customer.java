package org.coffeeshop.users.models;

import jakarta.persistence.*;

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


    @Column(name = "customer_phone_number")
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
