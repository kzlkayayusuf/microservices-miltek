package com.kodlamaio.inventoryService.business.abstracts;

import java.util.List;

import com.kodlamaio.inventoryService.business.requests.create.CreateModelRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateModelResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllModelsResponse;

public interface ModelService {
	List<GetAllModelsResponse> getAll();

	CreateModelResponse add(CreateModelRequest createModelRequest);
}
