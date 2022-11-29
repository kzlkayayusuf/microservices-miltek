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

import com.kodlamaio.inventoryService.business.abstracts.ModelService;
import com.kodlamaio.inventoryService.business.requests.create.CreateModelRequest;
import com.kodlamaio.inventoryService.business.requests.update.UpdateModelRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateModelResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllModelsResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetModelResponse;
import com.kodlamaio.inventoryService.business.responses.update.UpdateModelResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/models")
public class ModelsController {
	private ModelService modelService;

	@GetMapping
	public List<GetAllModelsResponse> getAll() {
		return this.modelService.getAll();
	}

	@PostMapping
	public CreateModelResponse add(@Valid @RequestBody CreateModelRequest createModelRequest) {
		return this.modelService.add(createModelRequest);
	}

	@PutMapping
	public UpdateModelResponse update(@Valid @RequestBody UpdateModelRequest updateModelRequest) {
		return this.modelService.update(updateModelRequest);
	}

	@GetMapping("/getById/{id}")
	public GetModelResponse getById(@PathVariable String id) {
		return this.modelService.getById(id);
	}

	@GetMapping("/{name}")
	public GetModelResponse getByName(@PathVariable String name) {
		return modelService.getByName(name);
	}

	@DeleteMapping
	public void deleteById(@PathVariable String id) {
		modelService.deleteById(id);
	}
}
