package com.kodlamaio.invoiceService.entities;

import java.time.LocalDateTime;

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
	@Column(name = "carId")
	private String carId;
	@Column(name = "fullName")
	private String fullName;
	@Column(name = "modelName")
	private String modelName;
	@Column(name = "brandName")
	private String brandName;
	@Column(name = "modelYear")
	private int modelYear;
	@Column(name = "dailyPrice")
	private double dailyPrice;
	@Column(name = "totalPrice")
	private double totalPrice;
	@Column(name = "rentedForDays")
	private int rentedForDays;
	@Column(name = "rentedAt")
	private LocalDateTime rentedAt;
}
