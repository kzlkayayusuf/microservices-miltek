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
import com.kodlamaio.filterService.business.dataAccess.responses.GetAllFiltersResponse;
import com.kodlamaio.filterService.dataAccess.abstracts.CarFilterRepository;
import com.kodlamaio.filterService.entities.CarFilter;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CarFilterManager implements CarFilterService {

	private CarFilterRepository carFilterRepository;
	private ModelMapperService modelMapperService;

	@Override
	public DataResult<List<GetAllFiltersResponse>> getAll() {
		List<CarFilter> filters = carFilterRepository.findAll();
		List<GetAllFiltersResponse> responses = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(responses, Messages.FilterListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> getByBrandName(String brandName) {
		List<CarFilter> filters = carFilterRepository.findByBrandNameIgnoreCase(brandName);
		List<GetAllFiltersResponse> responses = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(responses, Messages.FilterBrandNameListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> getByModelName(String modelName) {
		List<CarFilter> filters = carFilterRepository.findByModelNameIgnoreCase(modelName);
		List<GetAllFiltersResponse> responses = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(responses, Messages.FilterModelNameListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> getByPlate(String plate) {
		List<CarFilter> filters = carFilterRepository.findByPlateIgnoreCase(plate);
		List<GetAllFiltersResponse> responses = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(responses, Messages.FilterPlateListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> searchByPlate(String plate) {
		List<CarFilter> filters = carFilterRepository.findByPlateContainingIgnoreCase(plate);
		List<GetAllFiltersResponse> responses = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(responses, Messages.FilterSearchPlateListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> searchByBrandName(String brandName) {
		List<CarFilter> filters = carFilterRepository.findByBrandNameContainingIgnoreCase(brandName);
		List<GetAllFiltersResponse> responses = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(responses, Messages.FilterSearchBrandNameListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> searchByModelName(String modelName) {
		List<CarFilter> filters = carFilterRepository.findByModelNameContainingIgnoreCase(modelName);
		List<GetAllFiltersResponse> responses = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(responses, Messages.FilterSearchModelNameListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> getByModelYear(int modelYear) {
		List<CarFilter> filters = carFilterRepository.findByModelYear(modelYear);
		List<GetAllFiltersResponse> responses = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(responses, Messages.FilterModelYearListed);
	}

	@Override
	public DataResult<List<GetAllFiltersResponse>> getByState(int state) {
		List<CarFilter> filters = carFilterRepository.findByState(state);
		List<GetAllFiltersResponse> responses = filters.stream()
				.map(filter -> modelMapperService.forResponse().map(filter, GetAllFiltersResponse.class)).toList();

		return new SuccessDataResult<List<GetAllFiltersResponse>>(responses, Messages.FilterStateListed);
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

}
