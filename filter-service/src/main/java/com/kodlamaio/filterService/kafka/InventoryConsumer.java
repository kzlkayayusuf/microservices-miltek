package com.kodlamaio.filterService.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
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
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.filterService.business.abstracts.CarFilterService;
import com.kodlamaio.filterService.entities.CarFilter;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryConsumer.class);
	private final CarFilterService carFilterService;
	private final ModelMapperService mapper;

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "inventory-create")
	public void consume(InventoryCreatedEvent event) {
		CarFilter filter = mapper.forRequest().map(event, CarFilter.class);
		carFilterService.add(filter);
		LOGGER.info("Inventory created event consumed: {}", event);
	}

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "car-delete")
	public void consume(CarDeletedEvent event) {
		carFilterService.delete(event.getCarId());
		LOGGER.info("Car deleted event consumed: {}", event);
	}

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "car-update")
	public void consume(CarUpdatedEvent event) {
		CarFilter filter = mapper.forRequest().map(event, CarFilter.class);
		String id = carFilterService.getByCarId(event.getCarId()).getData().getCarId();
		filter.setId(id);
		carFilterService.add(filter);
		LOGGER.info("Car updated event consumed: {}", event);
	}

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "brand-delete")
	public void consume(BrandDeletedEvent event) {
		carFilterService.deleteAllByBrandId(event.getBrandId());

		LOGGER.info("Brand deleted event consumed: {}", event);
	}

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "brand-update")
	public void consume(BrandUpdatedEvent event) {
		carFilterService.getByBrandId(event.getCarBrandId()).getData().forEach(filter -> {
			filter.setCarBrandName(event.getCarBrandName());
			carFilterService.add(filter);
		});

		LOGGER.info("Brand updated event consumed: {}", event);
	}

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "model-delete")
	public void consume(ModelDeletedEvent event) {
		carFilterService.deleteAllByModelId(event.getModelId());

		LOGGER.info("Model deleted event consumed: {}", event);
	}

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "model-update")
	public void consume(ModelUpdatedEvent event) {
		carFilterService.getByModelId(event.getModelId()).getData().forEach(filter -> {
			filter.setCarModelName(event.getModelName());
			filter.setCarBrandId(event.getBrandId());
			filter.setCarBrandName(
					carFilterService.getByBrandId(event.getBrandId()).getData().get(0).getCarBrandName());
			carFilterService.add(filter);
		});

		LOGGER.info("Model updated event consumed: {}", event);
	}

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "car-rental-create")
	public void consume(CarRentalCreatedEvent event) {
		CarFilter filter = carFilterService.getByCarId(event.getCarId()).getData();
		filter.setCarState(3); // 3 = Rented
		carFilterService.add(filter);

		LOGGER.info("Car rental created event consumed: {}", event);
	}

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "car-rental-update")
	public void consume(CarRentalUpdatedEvent event) {
		CarFilter oldCar = carFilterService.getByCarId(event.getOldCarId()).getData();
		CarFilter newCar = carFilterService.getByCarId(event.getNewCarId()).getData();
		oldCar.setCarState(1); // 1 -Available
		newCar.setCarState(3); // 3 -Rented
		carFilterService.add(oldCar);
		carFilterService.add(newCar);

		LOGGER.info("Car rental updated event consumed: {}", event);
	}

	@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "car-rental-delete")
	public void consume(CarRentalDeletedEvent event) {
		CarFilter car = carFilterService.getByCarId(event.getCarId()).getData();
		car.setCarState(1); // 1 -Available
		carFilterService.add(car);
		LOGGER.info("Car rental deleted event consumed: {}", event);
	}
}
