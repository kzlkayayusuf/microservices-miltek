package com.kodlamaio.inventoryService.business.requests.update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarRequest {

	@NotBlank
	@NotNull
	private String id;
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
