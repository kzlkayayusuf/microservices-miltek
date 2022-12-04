package com.kodlamaio.rentalservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import feign.Headers;

@FeignClient(value = "paymentclient", url = "http://localhost:7017")
public interface PaymentClient {
	@RequestMapping(method = RequestMethod.GET, value = "/payment/api/payments/checkBalanceEnough/{balance}/{totalPric")
	@Headers(value = "Content-Type: application/json")
	void checkBalanceEnough(@PathVariable double balance, @PathVariable double totalPrice);
}
