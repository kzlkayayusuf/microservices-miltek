package com.kodlamaio.inventoryService.business.responses.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCarsResponse {
	
	private String id;
	private int modelYear;
	private double dailyPrice;
	private String plate;
	private String brandName;
	private String modelName;
	private int state;
}
