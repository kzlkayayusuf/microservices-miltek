package com.kodlamaio.paymentService.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllPaymentsResponse {

	private String id;
	private String rentalId;
	private double totalPrice;
	private String cardNo;
	private String cardHolder;
	private double cardBalance;
	private int status;
	
}
