package com.kodlamaio.common.events.inventories.cars;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDeletedEvent {

	private String carId;
}
