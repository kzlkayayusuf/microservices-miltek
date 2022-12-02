package com.kodlamaio.inventoryService.business.concretes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.inventoryService.business.abstracts.BrandService;
import com.kodlamaio.inventoryService.business.abstracts.ModelService;
import com.kodlamaio.inventoryService.business.requests.create.CreateModelRequest;
import com.kodlamaio.inventoryService.business.requests.update.UpdateModelRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateModelResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllModelsResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetModelResponse;
import com.kodlamaio.inventoryService.business.responses.update.UpdateModelResponse;
import com.kodlamaio.inventoryService.dataAccess.ModelRepository;
import com.kodlamaio.inventoryService.entities.Model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ModelManager implements ModelService {

	private ModelRepository modelRepository;
	private ModelMapperService modelMapperService;
	private BrandService brandService;

	@Override
	public List<GetAllModelsResponse> getAll() {
		List<Model> models = this.modelRepository.findAll();
		List<GetAllModelsResponse> response = models.stream()
				.map(model -> this.modelMapperService.forResponse().map(model, GetAllModelsResponse.class))
				.collect(Collectors.toList());
		return response;
	}

	@Override
	public CreateModelResponse add(CreateModelRequest createModelRequest) {
		checkIfModelExistsByName(createModelRequest.getName());
		checkIfBrandNotExistsById(createModelRequest.getBrandId());
		Model model = this.modelMapperService.forRequest().map(createModelRequest, Model.class);
		model.setId(UUID.randomUUID().toString());

		this.modelRepository.save(model);

		CreateModelResponse createModelResponse = this.modelMapperService.forResponse().map(model,
				CreateModelResponse.class);
		return createModelResponse;
	}

	@Override
	public UpdateModelResponse update(UpdateModelRequest updateModelRequest) {
		checkIfModelNotExistsById(updateModelRequest.getId());
		checkIfBrandNotExistsById(updateModelRequest.getBrandId());
		Model model = this.modelMapperService.forRequest().map(updateModelRequest, Model.class);
		this.modelRepository.save(model);

		UpdateModelResponse updateModelResponse = this.modelMapperService.forResponse().map(model,
				UpdateModelResponse.class);

		return updateModelResponse;
	}

	@Override
	public GetModelResponse getById(String id) {
		checkIfModelNotExistsById(id);
		Model model = this.modelRepository.findById(id).get();
		GetModelResponse modelResponse = this.modelMapperService.forResponse().map(model, GetModelResponse.class);
		return modelResponse;
	}

	@Override
	public GetModelResponse getByName(String name) {
		checkIfModelNotExistsByName(name);
		Model model = this.modelRepository.findByName(name).get();
		GetModelResponse modelResponse = this.modelMapperService.forResponse().map(model, GetModelResponse.class);
		return modelResponse;
	}

	@Override
	public void deleteById(String id) {
		checkIfModelNotExistsById(id);
		this.modelRepository.deleteById(id);

	}

	private void checkIfModelExistsByName(String name) {
		if (this.modelRepository.findByName(name).isPresent()) {
			throw new BusinessException("MODEL.EXISTS");
		}
	}

	private void checkIfModelNotExistsById(String id) {
		if (!this.modelRepository.findById(id).isPresent()) {
			throw new BusinessException("MODEL.NOT EXISTS");
		}
	}

	private void checkIfModelNotExistsByName(String name) {
		if (!this.modelRepository.findByName(name).isPresent()) {
			throw new BusinessException("MODEL.NOT EXISTS");
		}
	}

	private void checkIfModelNotExistsByBrandId(String brandId) {
		if (!this.modelRepository.findByBrandId(brandId).isPresent()) {
			throw new BusinessException("MODEL.BRANDID.NOT EXISTS");
		}
	}

	private void checkIfBrandNotExistsById(String brandId) {
		if (this.brandService.getById(brandId) == null) {
			throw new BusinessException("BRAND.ID.NOT EXISTS");
		}
	}

}
