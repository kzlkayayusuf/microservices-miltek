package com.kodlamaio.inventoryService.kafka.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.inventories.cars.rentals.CarRentalCreatedEvent;
import com.kodlamaio.common.events.inventories.cars.rentals.CarRentalDeletedEvent;
import com.kodlamaio.common.events.inventories.cars.rentals.CarRentalUpdatedEvent;
import com.kodlamaio.common.events.rentals.RentalCreatedEvent;
import com.kodlamaio.common.events.rentals.RentalDeletedEvent;
import com.kodlamaio.common.events.rentals.RentalUpdatedEvent;
import com.kodlamaio.inventoryService.business.abstracts.CarService;
import com.kodlamaio.inventoryService.kafka.producers.InventoryProducer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RentalConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(RentalConsumer.class);
	private final CarService carService;
	private final InventoryProducer producer;

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "created_rental")
	public void consume(RentalCreatedEvent event) {
		LOGGER.info(String.format("Order event received in stock service => %s", event.toString()));
		carService.updateCarState(event.getCarId(), 3); //1-available 2-under maintenance 3-rented
		LOGGER.info(event.getCarId() + " state changed");
		CarRentalCreatedEvent carRentalCreatedEvent = new CarRentalCreatedEvent();
		carRentalCreatedEvent.setCarId(event.getCarId());
		carRentalCreatedEvent.setMessage("Car rented!");
		producer.sendMessage(carRentalCreatedEvent);
		LOGGER.info("Car rented!");
	}

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "updated_rental")
	public void consume(RentalUpdatedEvent event) {
		LOGGER.info(String.format("Order event received in stock service => %s", event.toString()));
		carService.updateCarState(event.getOldCarId(), 1);
		carService.updateCarState(event.getNewCarId(), 3);
		LOGGER.info(event.getOldCarId() + " changed to " + event.getNewCarId());
		CarRentalUpdatedEvent carRentalUpdatedEvent = new CarRentalUpdatedEvent();
		carRentalUpdatedEvent.setNewCarId(event.getNewCarId());
		carRentalUpdatedEvent.setOldCarId(event.getOldCarId());
		carRentalUpdatedEvent.setMessage("Car rented state updated!");
		producer.sendMessage(carRentalUpdatedEvent);
		LOGGER.info("Car rented state updated!");
	}

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "rental-delete")
	public void consume(RentalDeletedEvent event) {
		carService.updateCarState(event.getCarId(), 1);
		CarRentalDeletedEvent carRentalDeletedEvent = new CarRentalDeletedEvent();
		carRentalDeletedEvent.setCarId(event.getCarId());
		carRentalDeletedEvent.setMessage("Car deleted from rental!");
		producer.sendMessage(carRentalDeletedEvent);
		LOGGER.info("Car deleted from rental!");
	}
}
