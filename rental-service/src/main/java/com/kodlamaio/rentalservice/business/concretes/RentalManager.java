package com.kodlamaio.rentalservice.business.concretes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.PaymentCreatedEvent;
import com.kodlamaio.common.events.RentalCreatedEvent;
import com.kodlamaio.common.events.RentalUpdatedEvent;
import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentalservice.business.abstracts.RentalService;
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
	public List<GetAllRentalsResponse> getAll() {
		List<Rental> rentals = this.rentalRepository.findAll();

		List<GetAllRentalsResponse> response = rentals.stream()
				.map(rental -> this.modelMapperService.forResponse().map(rental, GetAllRentalsResponse.class))
				.collect(Collectors.toList());

		return response;
	}

	@Override
	public CreateRentalResponse add(CreateRentalRequest createRentalRequest) {
		carClient.checkCarAvailable(createRentalRequest.getCarId());
		checkIfRentalExistsByCarId(createRentalRequest.getCarId());

		Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
		rental.setId(UUID.randomUUID().toString());
		rental.setTotalPrice(createRentalRequest.getDailyPrice() * createRentalRequest.getRentedForDays());
		Rental rentalCreated = this.rentalRepository.save(rental);

		RentalCreatedEvent rentalCreatedEvent = new RentalCreatedEvent();
		rentalCreatedEvent.setCarId(rentalCreated.getCarId());
		rentalCreatedEvent.setMessage("Rental Created");

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

		CreateRentalResponse createRentalResponse = this.modelMapperService.forResponse().map(rental,
				CreateRentalResponse.class);
		return createRentalResponse;
	}

	@Override
	public UpdateRentalResponse update(UpdateRentalRequest updateRentalRequest) {
		checkIfRentalNotExistsById(updateRentalRequest.getId());

		Rental rental = this.rentalRepository.findById(updateRentalRequest.getId()).get();
		RentalUpdatedEvent rentalUpdatedEvent = new RentalUpdatedEvent();
		rentalUpdatedEvent.setOldCarId(rental.getCarId());

		rental.setCarId(updateRentalRequest.getCarId());
		rental.setDailyPrice(updateRentalRequest.getDailyPrice());
		rental.setRentedForDays(updateRentalRequest.getRentedForDays());
		rental.setTotalPrice(updateRentalRequest.getDailyPrice() * updateRentalRequest.getRentedForDays());

		Rental updatedRental = rentalRepository.save(rental);

		rentalUpdatedEvent.setNewCarId(updatedRental.getCarId());
		rentalUpdatedEvent.setMessage("Rental Updated");
		rentalUpdatedProducer.sendMessage(rentalUpdatedEvent);

		UpdateRentalResponse updateRentalResponse = this.modelMapperService.forResponse().map(updatedRental,
				UpdateRentalResponse.class);
		return updateRentalResponse;
	}

	@Override
	public GetRentalResponse getById(String id) {
		checkIfRentalNotExistsById(id);
		Rental rental = this.rentalRepository.findById(id).get();
		GetRentalResponse rentalResponse = this.modelMapperService.forResponse().map(rental, GetRentalResponse.class);
		return rentalResponse;
	}

	@Override
	public void deleteById(String id) {
		checkIfRentalNotExistsById(id);
		this.rentalRepository.deleteById(id);

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
