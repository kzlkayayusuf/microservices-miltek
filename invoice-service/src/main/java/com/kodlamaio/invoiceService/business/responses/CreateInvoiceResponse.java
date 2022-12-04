package com.kodlamaio.invoiceService.business.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceResponse {

	private String id;
	private String rentalId;
	private double totalPrice;
	private String cardHolder;
}
