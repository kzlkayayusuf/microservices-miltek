package com.kodlamaio.inventoryService.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.RentalUpdatedEvent;
import com.kodlamaio.inventoryService.business.abstracts.CarService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RentalUpdatedConsumer {

	private CarService carService;

	private static final Logger LOGGER = LoggerFactory.getLogger(RentalUpdatedConsumer.class);

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "updated_rental")

	public void consume(RentalUpdatedEvent event) {
		LOGGER.info(String.format("Order event received in stock service => %s", event.toString()));
		carService.updateCarState(event.getOldCarId(), 1);
		carService.updateCarState(event.getNewCarId(), 3);
		LOGGER.info(event.getOldCarId() + " changed to " + event.getNewCarId());

	}
}
