package com.kodlamaio.paymentService.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.paymentService.business.abstracts.PaymentService;
import com.kodlamaio.paymentService.business.requests.create.CreatePaymentRequest;
import com.kodlamaio.paymentService.business.requests.create.CreateRentalPaymentRequest;
import com.kodlamaio.paymentService.business.requests.update.UpdatePaymentRequest;
import com.kodlamaio.paymentService.business.responses.create.CreatePaymentResponse;
import com.kodlamaio.paymentService.business.responses.get.GetAllPaymentsResponse;
import com.kodlamaio.paymentService.business.responses.update.UpdatePaymentResponse;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@AllArgsConstructor
public class PaymentsController {

	private final PaymentService paymentService;

	@GetMapping
	public ResponseEntity<?> getAll() {
		DataResult<List<GetAllPaymentsResponse>> result = paymentService.getAll();
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);

		}
		return ResponseEntity.badRequest().body(result);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) {
		Result result = paymentService.getById(id);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@PostMapping
	public ResponseEntity<?> add(@Valid @RequestBody CreatePaymentRequest request) {
		DataResult<CreatePaymentResponse> result = paymentService.add(request);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@PutMapping
	public ResponseEntity<?> update(@Valid @RequestBody UpdatePaymentRequest request) {
		DataResult<UpdatePaymentResponse> result = paymentService.update(request);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable String id) {
		Result result = paymentService.deleteById(id);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@PostMapping("/check")
	public ResponseEntity<?> checkIfPaymentSuccessful(@RequestParam String cardNumber, @RequestParam String fullName,
			@RequestParam int cardExpirationYear, @RequestParam int cardExpirationMonth, @RequestParam String cardCvv,
			@RequestParam double price) {

		CreateRentalPaymentRequest request = new CreateRentalPaymentRequest(cardNumber, fullName, cardExpirationYear,
				cardExpirationMonth, cardCvv, price);

		Result result = paymentService.checkIfPaymentSuccessful(request);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}
}