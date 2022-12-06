package com.kodlamaio.rentalservice.business.responses.get;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllRentalsResponse {

	private String id;
	private String carId;
	private LocalDateTime dateStarted;
	private int rentedForDays;
	private double carDailyPrice;
	private double totalPrice;
	private int carModelYear;
	private String carPlate;
	private int carState;
	private String carModelName;
	private String carModelBrandName;
}
