package com.kodlamaio.paymentService.business.abstracts;

import java.util.List;

import com.kodlamaio.paymentService.business.requests.CreatePaymentRequest;
import com.kodlamaio.paymentService.business.responses.CreatePaymentResponse;
import com.kodlamaio.paymentService.business.responses.GetAllPaymentsResponse;

public interface PaymentService {
	List<GetAllPaymentsResponse> getAll();

	CreatePaymentResponse add(CreatePaymentRequest request);

	void checkBalanceEnough(double balance, double totalPrice);
}
