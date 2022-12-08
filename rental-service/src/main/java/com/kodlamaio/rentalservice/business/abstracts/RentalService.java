package com.kodlamaio.rentalservice.business.abstracts;

import java.util.List;

import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.rentalservice.business.requests.create.CreateRentalRequest;
import com.kodlamaio.rentalservice.business.requests.update.UpdateRentalRequest;
import com.kodlamaio.rentalservice.business.responses.create.CreateRentalResponse;
import com.kodlamaio.rentalservice.business.responses.get.GetAllRentalsResponse;
import com.kodlamaio.rentalservice.business.responses.get.GetRentalResponse;
import com.kodlamaio.rentalservice.business.responses.update.UpdateRentalResponse;

public interface RentalService {

	DataResult<List<GetAllRentalsResponse>> getAll();

	DataResult<CreateRentalResponse> add(CreateRentalRequest createRentalRequest);

	DataResult<UpdateRentalResponse> update(UpdateRentalRequest updateRentalRequest);

	DataResult<GetRentalResponse> getById(String id);

	Result deleteById(String id);
}
