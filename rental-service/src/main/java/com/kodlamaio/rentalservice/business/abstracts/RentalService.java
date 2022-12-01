package com.kodlamaio.rentalservice.business.abstracts;

import java.util.List;

import com.kodlamaio.rentalservice.business.requests.create.CreateRentalRequest;
import com.kodlamaio.rentalservice.business.responses.create.CreateRentalResponse;
import com.kodlamaio.rentalservice.business.responses.get.GetAllRentalsResponse;


public interface RentalService {
	List<GetAllRentalsResponse> getAll();

	CreateRentalResponse add(CreateRentalRequest createRentalRequest);

}
