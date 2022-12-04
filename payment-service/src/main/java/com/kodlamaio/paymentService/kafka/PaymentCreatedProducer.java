package com.kodlamaio.paymentService.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.InvoiceCreatedEvent;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentCreatedProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentCreatedProducer.class);

	private NewTopic topic;

	private final KafkaTemplate<String, InvoiceCreatedEvent> kafkaTemplate;

	public void sendMessage(InvoiceCreatedEvent invoiceCreatedEvent) {
		LOGGER.info(String.format("Rental created event => %s", invoiceCreatedEvent.toString()));

		Message<InvoiceCreatedEvent> message = MessageBuilder.withPayload(invoiceCreatedEvent)
				.setHeader(KafkaHeaders.TOPIC, topic.name()).build();

		kafkaTemplate.send(message);
	}
}
