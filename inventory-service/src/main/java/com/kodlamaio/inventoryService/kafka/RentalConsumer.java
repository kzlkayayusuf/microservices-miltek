package com.kodlamaio.inventoryService.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.RentalCreatedEvent;

@Service
public class RentalConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(RentalConsumer.class);

    @KafkaListener(
            topics = "${spring.kafka.topic.name}"
            ,groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(RentalCreatedEvent event){
        LOGGER.info(String.format("Order event received in stock service => %s", event.toString()));

        // save the order event into the database
    }
}
