package com.kodlamaio.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreatedEvent {
	private String message;
	private String rentalId;
	private double totalPrice;
	private String cardNo;
	private String cardHolder;
	private double cardBalance;
}
