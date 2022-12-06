package com.kodlamaio.inventoryService.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.inventoryService.business.abstracts.CarService;
import com.kodlamaio.inventoryService.business.requests.create.CreateCarRequest;
import com.kodlamaio.inventoryService.business.requests.update.UpdateCarRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateCarResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllCarsResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetCarResponse;
import com.kodlamaio.inventoryService.business.responses.update.UpdateCarResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cars")
public class CarsController {
	private CarService carService;

	@GetMapping
	public ResponseEntity<?> getAll() {
		DataResult<List<GetAllCarsResponse>> result = carService.getAll();
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@PostMapping
	public ResponseEntity<?> add(@RequestBody @Valid CreateCarRequest createCarRequest) {
		DataResult<CreateCarResponse> result = carService.add(createCarRequest);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody @Valid UpdateCarRequest updateCarRequest) {
		DataResult<UpdateCarResponse> result = carService.update(updateCarRequest);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<?> getById(@PathVariable String carId) {
		DataResult<GetCarResponse> result = carService.getById(carId);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@GetMapping("/{plate}")
	public ResponseEntity<?> getByPlate(@PathVariable String plate) {
		Result result = carService.getByPlate(plate);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable String id) {
		Result result = carService.deleteById(id);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);

	}

	@GetMapping("/checkCarAvailable/{id}")
	void checkCarAvailable(@PathVariable String id) {
		this.carService.checkCarAvailable(id);
	}

	@GetMapping("/getCarResponse/{id}")
	public GetCarResponse getCarResponse(@PathVariable String id) {
		return carService.getById(id).getData();
	}
}
