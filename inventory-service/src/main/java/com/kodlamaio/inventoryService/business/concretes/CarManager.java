package com.kodlamaio.inventoryService.business.concretes;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.inventories.InventoryCreatedEvent;
import com.kodlamaio.common.events.inventories.cars.CarDeletedEvent;
import com.kodlamaio.common.events.inventories.cars.CarUpdatedEvent;
import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.common.utilities.results.SuccessDataResult;
import com.kodlamaio.common.utilities.results.SuccessResult;
import com.kodlamaio.inventoryService.business.abstracts.CarService;
import com.kodlamaio.inventoryService.business.abstracts.ModelService;
import com.kodlamaio.inventoryService.business.constants.Messages;
import com.kodlamaio.inventoryService.business.requests.create.CreateCarRequest;
import com.kodlamaio.inventoryService.business.requests.update.UpdateCarRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateCarResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllCarsResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetCarResponse;
import com.kodlamaio.inventoryService.business.responses.update.UpdateCarResponse;
import com.kodlamaio.inventoryService.dataAccess.CarRepository;
import com.kodlamaio.inventoryService.entities.Car;
import com.kodlamaio.inventoryService.kafka.producers.InventoryProducer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CarManager implements CarService {

	private CarRepository carRepository;
	private ModelMapperService modelMapperService;
	private ModelService modelService;
	private final InventoryProducer producer;

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
		car.setState(1);// 1-available , 2-maintenance, 3- rented
		carRepository.save(car);

		addToMongodb(car.getId());

		CreateCarResponse response = modelMapperService.forResponse().map(car, CreateCarResponse.class);
		return new SuccessDataResult<CreateCarResponse>(response, Messages.CarAdded);
	}

	@Override
	public DataResult<UpdateCarResponse> update(UpdateCarRequest updateCarRequest) {
		checkIfCarNotExistsById(updateCarRequest.getId());
		checkIfModelNotExistsById(updateCarRequest.getModelId());
		
		int oldCarState = carRepository.findById(updateCarRequest.getId()).get().getState();
		
		Car car = modelMapperService.forRequest().map(updateCarRequest, Car.class);
		car.setState(oldCarState);
		carRepository.save(car);

		updateMongo(updateCarRequest, car.getId());

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

		deleteMongo(id);
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
	public Result checkCarAvailable(String carId) {
		checkIfCarNotExistsById(carId);
		Car car = this.carRepository.findById(carId).get();
		if (car.getState() != 1) {
			throw new BusinessException("Car Not Available");
		}
		return new SuccessResult(Messages.CarAvailable);

	}

	private void addToMongodb(String id) {
		checkIfCarNotExistsById(id);
		Car car = carRepository.findById(id).orElseThrow();
		InventoryCreatedEvent event = modelMapperService.forResponse().map(car, InventoryCreatedEvent.class);
		producer.sendMessage(event);
	}

	private void updateMongo(UpdateCarRequest request, String id) {
		checkIfCarNotExistsById(id);
		Car car = carRepository.findById(id).orElseThrow();
		car.getModel().setId(request.getModelId());
		car.getModel().getBrand().setId(car.getModel().getBrand().getId());
		car.setState(car.getState());
		car.setPlate(request.getPlate());
		car.setModelYear(request.getModelYear());

		CarUpdatedEvent event = modelMapperService.forResponse().map(car, CarUpdatedEvent.class);
		producer.sendMessage(event);
	}

	private void deleteMongo(String id) {
		CarDeletedEvent event = new CarDeletedEvent();
		event.setCarId(id);
		producer.sendMessage(event);
	}

	private void checkIfCarExistsByPlate(String plate) {
		if (this.carRepository.findByPlate(plate).isPresent()) {
			throw new BusinessException("CAR.EXISTS");
		}
	}

	private void checkIfCarNotExistsById(String id) {
		if (!this.carRepository.findById(id).isPresent()) {
			throw new BusinessException("CAR.NOT.EXISTS");
		}
	}

	private void checkIfCarNotExistsByPlate(String plate) {
		if (!this.carRepository.findByPlate(plate).isPresent()) {
			throw new BusinessException("CAR.NOT.EXISTS");
		}
	}

	private void checkIfModelNotExistsById(String modelId) {
		if (this.modelService.getById(modelId) == null) {
			throw new BusinessException("MODEL.ID.NOT.EXISTS");
		}
	}

}
