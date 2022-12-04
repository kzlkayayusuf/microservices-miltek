package com.kodlamaio.inventoryService.dataAccess;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kodlamaio.inventoryService.entities.filter.CarFilter;

public interface CarFilterRepository extends MongoRepository<CarFilter, String> {
}
