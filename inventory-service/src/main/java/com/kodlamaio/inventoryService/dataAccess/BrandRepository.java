package com.kodlamaio.inventoryService.dataAccess;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.inventoryService.entities.Brand;

public interface BrandRepository extends JpaRepository<Brand, String> {
	Optional<Brand> findByName(String name);
}
