package com.kodlamaio.inventoryService.business.responses.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarResponse {
	private String id;
	private int modelYear;
	private double dailyPrice;
	private String plate;
	private int state;
	private String modelId;
}
