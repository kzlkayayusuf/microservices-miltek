package com.kodlamaio.inventoryService.dataAccess;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.inventoryService.entities.Car;

public interface CarRepository extends JpaRepository<Car, String> {
	Optional<Car> findByPlate(String plate);

	Optional<Car>  findByModelId(String modelId);
}
