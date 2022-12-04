package com.kodlamaio.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceCreatedEvent {
	private String message;
	private String rentalId;
	private double totalPrice;
	private String cardHolder;
}
