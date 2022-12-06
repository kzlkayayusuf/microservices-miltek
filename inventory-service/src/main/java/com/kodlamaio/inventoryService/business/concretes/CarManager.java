package com.kodlamaio.inventoryService.business.concretes;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.cars.CarCreatedEvent;
import com.kodlamaio.common.events.cars.CarDeletedEvent;
import com.kodlamaio.common.events.cars.CarUpdatedEvent;
import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.common.utilities.results.SuccessDataResult;
import com.kodlamaio.common.utilities.results.SuccessResult;
import com.kodlamaio.inventoryService.business.abstracts.CarService;
import com.kodlamaio.inventoryService.business.abstracts.ModelService;
import com.kodlamaio.inventoryService.business.constans.Messages;
import com.kodlamaio.inventoryService.business.requests.create.CreateCarRequest;
import com.kodlamaio.inventoryService.business.requests.update.UpdateCarRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateCarResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllCarsResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetCarResponse;
import com.kodlamaio.inventoryService.business.responses.update.UpdateCarResponse;
import com.kodlamaio.inventoryService.dataAccess.CarRepository;
import com.kodlamaio.inventoryService.entities.Car;
import com.kodlamaio.inventoryService.kafka.producers.CarCreatedProducer;
import com.kodlamaio.inventoryService.kafka.producers.CarDeletedProducer;
import com.kodlamaio.inventoryService.kafka.producers.CarUpdatedProducer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CarManager implements CarService {

	private CarRepository carRepository;
	private ModelMapperService modelMapperService;
	private ModelService modelService;
	private CarCreatedProducer carCreatedProducer;
	private CarUpdatedProducer carUpdatedProducer;
	private CarDeletedProducer carDeletedProducer;

	@Override
	public DataResult<List<GetAllCarsResponse>> getAll() {

		List<Car> cars = carRepository.findAll();
		List<GetAllCarsResponse> responses = cars.stream()
				.map(car -> modelMapperService.forResponse().map(car, GetAllCarsResponse.class)).toList();
		return new SuccessDataResult<List<GetAllCarsResponse>>(responses, Messages.CarListed);
	}

	@Override
	public DataResult<CreateCarResponse> add(CreateCarRequest createCarRequest) {
		checkIfCarExistsByPlate(createCarRequest.getPlate());
		checkIfModelNotExistsById(createCarRequest.getModelId());
		Car car = modelMapperService.forRequest().map(createCarRequest, Car.class);
		car.setId(UUID.randomUUID().toString());
		carRepository.save(car);

		GetCarResponse result = getById(car.getId()).getData();
		CarCreatedEvent carCreatedEvent = modelMapperService.forResponse().map(result, CarCreatedEvent.class);
		carCreatedEvent.setMessage(Messages.CarAdded);
		carCreatedProducer.sendMessage(carCreatedEvent);

		CreateCarResponse response = modelMapperService.forResponse().map(car, CreateCarResponse.class);
		return new SuccessDataResult<CreateCarResponse>(response, Messages.CarAdded);
	}

	@Override
	public DataResult<UpdateCarResponse> update(UpdateCarRequest updateCarRequest) {
		checkIfCarNotExistsById(updateCarRequest.getId());
		checkIfModelNotExistsById(updateCarRequest.getModelId());
		Car car = modelMapperService.forRequest().map(updateCarRequest, Car.class);
		carRepository.save(car);

		GetCarResponse result = getById(car.getId()).getData();
		CarUpdatedEvent carUpdatedEvent = modelMapperService.forResponse().map(result, CarUpdatedEvent.class);
		carUpdatedEvent.setMessage(Messages.CarUpdated);
		carUpdatedProducer.sendMessage(carUpdatedEvent);

		UpdateCarResponse response = modelMapperService.forResponse().map(car, UpdateCarResponse.class);
		return new SuccessDataResult<UpdateCarResponse>(response, Messages.CarUpdated);
	}

	@Override
	public DataResult<GetCarResponse> getById(String carId) {
		checkIfCarNotExistsById(carId);
		Car car = carRepository.findById(carId).get();
		GetCarResponse response = modelMapperService.forResponse().map(car, GetCarResponse.class);
		return new SuccessDataResult<GetCarResponse>(response, Messages.CarListed);
	}

	@Override
	public DataResult<GetCarResponse> getByPlate(String plate) {
		checkIfCarNotExistsByPlate(plate);
		Car car = this.carRepository.findByPlate(plate).get();
		GetCarResponse response = modelMapperService.forResponse().map(car, GetCarResponse.class);
		return new SuccessDataResult<GetCarResponse>(response, Messages.CarListed);
	}

	@Override
	public Result deleteById(String id) {
		checkIfCarNotExistsById(id);
		this.carRepository.deleteById(id);

		CarDeletedEvent deletedEvent = new CarDeletedEvent();
		deletedEvent.setCarId(id);
		carDeletedProducer.sendMessage(deletedEvent);
		return new SuccessResult(Messages.CarDeleted);

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
