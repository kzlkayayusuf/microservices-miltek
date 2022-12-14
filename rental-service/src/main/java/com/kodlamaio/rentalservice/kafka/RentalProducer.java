package com.kodlamaio.rentalservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.payments.PaymentReceivedEvent;
import com.kodlamaio.common.events.rentals.RentalCreatedEvent;
import com.kodlamaio.common.events.rentals.RentalDeletedEvent;
import com.kodlamaio.common.events.rentals.RentalUpdatedEvent;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RentalProducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(RentalProducer.class);

	private final NewTopic topic;

	private final KafkaTemplate<String, RentalCreatedEvent> kafkaTemplate;

	public void sendMessage(RentalCreatedEvent rentalCreatedEvent) {
		LOGGER.info(String.format("Rental created event => %s", rentalCreatedEvent.toString()));

		Message<RentalCreatedEvent> message = MessageBuilder.withPayload(rentalCreatedEvent)
				.setHeader(KafkaHeaders.TOPIC, "rental-created").build();

		kafkaTemplate.send(message);
	}

	public void sendMessage(RentalUpdatedEvent rentalUpdatedEvent) {
		LOGGER.info(String.format("Rental updated event => %s", rentalUpdatedEvent.toString()));

		Message<RentalUpdatedEvent> message = MessageBuilder.withPayload(rentalUpdatedEvent)
				.setHeader(KafkaHeaders.TOPIC, "rental-updated").build();

		kafkaTemplate.send(message);
	}

	public void sendMessage(RentalDeletedEvent rentalDeleteEvent) {
		LOGGER.info(String.format("Rental deleted event => %s", rentalDeleteEvent.toString()));

		Message<RentalDeletedEvent> message = MessageBuilder.withPayload(rentalDeleteEvent)
				.setHeader(KafkaHeaders.TOPIC, "rental-deleted").build();

		kafkaTemplate.send(message);
	}

	public void sendMessage(PaymentReceivedEvent event) {
		LOGGER.info(String.format("Payment received event => %s", event.toString()));

		Message<PaymentReceivedEvent> message = MessageBuilder.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC, "payment-received").build();

		kafkaTemplate.send(message);
	}
}
