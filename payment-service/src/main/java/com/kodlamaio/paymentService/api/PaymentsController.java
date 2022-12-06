package com.kodlamaio.paymentService.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.paymentService.business.abstracts.PaymentService;
import com.kodlamaio.paymentService.business.requests.create.CreatePaymentRequest;
import com.kodlamaio.paymentService.business.requests.get.GetPaymentRequest;
import com.kodlamaio.paymentService.business.requests.update.UpdatePaymentRequest;
import com.kodlamaio.paymentService.business.responses.create.CreatePaymentResponse;
import com.kodlamaio.paymentService.business.responses.get.GetAllPaymentsResponse;
import com.kodlamaio.paymentService.business.responses.get.GetPaymentResponse;
import com.kodlamaio.paymentService.business.responses.update.UpdatePaymentResponse;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@AllArgsConstructor
public class PaymentsController {

	private final PaymentService paymentService;

	@GetMapping
	public List<GetAllPaymentsResponse> getAll() {
		return paymentService.getAll();
	}

	@GetMapping("/{id}")
	public GetPaymentResponse getById(@PathVariable String id) {
		return paymentService.getById(id);
	}

	@PostMapping
	public CreatePaymentResponse add(@Valid @RequestBody CreatePaymentRequest request) {
		return paymentService.add(request);
	}

	@PutMapping("/{id}")
	public UpdatePaymentResponse update(@Valid @RequestBody UpdatePaymentRequest request, @PathVariable String id) {
		return paymentService.update(request, id);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		paymentService.delete(id);
	}

	@PostMapping("/check")
	public void checkIfPaymentSuccessful(@RequestParam String cardNumber, @RequestParam String fullName,
			@RequestParam int cardExpirationYear, @RequestParam int cardExpirationMonth, @RequestParam String cardCvv,
			@RequestParam double price) {
		GetPaymentRequest request = new GetPaymentRequest(cardNumber, fullName, cardExpirationYear, cardExpirationMonth,
				cardCvv, price);
		paymentService.checkIfPaymentSuccessful(request);
	}
}