package org.coffeeshop.checkout.dtos;

public record HorsePayRequestDto(
	String storeID,
	String customerID,
	String date,
	String time,
	String timeZone,
	double transactionAmount,
	String currencyCode,
	Boolean forcePaymentSatusReturnType // Optional
) {}
