package org.coffeeshop.checkout.controllers;

import org.coffeeshop.checkout.dtos.HorsePayRequestDto;
import org.coffeeshop.checkout.dtos.HorsePayResponseDto;
import org.coffeeshop.checkout.services.HorsePayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

	private final HorsePayService horsePayService;

	@Autowired
	public CheckoutController(HorsePayService horsePayService) {
		this.horsePayService = horsePayService;
	}

	@PostMapping("/pay")
	public ResponseEntity<HorsePayResponseDto> pay(@RequestBody HorsePayRequestDto requestDto) {
		HorsePayResponseDto response = horsePayService.processPayment(requestDto);
		return ResponseEntity.ok(response);
	}
}
