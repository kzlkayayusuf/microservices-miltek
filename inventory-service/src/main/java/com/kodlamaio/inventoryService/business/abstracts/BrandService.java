package com.kodlamaio.inventoryService.business.abstracts;

import java.util.List;

import com.kodlamaio.inventoryService.business.requests.create.CreateBrandRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateBrandResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllBrandsResponse;

public interface BrandService {
	List<GetAllBrandsResponse> getAll();

	CreateBrandResponse add(CreateBrandRequest createBrandRequest);
}
