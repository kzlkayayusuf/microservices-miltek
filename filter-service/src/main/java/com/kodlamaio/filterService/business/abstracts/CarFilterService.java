package com.kodlamaio.filterService.business.abstracts;

import java.util.List;

import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.filterService.business.responses.GetAllFiltersResponse;
import com.kodlamaio.filterService.business.responses.GetFilterResponse;
import com.kodlamaio.filterService.entities.CarFilter;

public interface CarFilterService {
	DataResult<List<GetAllFiltersResponse>> getAll();

	DataResult<List<GetAllFiltersResponse>> getByBrandName(String brandName);

	DataResult<List<GetAllFiltersResponse>> getByModelName(String modelName);

	DataResult<GetFilterResponse> getByPlate(String plate);

	DataResult<List<GetAllFiltersResponse>> searchByPlate(String plate);

	DataResult<List<GetAllFiltersResponse>> searchByBrandName(String brandName);

	DataResult<List<GetAllFiltersResponse>> searchByModelName(String modelName);

	DataResult<List<GetAllFiltersResponse>> getByModelYear(int modelYear);

	DataResult<List<GetAllFiltersResponse>> getByState(int state);

	// Consumer service
	DataResult<CarFilter> getByCarId(String id);

	DataResult<List<CarFilter>> getByModelId(String modelId);

	DataResult<List<CarFilter>> getByBrandId(String brandId);

	Result add(CarFilter mongodb);

	Result delete(String id);

	Result deleteAllByBrandId(String brandId);

	Result deleteAllByModelId(String modelId);
}
