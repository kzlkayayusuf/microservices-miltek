package com.kodlamaio.filterService.business.concretes;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.common.utilities.results.SuccessDataResult;
import com.kodlamaio.common.utilities.results.SuccessResult;
import com.kodlamaio.filterService.business.abstracts.CarFilterService;
import com.kodlamaio.filterService.business.constants.Messages;
import com.kodlamaio.filterService.business.responses.GetAllFiltersResponse;
import com.kodlamaio.filterService.business.responses.GetFilterResponse;
import com.kodlamaio.filterService.dataAccess.abstracts.CarFilterRepository;
import com.kodlamaio.filterService.entities.CarFilter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CarFilterManager implements CarFilterService {

	private CarFilterRepository carFilterRepository;
	private ModelMapperService modelMapperService;

	@Override
	public DataResult<List<GetAllFiltersResponse>> getAll() {
		List<CarFilter> filters = carFilterRepository.findAll();
		List<GetAllFiltersResponse> response = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(response, Messages.FilterListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> getByBrandName(String brandName) {
		List<CarFilter> filters = carFilterRepository.findByBrandNameIgnoreCase(brandName);
		List<GetAllFiltersResponse> response = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(response, Messages.FilterBrandNameListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> getByModelName(String modelName) {
		List<CarFilter> filters = carFilterRepository.findByModelNameIgnoreCase(modelName);
		List<GetAllFiltersResponse> response = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(response, Messages.FilterModelNameListed);
	}

	@Override
	public DataResult<GetFilterResponse> getByPlate(String plate) {
		checkIfExistByPlate(plate);
		CarFilter filter = carFilterRepository.findByPlateIgnoreCase(plate);
		GetFilterResponse response = modelMapperService.forResponse().map(filter, GetFilterResponse.class);

		return new SuccessDataResult<GetFilterResponse>(response, Messages.FilterPlateListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> searchByPlate(String plate) {
		List<CarFilter> filters = carFilterRepository.findByPlateContainingIgnoreCase(plate);
		List<GetAllFiltersResponse> response = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(response, Messages.FilterSearchPlateListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> searchByBrandName(String brandName) {
		List<CarFilter> filters = carFilterRepository.findByBrandNameContainingIgnoreCase(brandName);
		List<GetAllFiltersResponse> response = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(response, Messages.FilterSearchBrandNameListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> searchByModelName(String modelName) {
		List<CarFilter> filters = carFilterRepository.findByModelNameContainingIgnoreCase(modelName);
		List<GetAllFiltersResponse> response = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(response, Messages.FilterSearchModelNameListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> getByModelYear(int modelYear) {
		List<CarFilter> filters = carFilterRepository.findByModelYear(modelYear);
		List<GetAllFiltersResponse> response = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(response, Messages.FilterModelYearListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> getByState(int state) {
		List<CarFilter> filters = carFilterRepository.findByState(state);
		List<GetAllFiltersResponse> response = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(response, Messages.FilterStateListed);
	}

	@Override
	public DataResult<CarFilter> getByCarId(String carId) {
		CarFilter carFilter = carFilterRepository.findByCarId(carId);
		return new SuccessDataResult<CarFilter>(carFilter);
	}

	@Override
	public DataResult<List<CarFilter>> getByModelId(String modelId) {
		List<CarFilter> carFilter = carFilterRepository.findByModelId(modelId);
		return new SuccessDataResult<List<CarFilter>>(carFilter);
	}

	@Override
	public DataResult<List<CarFilter>> getByBrandId(String brandId) {
		List<CarFilter> carFilter = carFilterRepository.findByBrandId(brandId);
		return new SuccessDataResult<List<CarFilter>>(carFilter);
	}

	@Override
	public Result add(CarFilter carFilter) {
		carFilterRepository.save(carFilter);
		return new SuccessResult();
	}

	@Override
	public Result delete(String id) {
		carFilterRepository.deleteByCarId(id);
		return new SuccessResult();
	}

	@Override
	public Result deleteAllByBrandId(String brandId) {
		carFilterRepository.deleteAllByBrandId(brandId);
		return new SuccessResult();
	}

	@Override
	public Result deleteAllByModelId(String modelId) {
		carFilterRepository.deleteAllByModelId(modelId);
		return new SuccessResult();
	}

	private void checkIfExistByPlate(String plate) {
		if (!carFilterRepository.existsByPlate(plate)) {
			throw new RuntimeException("CAR_NOT_EXISTS");
		}
	}

}
