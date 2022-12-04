package com.kodlamaio.paymentService.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.paymentService.business.abstracts.PaymentService;
import com.kodlamaio.paymentService.business.requests.CreatePaymentRequest;
import com.kodlamaio.paymentService.business.responses.CreatePaymentResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@AllArgsConstructor
public class PaymentsController {
	private PaymentService paymentService;

	@PostMapping
	public CreatePaymentResponse add(@Valid @RequestBody CreatePaymentRequest createPaymentRequest) {
		return paymentService.add(createPaymentRequest);
	}

	@GetMapping("checkBalanceEnough/{balance}/{totalPrice}")
	public void checkBalanceEnough(@PathVariable double balance, @PathVariable double totalPrice) {
		paymentService.checkBalanceEnough(balance, totalPrice);
	}
}