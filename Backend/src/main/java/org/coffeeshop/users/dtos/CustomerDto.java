package org.coffeeshop.users.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerDto(
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("customer_firstname") String customerFirstName,
        @JsonProperty("customer_lastname") String customerLastName,
        @JsonProperty("customer_phone_number") String customerPhoneNumber
) {

    @Override
    public String toString() {
        return "customer firstname : " + customerFirstName + "\n"
                + "customer lastname : " + customerLastName + "\n"
                + "phone number : " + customerPhoneNumber + "\n";
    }
}
