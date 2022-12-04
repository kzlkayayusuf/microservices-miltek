package com.kodlamaio.inventoryService.business.concretes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.inventoryService.business.abstracts.CarService;
import com.kodlamaio.inventoryService.business.abstracts.ModelService;
import com.kodlamaio.inventoryService.business.requests.create.CreateCarRequest;
import com.kodlamaio.inventoryService.business.requests.update.UpdateCarRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateCarResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllCarsResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetCarResponse;
import com.kodlamaio.inventoryService.business.responses.update.UpdateCarResponse;
import com.kodlamaio.inventoryService.dataAccess.CarRepository;
import com.kodlamaio.inventoryService.entities.Car;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CarManager implements CarService {

	private CarRepository carRepository;
	private ModelMapperService modelMapperService;
	private ModelService modelService;

	@Override
	public List<GetAllCarsResponse> getAll() {
		List<Car> cars = this.carRepository.findAll();
		List<GetAllCarsResponse> response = cars.stream()
				.map(car -> this.modelMapperService.forResponse().map(car, GetAllCarsResponse.class))
				.collect(Collectors.toList());
		return response;
	}

	@Override
	public CreateCarResponse add(CreateCarRequest createCarRequest) {
		checkIfCarExistsByPlate(createCarRequest.getPlate());
		checkIfModelNotExistsById(createCarRequest.getModelId());
		Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);
		car.setId(UUID.randomUUID().toString());

		this.carRepository.save(car);

		CreateCarResponse createCarResponse = this.modelMapperService.forResponse().map(car, CreateCarResponse.class);
		return createCarResponse;
	}

	@Override
	public UpdateCarResponse update(UpdateCarRequest updateCarRequest) {
		checkIfCarNotExistsById(updateCarRequest.getId());
		checkIfModelNotExistsById(updateCarRequest.getModelId());
		Car car = this.modelMapperService.forRequest().map(updateCarRequest, Car.class);
		this.carRepository.save(car);

		UpdateCarResponse updateCarResponse = this.modelMapperService.forResponse().map(car, UpdateCarResponse.class);

		return updateCarResponse;
	}

	@Override
	public GetCarResponse getById(String id) {
		checkIfCarNotExistsById(id);
		Car car = this.carRepository.findById(id).get();
		GetCarResponse carResponse = this.modelMapperService.forResponse().map(car, GetCarResponse.class);
		return carResponse;
	}

	@Override
	public GetCarResponse getByPlate(String plate) {
		checkIfCarNotExistsByPlate(plate);
		Car car = this.carRepository.findByPlate(plate).get();
		GetCarResponse carResponse = this.modelMapperService.forResponse().map(car, GetCarResponse.class);
		return carResponse;
	}

	@Override
	public void deleteById(String id) {
		checkIfCarNotExistsById(id);
		this.carRepository.deleteById(id);

	}

	@Override
	public UpdateCarResponse updateCarState(String carId, int state) {
		checkIfCarNotExistsById(carId);
		Car car = this.carRepository.findById(carId).get();
		car.setState(state);
		this.carRepository.save(car);
		UpdateCarResponse updateCarResponse = this.modelMapperService.forResponse().map(car, UpdateCarResponse.class);
		return updateCarResponse;
	}
	
	@Override
	public void checkCarAvailable(String carId) {
		Car car = this.carRepository.findById(carId).get();
		if (car.getState() == 3) {
			throw new BusinessException("Car Not Available");
		}

	}

	private void checkIfCarExistsByPlate(String plate) {
		if (this.carRepository.findByPlate(plate).isPresent()) {
			throw new BusinessException("CAR.EXISTS");
		}
	}

	private void checkIfCarNotExistsById(String id) {
		if (!this.carRepository.findById(id).isPresent()) {
			throw new BusinessException("CAR.NOT EXISTS");
		}
	}

	private void checkIfCarNotExistsByPlate(String plate) {
		if (!this.carRepository.findByPlate(plate).isPresent()) {
			throw new BusinessException("CAR.NOT EXISTS");
		}
	}

	private void checkIfModelNotExistsById(String modelId) {
		if (this.modelService.getById(modelId) == null) {
			throw new BusinessException("MODEL.ID.NOT EXISTS");
		}
	}

	

}
