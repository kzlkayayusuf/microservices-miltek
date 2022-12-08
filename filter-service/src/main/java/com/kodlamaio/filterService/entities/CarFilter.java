package com.kodlamaio.filterService.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "search-inventory")
public class CarFilter {

	@Id
	private String id;
	@Field(name = "carId")
	private String carId;
	@Field(name = "modelId")
	private String modelId;
	@Field(name = "brandId")
	private String brandId;
	@Field(name = "modelName")
	private String modelName;
	@Field(name = "brandName")
	private String brandName;
	@Field(name = "dailyPrice")
	private double dailyPrice;
	@Field(name = "modelYear")
	private int modelYear;
	@Field(name = "plate")
	private String plate;
	@Field(name = "state")
	private int state;
}
