package com.kodlamaio.rentalservice.business.requests.create;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.kodlamaio.common.dataAccess.CreatePaymentRequestDAL;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRentalRequest {

	@NotBlank
	@NotNull
	private String carId;
	@NotNull
	@Min(value = 1)
	private int rentedForDays;
	@NotNull
	@Min(value = 0)
	private double dailyPrice;
	private CreatePaymentRequestDAL paymentRequest;

}
