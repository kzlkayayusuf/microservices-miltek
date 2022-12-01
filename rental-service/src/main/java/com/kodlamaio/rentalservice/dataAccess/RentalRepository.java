package com.kodlamaio.rentalservice.dataAccess;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.rentalservice.entities.Rental;

public interface RentalRepository extends JpaRepository<Rental, String> {
	Optional<Rental> findByCarId(String id);
}
