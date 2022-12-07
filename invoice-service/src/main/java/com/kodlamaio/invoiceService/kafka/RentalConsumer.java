package com.kodlamaio.invoiceService.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.payments.PaymentReceivedEvent;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.invoiceService.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceService.entities.Invoice;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RentalConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(RentalConsumer.class);
	private final InvoiceService service;
	private final ModelMapperService mapper;
	// private final CarClient client;

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "payment-received")
	public void consume(PaymentReceivedEvent event) {
		Invoice invoice = mapper.forRequest().map(event, Invoice.class);
		service.createInvoice(invoice);
		LOGGER.info("Invoice created for : {}", event.getFullName());
	}
}
