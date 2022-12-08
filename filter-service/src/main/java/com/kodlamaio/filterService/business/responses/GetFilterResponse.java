package com.kodlamaio.filterService.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetFilterResponse {

	private String id;
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
