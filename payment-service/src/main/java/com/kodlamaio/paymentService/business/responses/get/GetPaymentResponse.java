package com.kodlamaio.paymentService.business.responses.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPaymentResponse {

	private String id;
	private String cardNumber;
	private String fullName;
	private int cardExpirationYear;
	private int cardExpirationMonth;
	private String cardCvv;
	private double balance;
}
