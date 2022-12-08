package com.kodlamaio.paymentService.business.abstracts;

import java.util.List;

import com.kodlamaio.common.dataAccess.CreateRentalPaymentRequest;
import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.paymentService.business.requests.create.CreatePaymentRequest;
import com.kodlamaio.paymentService.business.requests.update.UpdatePaymentRequest;
import com.kodlamaio.paymentService.business.responses.create.CreatePaymentResponse;
import com.kodlamaio.paymentService.business.responses.get.GetAllPaymentsResponse;
import com.kodlamaio.paymentService.business.responses.get.GetPaymentResponse;
import com.kodlamaio.paymentService.business.responses.update.UpdatePaymentResponse;

public interface PaymentService {

	DataResult<List<GetAllPaymentsResponse>> getAll();

	DataResult<GetPaymentResponse> getById(String id);

	DataResult<CreatePaymentResponse> add(CreatePaymentRequest request);

	DataResult<UpdatePaymentResponse> update(UpdatePaymentRequest request);

	Result deleteById(String id);

	Result checkIfPaymentSuccessful(CreateRentalPaymentRequest request);
}
