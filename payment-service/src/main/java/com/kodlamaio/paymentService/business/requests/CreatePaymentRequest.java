package com.kodlamaio.paymentService.business.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {
	
	@NotBlank
	@NotNull
	private String rentalId;
	
	@NotBlank
	@NotNull
	@Size(min = 16, max =16)
	private String cardNo;
	
	@NotBlank
	@NotNull
	@Size(min = 4)
	private String cardHolder;
	
	@Min(value = 0)
	private double cardBalance;
	
	@Min(0)
	@Max(1)
	private int state;
	
	@Min(value = 0)
	private double totalPrice;
}
