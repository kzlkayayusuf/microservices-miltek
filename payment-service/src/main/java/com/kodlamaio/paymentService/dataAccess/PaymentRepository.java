package com.kodlamaio.paymentService.dataAccess;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.paymentService.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {
	boolean existsByCardNumberAndFullNameAndCardExpirationYearAndCardExpirationMonthAndCardCvv(String cardNumber,
			String fullName, int cardExpirationYear, int cardExpirationMonth, String cardCvv);

	Payment findByCardNumber(String cardNumber);

	boolean existsByCardNumber(String cardNumber);
}
