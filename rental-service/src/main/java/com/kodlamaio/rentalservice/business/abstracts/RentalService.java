package com.kodlamaio.rentalservice.business.abstracts;

import java.util.List;

import com.kodlamaio.rentalservice.business.requests.create.CreateRentalRequest;
import com.kodlamaio.rentalservice.business.requests.update.UpdateRentalRequest;
import com.kodlamaio.rentalservice.business.responses.create.CreateRentalResponse;
import com.kodlamaio.rentalservice.business.responses.get.GetAllRentalsResponse;
import com.kodlamaio.rentalservice.business.responses.get.GetRentalResponse;
import com.kodlamaio.rentalservice.business.responses.update.UpdateRentalResponse;

public interface RentalService {
	List<GetAllRentalsResponse> getAll();

	CreateRentalResponse add(CreateRentalRequest createRentalRequest);

	UpdateRentalResponse update(UpdateRentalRequest updateRentalRequest);

	GetRentalResponse getById(String id);

	void deleteById(String id);

}
