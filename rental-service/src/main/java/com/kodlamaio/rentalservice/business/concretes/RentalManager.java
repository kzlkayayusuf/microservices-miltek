package com.kodlamaio.rentalservice.business.concretes;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.dataAccess.CreateRentalPaymentRequest;
import com.kodlamaio.common.events.payments.PaymentReceivedEvent;
import com.kodlamaio.common.events.rentals.RentalCreatedEvent;
import com.kodlamaio.common.events.rentals.RentalDeletedEvent;
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
import com.kodlamaio.rentalservice.kafka.RentalProducer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RentalManager implements RentalService {

	private RentalRepository rentalRepository;
	private ModelMapperService modelMapperService;
	private RentalProducer rentalProducer;
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
		double totalPrice = createRentalRequest.getDailyPrice() * createRentalRequest.getRentedForDays();
		rental.setTotalPrice(totalPrice);

		CreateRentalPaymentRequest paymentRequest = new CreateRentalPaymentRequest();
		modelMapperService.forRequest().map(createRentalRequest.getPaymentRequest(), paymentRequest);
		paymentRequest.setPrice(totalPrice);

		paymentClient.checkIfPaymentSuccessful(paymentRequest);

		this.rentalRepository.save(rental);

		rentalCreatedEvent(rental);
		paymentReceivedEvent(createRentalRequest, rental, totalPrice);

		CreateRentalResponse response = modelMapperService.forResponse().map(rental, CreateRentalResponse.class);
		return new SuccessDataResult<CreateRentalResponse>(response, Messages.RentalCreated);

	}

	@Override
	public DataResult<UpdateRentalResponse> update(UpdateRentalRequest updateRentalRequest) {
		carClient.checkCarAvailable(updateRentalRequest.getCarId());
		checkIfRentalNotExistsById(updateRentalRequest.getId());

		Rental rental = modelMapperService.forRequest().map(updateRentalRequest, Rental.class);
		rentalUpdatedEvent(updateRentalRequest.getId(), rental);

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
		rentalDeletedEvent(id);
		RentalDeletedEvent event = new RentalDeletedEvent();
		event.setCarId(rentalRepository.findById(id).orElseThrow().getCarId());
		event.setMessage("Rental Deleted");
		rentalProducer.sendMessage(event);
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

	private void rentalCreatedEvent(Rental rental) {
		RentalCreatedEvent rentalCreatedEvent = new RentalCreatedEvent();
		rentalCreatedEvent.setCarId(rental.getCarId());
		rentalCreatedEvent.setMessage("Rental Created");
		rentalProducer.sendMessage(rentalCreatedEvent);
	}

	private void rentalUpdatedEvent(String id, Rental rental) {
		RentalUpdatedEvent rentalUpdatedEvent = new RentalUpdatedEvent();
		rental.setId(id);
		rentalUpdatedEvent.setOldCarId(rentalRepository.findById(id).orElseThrow().getCarId());
		rentalRepository.save(rental);
		rentalUpdatedEvent.setNewCarId(rental.getCarId());
		rentalUpdatedEvent.setMessage("Rental Updated");
		rentalProducer.sendMessage(rentalUpdatedEvent);
	}

	private void rentalDeletedEvent(String id) {
		RentalDeletedEvent event = new RentalDeletedEvent();
		event.setCarId(rentalRepository.findById(id).orElseThrow().getCarId());
		event.setMessage("Rental Deleted");
		rentalProducer.sendMessage(event);
	}

	private void paymentReceivedEvent(CreateRentalRequest request, Rental rental, double totalPrice) {
		PaymentReceivedEvent paymentReceivedEvent = new PaymentReceivedEvent();
		paymentReceivedEvent.setCarId(rental.getCarId());
		paymentReceivedEvent.setFullName(request.getPaymentRequest().getFullName());
		paymentReceivedEvent.setDailyPrice(request.getDailyPrice());
		paymentReceivedEvent.setTotalPrice(totalPrice);
		paymentReceivedEvent.setRentedForDays(request.getRentedForDays());
		paymentReceivedEvent.setRentedAt(rental.getDateStarted());
		paymentReceivedEvent.setBrandName(carClient.getCarResponse(rental.getCarId()).getBrandName());
		paymentReceivedEvent.setModelName(carClient.getCarResponse(rental.getCarId()).getModelName());
		paymentReceivedEvent.setModelYear(carClient.getCarResponse(rental.getCarId()).getModelYear());
		rentalProducer.sendMessage(paymentReceivedEvent);
	}

}
