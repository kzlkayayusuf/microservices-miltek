package com.kodlamaio.rentalservice.business.concretes;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.PaymentCreatedEvent;
import com.kodlamaio.common.events.rentals.RentalCreatedEvent;
import com.kodlamaio.common.events.rentals.RentalUpdatedEvent;
import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.common.utilities.results.SuccessDataResult;
import com.kodlamaio.common.utilities.results.SuccessResult;
import com.kodlamaio.rentalservice.business.abstracts.RentalService;
import com.kodlamaio.rentalservice.business.constants.Messages;
import com.kodlamaio.rentalservice.business.requests.create.CreateRentalRequest;
import com.kodlamaio.rentalservice.business.requests.update.UpdateRentalRequest;
import com.kodlamaio.rentalservice.business.responses.create.CreateRentalResponse;
import com.kodlamaio.rentalservice.business.responses.get.GetAllRentalsResponse;
import com.kodlamaio.rentalservice.business.responses.get.GetRentalResponse;
import com.kodlamaio.rentalservice.business.responses.update.UpdateRentalResponse;
import com.kodlamaio.rentalservice.client.CarClient;
import com.kodlamaio.rentalservice.client.PaymentClient;
import com.kodlamaio.rentalservice.dataAccess.RentalRepository;
import com.kodlamaio.rentalservice.entities.Rental;
import com.kodlamaio.rentalservice.kafka.RentalCreatedProducer;
import com.kodlamaio.rentalservice.kafka.RentalUpdatedProducer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RentalManager implements RentalService {

	private RentalRepository rentalRepository;
	private ModelMapperService modelMapperService;
	private RentalCreatedProducer rentalCreatedProducer;
	private RentalUpdatedProducer rentalUpdatedProducer;
	private CarClient carClient;
	private PaymentClient paymentClient;

	@Override
	public DataResult<List<GetAllRentalsResponse>> getAll() {
		List<Rental> rentals = rentalRepository.findAll();
		List<GetAllRentalsResponse> responses = rentals.stream()
				.map(rental -> modelMapperService.forResponse().map(rental, GetAllRentalsResponse.class)).toList();
		return new SuccessDataResult<List<GetAllRentalsResponse>>(responses, Messages.RentalListed);

	}

	@Override
	public DataResult<CreateRentalResponse> add(CreateRentalRequest createRentalRequest) {
		carClient.checkCarAvailable(createRentalRequest.getCarId());
		checkIfRentalExistsByCarId(createRentalRequest.getCarId());

		Rental rental = modelMapperService.forRequest().map(createRentalRequest, Rental.class);
		rental.setId(UUID.randomUUID().toString());
		rental.setTotalPrice(createRentalRequest.getDailyPrice() * createRentalRequest.getRentedForDays());
		Rental rentalCreated = this.rentalRepository.save(rental);

		RentalCreatedEvent rentalCreatedEvent = new RentalCreatedEvent();
		rentalCreatedEvent.setCarId(rentalCreated.getCarId());
		rentalCreatedEvent.setMessage(Messages.RentalCreated);

		PaymentCreatedEvent paymentCreatedEvent = new PaymentCreatedEvent();
		paymentCreatedEvent.setRentalId(rentalCreated.getId());
		paymentCreatedEvent.setCardNo(createRentalRequest.getCardNo());
		paymentCreatedEvent.setCardHolder(createRentalRequest.getCardHolder());
		paymentCreatedEvent.setCardBalance(createRentalRequest.getCardBalance());
		paymentCreatedEvent.setTotalPrice(rentalCreated.getTotalPrice());

		this.rentalCreatedProducer.sendMessage(rentalCreatedEvent);
		paymentClient.checkBalanceEnough(paymentCreatedEvent.getCardBalance(), paymentCreatedEvent.getTotalPrice());
		rentalRepository.save(rental);
		rentalCreatedProducer.sendMessage(rentalCreatedEvent);

		CreateRentalResponse response = modelMapperService.forResponse().map(rental, CreateRentalResponse.class);
		return new SuccessDataResult<CreateRentalResponse>(response, Messages.RentalCreated);

	}

	@Override
	public DataResult<UpdateRentalResponse> update(UpdateRentalRequest updateRentalRequest) {
		carClient.checkCarAvailable(updateRentalRequest.getCarId());
		checkIfRentalNotExistsById(updateRentalRequest.getId());

		RentalUpdatedEvent rentalUpdatedEvent = new RentalUpdatedEvent();
		Rental rental = rentalRepository.findById(updateRentalRequest.getId()).get();
		rentalUpdatedEvent.setOldCarId(rental.getCarId());

		rental.setCarId(updateRentalRequest.getCarId());
		rental.setDailyPrice(updateRentalRequest.getDailyPrice());
		rental.setRentedForDays(updateRentalRequest.getRentedForDays());
		rental.setTotalPrice(updateRentalRequest.getDailyPrice() * updateRentalRequest.getRentedForDays());

		Rental updatedRental = rentalRepository.save(rental);

		rentalUpdatedEvent.setNewCarId(updatedRental.getCarId());
		rentalUpdatedEvent.setMessage(Messages.RentalUpdated);
		rentalUpdatedProducer.sendMessage(rentalUpdatedEvent);

		UpdateRentalResponse response = modelMapperService.forResponse().map(rental, UpdateRentalResponse.class);
		return new SuccessDataResult<UpdateRentalResponse>(response, Messages.RentalUpdated);
	}

	@Override
	public DataResult<GetRentalResponse> getById(String id) {
		checkIfRentalNotExistsById(id);
		Rental rental = this.rentalRepository.findById(id).get();
		GetRentalResponse response = modelMapperService.forResponse().map(rental, GetRentalResponse.class);
		return new SuccessDataResult<GetRentalResponse>(response);
	}

	@Override
	public Result deleteById(String id) {
		checkIfRentalNotExistsById(id);
		this.rentalRepository.deleteById(id);

		return new SuccessResult(Messages.RentalDeleted);

	}

	private void checkIfRentalExistsByCarId(String id) {
		if (this.rentalRepository.findByCarId(id).isPresent()) {
			throw new BusinessException("RENTAL.EXISTS");
		}
	}

	private void checkIfRentalNotExistsById(String id) {
		if (!this.rentalRepository.findById(id).isPresent()) {
			throw new BusinessException("RENTAL.NOT.EXISTS");
		}
	}

}
