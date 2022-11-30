package com.kodlamaio.inventoryService.api;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.inventoryService.business.abstracts.CarService;
import com.kodlamaio.inventoryService.business.requests.create.CreateCarRequest;
import com.kodlamaio.inventoryService.business.requests.update.UpdateCarRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateCarResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllCarsResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetCarResponse;
import com.kodlamaio.inventoryService.business.responses.update.UpdateCarResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cars")
public class CarsController {
	private CarService carService;

	@GetMapping
	public List<GetAllCarsResponse> getAll() {
		return this.carService.getAll();
	}

	@PostMapping
	public CreateCarResponse add(@Valid @RequestBody CreateCarRequest createCarRequest) {
		return this.carService.add(createCarRequest);
	}

	@PutMapping
	public UpdateCarResponse update(@Valid @RequestBody UpdateCarRequest updateCarRequest) {
		return this.carService.update(updateCarRequest);
	}

	@GetMapping("/getById/{id}")
	public GetCarResponse getById(@PathVariable String id) {
		return this.carService.getById(id);
	}

	@GetMapping("/{plate}")
	public GetCarResponse getByPlate(@PathVariable String plate) {
		return carService.getByPlate(plate);
	}

	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable String id) {
		carService.deleteById(id);
	}
}
