package com.kodlamaio.rentalservice.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rentals")
public class Rental {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "carId")
	private String carId;

	@Column(name = "dateStarted")
	private LocalDateTime dateStarted=LocalDateTime.now();

	@Column(name = "rentedForDays")
	private int rentedForDays;

	@Column(name = "dailyPrice")
	private double dailyPrice;

	@Column(name = "totalPrice")
	private double totalPrice;
}
