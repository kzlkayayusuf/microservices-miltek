package com.kodlamaio.common.events.inventories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryCreatedEvent {

	private String carId;
	private String modelId;
	private String brandId;
	private String modelName;
	private String brandName;
	private double dailyPrice;
	private int modelYear;
	private String plate;
	private int state;
}
