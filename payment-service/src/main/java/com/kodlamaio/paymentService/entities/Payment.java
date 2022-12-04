package com.kodlamaio.paymentService.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "rentalId")
	private String rentalId;

	@Column(name = "cardNo")
	private String cardNo;

	@Column(name = "cardHolder")
	private String cardHolder;

	@Column(name = "cardBalance")
	private double cardBalance;

	@Column(name = "status")
	private int status;  //0-Red,1-Onay

	@Column(name = "totalPrice")
	private double totalPrice;

}
