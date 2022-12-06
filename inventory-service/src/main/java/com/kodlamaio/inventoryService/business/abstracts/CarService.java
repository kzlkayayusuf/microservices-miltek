package com.kodlamaio.inventoryService.business.abstracts;

import java.util.List;

import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.inventoryService.business.requests.create.CreateCarRequest;
import com.kodlamaio.inventoryService.business.requests.update.UpdateCarRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateCarResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllCarsResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetCarResponse;
import com.kodlamaio.inventoryService.business.responses.update.UpdateCarResponse;

public interface CarService {

	DataResult<List<GetAllCarsResponse>> getAll();

	DataResult<GetCarResponse> getById(String carId);

	DataResult<GetCarResponse> getByPlate(String plate);

	DataResult<CreateCarResponse> add(CreateCarRequest createCarRequest);

	DataResult<UpdateCarResponse> update(UpdateCarRequest updateCarRequest);

	Result deleteById(String id);

	UpdateCarResponse updateCarState(String carId, int state);

	Result checkCarAvailable(String carId);
}
