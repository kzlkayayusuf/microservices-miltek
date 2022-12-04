package com.kodlamaio.paymentService.business.concretes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.InvoiceCreatedEvent;
import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.paymentService.business.abstracts.PaymentService;
import com.kodlamaio.paymentService.business.requests.CreatePaymentRequest;
import com.kodlamaio.paymentService.business.responses.CreatePaymentResponse;
import com.kodlamaio.paymentService.business.responses.GetAllPaymentsResponse;
import com.kodlamaio.paymentService.dataAccess.PaymentRepository;
import com.kodlamaio.paymentService.entities.Payment;
import com.kodlamaio.paymentService.kafka.PaymentCreatedProducer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentManager implements PaymentService {
	private PaymentRepository paymentRepository;
	private ModelMapperService modelMapperService;
	private PaymentCreatedProducer paymentCreatedProducer;

	@Override
	public List<GetAllPaymentsResponse> getAll() {
		return paymentRepository.findAll().stream()
				.map(payment -> modelMapperService.forResponse().map(payment, GetAllPaymentsResponse.class))
				.collect(Collectors.toList());
	}

	@Override
	public CreatePaymentResponse add(CreatePaymentRequest createPaymentRequest) {

		Payment payment = modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
		payment.setId(UUID.randomUUID().toString());
		payment.setStatus(1);// onay

		Payment createdPayment = paymentRepository.save(payment);

		InvoiceCreatedEvent event = new InvoiceCreatedEvent();
		event.setRentalId(createPaymentRequest.getRentalId());
		event.setCardHolder(createPaymentRequest.getCardHolder());
		event.setTotalPrice(createPaymentRequest.getTotalPrice());

		paymentCreatedProducer.sendMessage(event);

		CreatePaymentResponse createPaymentResponse = modelMapperService.forResponse().map(payment,
				CreatePaymentResponse.class);

		return createPaymentResponse;
	}

	@Override
	public void checkBalanceEnough(double balance, double totalPrice) {
		if (balance < totalPrice)
			throw new BusinessException("Insufficient Balance");
	}

}