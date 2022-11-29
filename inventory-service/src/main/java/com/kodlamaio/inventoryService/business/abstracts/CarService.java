package com.kodlamaio.inventoryService.business.abstracts;

import java.util.List;

import com.kodlamaio.inventoryService.business.requests.create.CreateCarRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateCarResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllCarsResponse;

public interface CarService {
	List<GetAllCarsResponse> getAll();

	CreateCarResponse add(CreateCarRequest createCarRequest);
}
