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
@Document(collection = "filter-inventory")
public class CarFilter {

	@Id
	private String id;
	@Field(name = "carId")
	private String carId;
	@Field(name = "carDailyPrice")
	private double carDailyPrice;
	@Field(name = "carModelYear")
	private int carModelYear;
	@Field(name = "carPlate")
	private String carPlate;
	@Field(name = "modelId")
	private String carModelId;
	@Field(name = "carModelName")
	private String carModelName;
	@Field(name = "carBrandId")
	private String carBrandId;
	@Field(name = "carBrandName")
	private String carBrandName;
	@Field(name = "carState")
	private int carState;
}
