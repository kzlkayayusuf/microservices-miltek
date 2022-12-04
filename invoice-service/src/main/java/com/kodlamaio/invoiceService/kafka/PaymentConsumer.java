package com.kodlamaio.invoiceService.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.InvoiceCreatedEvent;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.invoiceService.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceService.business.requests.CreateInvoiceRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentConsumer {

	private final InvoiceService invoiceService;
	private final ModelMapperService mapperService;

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentConsumer.class);

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "PaymentInvoiceCreate")
	public void consume(InvoiceCreatedEvent event) {
		LOGGER.info(String.format("Order event received in stock service => %s", event.toString()));
		CreateInvoiceRequest request = mapperService.forRequest().map(event, CreateInvoiceRequest.class);
		invoiceService.add(request);
	}
}
