package com.kodlamaio.inventoryService.business.responses.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCarResponse {

	private String id;
	private int modelYear;
	private double dailyPrice;
	private String plate;
	private int state;
	private String modelId;
}
