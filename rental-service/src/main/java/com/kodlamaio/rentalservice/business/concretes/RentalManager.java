package com.kodlamaio.rentalservice.business.concretes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.RentalCreatedEvent;
import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.rentalservice.business.abstracts.RentalService;
import com.kodlamaio.rentalservice.business.requests.create.CreateRentalRequest;
import com.kodlamaio.rentalservice.business.responses.create.CreateRentalResponse;
import com.kodlamaio.rentalservice.business.responses.get.GetAllRentalsResponse;
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
		//checkIfBrandExistsByCarId(createRentalRequest.getCarId());
		Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
		rental.setId(UUID.randomUUID().toString());
		rental.setTotalPrice(createRentalRequest.getDailyPrice() * createRentalRequest.getRentedForDays());
		Rental rentalCreated=this.rentalRepository.save(rental);
		
		RentalCreatedEvent rentalCreatedEvent = new RentalCreatedEvent();
		rentalCreatedEvent.setCarId(rentalCreated.getCarId());
		rentalCreatedEvent.setMessage("Rental Created");
		
		this.rentalProducer.sendMessage(rentalCreatedEvent);

		CreateRentalResponse createRentalResponse = this.modelMapperService.forResponse().map(rental,
				CreateRentalResponse.class);
		return createRentalResponse;
	}

	private void checkIfBrandExistsByCarId(String id) {
		if(this.rentalRepository.findByCarId(id).isPresent()) {
			throw new BusinessException("RENTAL.EXISTS");
		}
	}
	
}
