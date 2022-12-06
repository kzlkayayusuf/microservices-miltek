package com.kodlamaio.paymentService.business.abstracts;

import java.util.List;

import com.kodlamaio.paymentService.business.requests.create.CreatePaymentRequest;
import com.kodlamaio.paymentService.business.requests.get.GetPaymentRequest;
import com.kodlamaio.paymentService.business.requests.update.UpdatePaymentRequest;
import com.kodlamaio.paymentService.business.responses.create.CreatePaymentResponse;
import com.kodlamaio.paymentService.business.responses.get.GetAllPaymentsResponse;
import com.kodlamaio.paymentService.business.responses.get.GetPaymentResponse;
import com.kodlamaio.paymentService.business.responses.update.UpdatePaymentResponse;

public interface PaymentService {
	List<GetAllPaymentsResponse> getAll();

	GetPaymentResponse getById(String id);

	CreatePaymentResponse add(CreatePaymentRequest request);

	UpdatePaymentResponse update(UpdatePaymentRequest request, String id);

	void delete(String id);

	void checkIfPaymentSuccessful(GetPaymentRequest request);
}
