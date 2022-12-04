package com.kodlamaio.inventoryService.entities.filter;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarFilter {
	@Id
	private String id;
	
	private String carId;
	private String carPlate;
	private int carModelYear;
	private double carDailyPrice;
	private int state;
	
	private String carModelId;
	private String carModelName;
	
	private String carBrandId;
	private String carBrandName;
}
