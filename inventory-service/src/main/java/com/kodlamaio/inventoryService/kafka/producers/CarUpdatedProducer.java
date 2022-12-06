package com.kodlamaio.inventoryService.kafka.producers;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.cars.CarUpdatedEvent;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CarUpdatedProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(CarUpdatedProducer.class);

	private NewTopic topic;

	private KafkaTemplate<String, CarUpdatedEvent> kafkaTemplate;

	public void sendMessage(CarUpdatedEvent carUpdatedEvent) {
		LOGGER.info(String.format("Car updated event => %s", carUpdatedEvent.toString()));
		Message<CarUpdatedEvent> message = MessageBuilder.withPayload(carUpdatedEvent)
				.setHeader(KafkaHeaders.TOPIC, topic.name()).build();
		kafkaTemplate.send(message);
	}
}
