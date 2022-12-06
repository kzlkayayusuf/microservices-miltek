package com.kodlamaio.inventoryService.kafka.producers;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.inventories.InventoryCreatedEvent;
import com.kodlamaio.common.events.inventories.brands.BrandDeletedEvent;
import com.kodlamaio.common.events.inventories.brands.BrandUpdatedEvent;
import com.kodlamaio.common.events.inventories.cars.CarDeletedEvent;
import com.kodlamaio.common.events.inventories.cars.CarUpdatedEvent;
import com.kodlamaio.common.events.inventories.cars.rentals.CarRentalCreatedEvent;
import com.kodlamaio.common.events.inventories.cars.rentals.CarRentalDeletedEvent;
import com.kodlamaio.common.events.inventories.cars.rentals.CarRentalUpdatedEvent;
import com.kodlamaio.common.events.inventories.models.ModelDeletedEvent;
import com.kodlamaio.common.events.inventories.models.ModelUpdatedEvent;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryProducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryProducer.class);

	private final NewTopic topic;

	private final KafkaTemplate<String, InventoryCreatedEvent> kafkaTemplate;

	public void sendMessage(InventoryCreatedEvent event) {
		LOGGER.info(String.format("Inventory created event => %s", event.toString()));

		Message<InventoryCreatedEvent> message = MessageBuilder.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC, topic.name()).build();

		kafkaTemplate.send(message);
	}

	public void sendMessage(CarUpdatedEvent event) {
		LOGGER.info(String.format("Car updated event => %s", event.toString()));

		Message<CarUpdatedEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, topic.name())
				.build();

		kafkaTemplate.send(message);
	}

	public void sendMessage(CarDeletedEvent event) {
		LOGGER.info(String.format("Car deleted event => %s", event.toString()));

		Message<CarDeletedEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, topic.name())
				.build();

		kafkaTemplate.send(message);
	}

	public void sendMessage(BrandUpdatedEvent event) {
		LOGGER.info(String.format("Brand updated event => %s", event.toString()));

		Message<BrandUpdatedEvent> message = MessageBuilder.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC, topic.name()).build();

		kafkaTemplate.send(message);
	}

	public void sendMessage(BrandDeletedEvent event) {
		LOGGER.info(String.format("Brand deleted event => %s", event.toString()));

		Message<BrandDeletedEvent> message = MessageBuilder.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC, topic.name()).build();

		kafkaTemplate.send(message);
	}

	public void sendMessage(ModelUpdatedEvent event) {
		LOGGER.info(String.format("Model updated event => %s", event.toString()));

		Message<ModelUpdatedEvent> message = MessageBuilder.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC, topic.name()).build();

		kafkaTemplate.send(message);
	}

	public void sendMessage(ModelDeletedEvent event) {
		LOGGER.info(String.format("Model deleted event => %s", event.toString()));

		Message<ModelDeletedEvent> message = MessageBuilder.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC, topic.name()).build();

		kafkaTemplate.send(message);
	}

	public void sendMessage(CarRentalCreatedEvent event) {
		LOGGER.info(String.format("Car Rental created event => %s", event.toString()));

		Message<CarRentalCreatedEvent> message = MessageBuilder.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC, topic.name()).build();

		kafkaTemplate.send(message);
	}

	public void sendMessage(CarRentalUpdatedEvent event) {
		LOGGER.info(String.format("Car rental updated event => %s", event.toString()));

		Message<CarRentalUpdatedEvent> message = MessageBuilder.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC, topic.name()).build();

		kafkaTemplate.send(message);
	}

	public void sendMessage(CarRentalDeletedEvent event) {
		LOGGER.info(String.format("Car rental deleted event => %s", event.toString()));

		Message<CarRentalDeletedEvent> message = MessageBuilder.withPayload(event)
				.setHeader(KafkaHeaders.TOPIC, topic.name()).build();

		kafkaTemplate.send(message);
	}
}
