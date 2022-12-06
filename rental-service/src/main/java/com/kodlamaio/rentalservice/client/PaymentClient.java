package com.kodlamaio.rentalservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kodlamaio.common.utilities.results.Result;

import feign.Headers;

@FeignClient(value = "paymentclient", url = "http://localhost:7017")
public interface PaymentClient {
	@RequestMapping(method = RequestMethod.GET, value = "/payment/api/payments/check")
	@Headers(value = "Content-Type: application/json")
	Result checkIfPaymentSuccessful(@RequestParam String cardNumber, @RequestParam String fullName,
			@RequestParam int cardExpirationYear, @RequestParam int cardExpirationMonth, @RequestParam String cardCvv,
			@RequestParam double price);
}
