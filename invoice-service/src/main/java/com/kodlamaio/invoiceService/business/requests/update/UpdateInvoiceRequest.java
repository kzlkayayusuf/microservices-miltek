package com.kodlamaio.invoiceService.business.requests.update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInvoiceRequest {

	@NotNull
	@NotBlank
	private String id;
	@NotBlank
	private String carId;
	@NotBlank
	private String fullName;
	@NotBlank
	private String modelName;
	@NotBlank
	private String brandName;
	@NotNull
	@Min(1886)
	private int modelYear;
	@Min(0)
	private double dailyPrice;
	@Min(0)
	private double totalPrice;
	@Min(0)
	private int rentedForDays;
}
