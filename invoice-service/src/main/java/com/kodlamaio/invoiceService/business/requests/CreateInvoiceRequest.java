package com.kodlamaio.invoiceService.business.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceRequest {

	private String rentalId;
	private double totalPrice;
	private String cardHolder;
}
