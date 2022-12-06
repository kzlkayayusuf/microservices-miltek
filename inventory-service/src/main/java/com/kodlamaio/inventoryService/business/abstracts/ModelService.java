package com.kodlamaio.inventoryService.business.abstracts;

import java.util.List;

import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.inventoryService.business.requests.create.CreateModelRequest;
import com.kodlamaio.inventoryService.business.requests.update.UpdateModelRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateModelResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllModelsResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetModelResponse;
import com.kodlamaio.inventoryService.business.responses.update.UpdateModelResponse;

public interface ModelService {

	DataResult<List<GetAllModelsResponse>> getAll();

	DataResult<GetModelResponse> getById(String id);

	DataResult<GetModelResponse> getByName(String name);

	DataResult<CreateModelResponse> add(CreateModelRequest createModelRequest);

	DataResult<UpdateModelResponse> update(UpdateModelRequest updateModelRequest);

	Result deleteById(String id);
}
