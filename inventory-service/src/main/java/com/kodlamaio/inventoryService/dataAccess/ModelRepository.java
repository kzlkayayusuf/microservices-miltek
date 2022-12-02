package com.kodlamaio.inventoryService.dataAccess;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.inventoryService.entities.Model;

public interface ModelRepository extends JpaRepository<Model, String> {
	Optional<Model> findByName(String name);

	Optional<Model>  findByBrandId(String brandId);
}