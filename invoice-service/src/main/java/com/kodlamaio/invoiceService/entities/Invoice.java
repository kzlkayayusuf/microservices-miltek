package com.kodlamaio.invoiceService.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "rentalId")
	private String rentalId;
	@Column(name = "totalPrice")
	private double totalPrice;
	@Column(name = "cardHolder")
	private String cardHolder;
}
