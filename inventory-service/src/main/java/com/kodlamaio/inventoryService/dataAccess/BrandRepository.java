package com.kodlamaio.inventoryService.dataAccess;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodlamaio.inventoryService.entities.Brand;

public interface BrandRepository extends JpaRepository<Brand, String> {
	Optional<List<Brand>> findByName(String name);
}
