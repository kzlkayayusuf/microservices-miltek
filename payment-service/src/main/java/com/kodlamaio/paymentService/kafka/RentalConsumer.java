package com.kodlamaio.paymentService.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.PaymentCreatedEvent;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.paymentService.business.abstracts.PaymentService;
import com.kodlamaio.paymentService.business.requests.CreatePaymentRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RentalConsumer {
	private final PaymentService paymentService;
	private final ModelMapperService mapperService;

	private static final Logger LOGGER = LoggerFactory.getLogger(RentalConsumer.class);

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "PaymentCreate")
	public void consume(PaymentCreatedEvent event) {
		LOGGER.info(String.format("Order event received in stock service => %s", event.toString()));
		CreatePaymentRequest request = mapperService.forRequest().map(event, CreatePaymentRequest.class);
		paymentService.add(request);
	}
}
