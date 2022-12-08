package com.kodlamaio.inventoryService.business.requests.create;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCarRequest {

	@Min(0)
	private double dailyPrice;
	@Min(1994)
	private int modelYear;
	@NotBlank
	@NotNull
	private String plate;
	@NotBlank
	@NotNull
	private String modelId;
}
