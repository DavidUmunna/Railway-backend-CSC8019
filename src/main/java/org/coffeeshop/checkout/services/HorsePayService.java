package org.coffeeshop.checkout.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.coffeeshop.checkout.dtos.HorsePayRequestDto;
import org.coffeeshop.checkout.dtos.HorsePayResponseDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HorsePayService {
	private static final String HORSEPAY_URL = "http://homepages.cs.ncl.ac.uk/daniel.nesbitt/CSC8019/HorsePay/HorsePay.php";
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	public HorsePayService() {
		this.restTemplate = new RestTemplate();
		this.objectMapper = new ObjectMapper();
	}

	public HorsePayResponseDto processPayment(HorsePayRequestDto request) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<HorsePayRequestDto> entity = new HttpEntity<>(request, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(HORSEPAY_URL, entity, String.class);
			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				return objectMapper.readValue(response.getBody(), HorsePayResponseDto.class);
			} else {
				throw new RuntimeException("HorsePay API error: " + response.getStatusCode());
			}
		} catch (Exception e) {
			throw new RuntimeException("HorsePay payment failed: " + e.getMessage(), e);
		}
	}
}
