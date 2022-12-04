package com.kodlamaio.rentalservice.business.requests.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

	@Min(value = 1)
	private int rentedForDays;

	@Min(value = 0)
	private double dailyPrice;

}
