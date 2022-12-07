package com.kodlamaio.paymentService.business.concretes;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.common.utilities.results.SuccessDataResult;
import com.kodlamaio.common.utilities.results.SuccessResult;
import com.kodlamaio.paymentService.business.abstracts.PaymentService;
import com.kodlamaio.paymentService.business.abstracts.PosService;
import com.kodlamaio.paymentService.business.constants.Messages;
import com.kodlamaio.paymentService.business.requests.create.CreatePaymentRequest;
import com.kodlamaio.paymentService.business.requests.create.CreateRentalPaymentRequest;
import com.kodlamaio.paymentService.business.requests.update.UpdatePaymentRequest;
import com.kodlamaio.paymentService.business.responses.create.CreatePaymentResponse;
import com.kodlamaio.paymentService.business.responses.get.GetAllPaymentsResponse;
import com.kodlamaio.paymentService.business.responses.get.GetPaymentResponse;
import com.kodlamaio.paymentService.business.responses.update.UpdatePaymentResponse;
import com.kodlamaio.paymentService.dataAccess.PaymentRepository;
import com.kodlamaio.paymentService.entities.Payment;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentManager implements PaymentService {
	private PaymentRepository paymentRepository;
	private ModelMapperService modelMapperService;
	private final PosService posService;

	@Override
	public DataResult<List<GetAllPaymentsResponse>> getAll() {
		List<Payment> payments = paymentRepository.findAll();
		List<GetAllPaymentsResponse> response = payments.stream()
				.map(payment -> modelMapperService.forResponse().map(payment, GetAllPaymentsResponse.class)).toList();

		return new SuccessDataResult<List<GetAllPaymentsResponse>>(response, Messages.PaymentListed);
	}

	@Override
	public DataResult<GetPaymentResponse> getById(String id) {
		checkIfPaymentExists(id);
		Payment payment = paymentRepository.findById(id).orElseThrow();
		GetPaymentResponse response = modelMapperService.forResponse().map(payment, GetPaymentResponse.class);

		return new SuccessDataResult<GetPaymentResponse>(response);
	}

	@Override
	public DataResult<CreatePaymentResponse> add(CreatePaymentRequest request) {
		checkIfCardNumberExists(request.getCardNumber());
		Payment payment = modelMapperService.forRequest().map(request, Payment.class);
		payment.setId(UUID.randomUUID().toString());
		paymentRepository.save(payment);
		CreatePaymentResponse response = modelMapperService.forResponse().map(payment, CreatePaymentResponse.class);

		return new SuccessDataResult<CreatePaymentResponse>(response);
	}

	@Override
	public DataResult<UpdatePaymentResponse> update(UpdatePaymentRequest request) {
		checkIfPaymentExists(request.getId());
		Payment payment = modelMapperService.forRequest().map(request, Payment.class);
		paymentRepository.save(payment);
		UpdatePaymentResponse response = modelMapperService.forResponse().map(payment, UpdatePaymentResponse.class);

		return new SuccessDataResult<UpdatePaymentResponse>(response);
	}

	@Override
	public Result deleteById(String id) {
		checkIfPaymentExists(id);
		paymentRepository.deleteById(id);
		return new SuccessResult(Messages.PaymentDeleted);
	}

	@Override
	public Result checkIfPaymentSuccessful(CreateRentalPaymentRequest request) {
		checkPayment(request);
		return new SuccessResult(Messages.PaymentSuccessful);
	}

	private void checkPayment(CreateRentalPaymentRequest request) {
		if (!paymentRepository.existsByCardNumberAndFullNameAndCardExpirationYearAndCardExpirationMonthAndCardCvv(
				request.getCardNumber(), request.getFullName(), request.getCardExpirationYear(),
				request.getCardExpirationMonth(), request.getCardCvv())) {
			throw new BusinessException("NOT_A_VALID_PAYMENT!");
		} else {
			Payment payment = paymentRepository.findByCardNumber(request.getCardNumber());
			double balance = payment.getBalance();
			if (balance < request.getPrice()) {
				throw new BusinessException("NOT_ENOUGH_MONEY!");
			} else {
				posService.pay(); // Fake payment
				payment.setBalance(balance - request.getPrice());
				paymentRepository.save(payment);
			}
		}
	}

	private void checkIfPaymentExists(String id) {
		if (!paymentRepository.existsById(id)) {
			throw new BusinessException("PAYMENT_NOT_FOUND!");
		}
	}

	private void checkIfCardNumberExists(String cardNumber) {
		if (paymentRepository.existsByCardNumber(cardNumber)) {
			throw new BusinessException("CARD_NUMBER_ALREADY_EXISTS!");
		}
	}

}