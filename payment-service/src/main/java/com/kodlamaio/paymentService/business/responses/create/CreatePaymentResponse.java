package com.kodlamaio.paymentService.business.responses.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentResponse {

	private String id;
	private String cardNumber;
	private String fullName;
	private int cardExpirationYear;
	private int cardExpirationMonth;
	private String cardCvv;
	private double balance;
}
