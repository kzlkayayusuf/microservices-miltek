package com.kodlamaio.inventoryService.business.concretes;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.inventories.models.ModelDeletedEvent;
import com.kodlamaio.common.events.inventories.models.ModelUpdatedEvent;
import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.common.utilities.results.SuccessDataResult;
import com.kodlamaio.common.utilities.results.SuccessResult;
import com.kodlamaio.inventoryService.business.abstracts.BrandService;
import com.kodlamaio.inventoryService.business.abstracts.ModelService;
import com.kodlamaio.inventoryService.business.constants.Messages;
import com.kodlamaio.inventoryService.business.requests.create.CreateModelRequest;
import com.kodlamaio.inventoryService.business.requests.update.UpdateModelRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateModelResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllModelsResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetModelResponse;
import com.kodlamaio.inventoryService.business.responses.update.UpdateModelResponse;
import com.kodlamaio.inventoryService.dataAccess.ModelRepository;
import com.kodlamaio.inventoryService.entities.Model;
import com.kodlamaio.inventoryService.kafka.producers.InventoryProducer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ModelManager implements ModelService {

	private ModelRepository modelRepository;
	private ModelMapperService modelMapperService;
	private BrandService brandService;
	private final InventoryProducer producer;

	@Override
	public DataResult<List<GetAllModelsResponse>> getAll() {
		List<Model> models = modelRepository.findAll();
		List<GetAllModelsResponse> responses = models.stream()
				.map(model -> modelMapperService.forResponse().map(model, GetAllModelsResponse.class)).toList();
		return new SuccessDataResult<List<GetAllModelsResponse>>(responses, Messages.ModelListed);
	}

	@Override
	public DataResult<CreateModelResponse> add(CreateModelRequest createModelRequest) {
		checkIfModelExistsByName(createModelRequest.getName());
		checkIfBrandNotExistsById(createModelRequest.getBrandId());
		Model model = modelMapperService.forRequest().map(createModelRequest, Model.class);
		model.setId(UUID.randomUUID().toString());
		modelRepository.save(model);

		CreateModelResponse response = modelMapperService.forResponse().map(model, CreateModelResponse.class);
		return new SuccessDataResult<CreateModelResponse>(response, Messages.ModelAdded);
	}

	@Override
	public DataResult<UpdateModelResponse> update(UpdateModelRequest updateModelRequest) {
		checkIfModelNotExistsById(updateModelRequest.getId());
		checkIfBrandNotExistsById(updateModelRequest.getBrandId());
		Model model = modelMapperService.forRequest().map(updateModelRequest, Model.class);
		modelRepository.save(model);

		updateMongo(updateModelRequest.getId(), updateModelRequest.getName(), updateModelRequest.getBrandId());

		UpdateModelResponse response = modelMapperService.forResponse().map(model, UpdateModelResponse.class);
		return new SuccessDataResult<UpdateModelResponse>(response, Messages.ModelUpdated);
	}

	@Override
	public DataResult<GetModelResponse> getById(String id) {
		checkIfModelNotExistsById(id);
		Model model = modelRepository.findById(id).get();
		GetModelResponse response = modelMapperService.forResponse().map(model, GetModelResponse.class);
		return new SuccessDataResult<GetModelResponse>(response);
	}

	@Override
	public DataResult<GetModelResponse> getByName(String name) {
		checkIfModelNotExistsByName(name);
		Model model = this.modelRepository.findByName(name).get();
		GetModelResponse response = modelMapperService.forResponse().map(model, GetModelResponse.class);
		return new SuccessDataResult<GetModelResponse>(response);
	}

	@Override
	public Result deleteById(String id) {
		checkIfModelNotExistsById(id);
		this.modelRepository.deleteById(id);

		deleteMongo(id);

		return new SuccessResult(Messages.ModelDeleted);
	}

	private void updateMongo(String id, String name, String brandId) {
		ModelUpdatedEvent event = new ModelUpdatedEvent();
		event.setId(id);
		event.setName(name);
		event.setBrandId(brandId);
		producer.sendMessage(event);
	}

	private void deleteMongo(String id) {
		ModelDeletedEvent event = new ModelDeletedEvent();
		event.setModelId(id);
		producer.sendMessage(event);
	}

	private void checkIfModelExistsByName(String name) {
		if (this.modelRepository.findByName(name).isPresent()) {
			throw new BusinessException("MODEL.EXISTS");
		}
	}

	private void checkIfModelNotExistsById(String id) {
		if (!this.modelRepository.findById(id).isPresent()) {
			throw new BusinessException("MODEL.NOT.EXISTS");
		}
	}

	private void checkIfModelNotExistsByName(String name) {
		if (!this.modelRepository.findByName(name).isPresent()) {
			throw new BusinessException("MODEL.NOT.EXISTS");
		}
	}

	private void checkIfBrandNotExistsById(String brandId) {
		if (this.brandService.getById(brandId) == null) {
			throw new BusinessException("BRAND.ID.NOT.EXISTS");
		}
	}

}
