package com.kodlamaio.filterService.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.filterService.business.abstracts.CarFilterService;
import com.kodlamaio.filterService.business.responses.GetAllFiltersResponse;
import com.kodlamaio.filterService.business.responses.GetFilterResponse;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/filters")
public class CarFiltersController {
	private final CarFilterService service;

	@GetMapping
	public ResponseEntity<?> getAll() {
		DataResult<List<GetAllFiltersResponse>> result = this.service.getAll();
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@GetMapping("/brand")
	public ResponseEntity<?> getByBrandName(@RequestParam String brandName) {
		DataResult<List<GetAllFiltersResponse>> result = this.service.getByBrandName(brandName);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@GetMapping("/model")
	public ResponseEntity<?> getByModelName(@RequestParam String modelName) {
		DataResult<List<GetAllFiltersResponse>> result = this.service.getByModelName(modelName);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@GetMapping("/plate")
	public ResponseEntity<?> getByPlate(@RequestParam String plate) {
		DataResult<GetFilterResponse> result = this.service.getByPlate(plate);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@GetMapping("/plate-search")
	public ResponseEntity<?> searchByPlate(@RequestParam String plate) {
		DataResult<List<GetAllFiltersResponse>> result = this.service.searchByPlate(plate);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@GetMapping("/brand-search")
	public ResponseEntity<?> searchByBrandName(@RequestParam String brandName) {
		DataResult<List<GetAllFiltersResponse>> result = this.service.searchByBrandName(brandName);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@GetMapping("/model-search")
	public ResponseEntity<?> searchByModelName(@RequestParam String modelName) {
		DataResult<List<GetAllFiltersResponse>> result = this.service.searchByModelName(modelName);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@GetMapping("/modelYear")
	public ResponseEntity<?> getByModelYear(@RequestParam int modelYear) {
		DataResult<List<GetAllFiltersResponse>> result = this.service.getByModelYear(modelYear);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@GetMapping("/state")
	public ResponseEntity<?> getByState(@RequestParam int state) {
		DataResult<List<GetAllFiltersResponse>> result = this.service.getByState(state);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}
}