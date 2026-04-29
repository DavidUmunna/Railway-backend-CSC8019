package org.coffeeshop.checkout.dtos;

public record HorsePayResponseDto(
	String storeID,
	String customerID,
	String date,
	String time,
	String timeZone,
	double transactionAmount,
	String currencyCode,
    @com.fasterxml.jackson.annotation.JsonProperty("paymetSuccess")
	PaymentSuccess paymentSuccess
) {
	public record PaymentSuccess(Boolean Status, String reason) {}
}
