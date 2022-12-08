package com.kodlamaio.rentalservice.business.requests.update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRentalRequest {
	@NotBlank
	@NotNull
	private String id;

	@NotBlank
	@NotNull
	private String carId;
	@NotNull
	@Min(value = 1)
	private int rentedForDays;
	@NotNull
	@Min(value = 0)
	private double dailyPrice;

}
