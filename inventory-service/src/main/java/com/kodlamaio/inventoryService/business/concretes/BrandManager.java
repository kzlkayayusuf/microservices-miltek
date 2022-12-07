package com.kodlamaio.inventoryService.business.concretes;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.events.inventories.brands.BrandDeletedEvent;
import com.kodlamaio.common.events.inventories.brands.BrandUpdatedEvent;
import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.common.utilities.results.SuccessDataResult;
import com.kodlamaio.common.utilities.results.SuccessResult;
import com.kodlamaio.inventoryService.business.abstracts.BrandService;
import com.kodlamaio.inventoryService.business.constants.Messages;
import com.kodlamaio.inventoryService.business.requests.create.CreateBrandRequest;
import com.kodlamaio.inventoryService.business.requests.update.UpdateBrandRequest;
import com.kodlamaio.inventoryService.business.responses.create.CreateBrandResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetAllBrandsResponse;
import com.kodlamaio.inventoryService.business.responses.get.GetBrandResponse;
import com.kodlamaio.inventoryService.business.responses.update.UpdateBrandResponse;
import com.kodlamaio.inventoryService.dataAccess.BrandRepository;
import com.kodlamaio.inventoryService.entities.Brand;
import com.kodlamaio.inventoryService.kafka.producers.InventoryProducer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BrandManager implements BrandService {

	private BrandRepository brandRepository;
	private ModelMapperService modelMapperService;
	private final InventoryProducer producer;

	@Override
	public DataResult<List<GetAllBrandsResponse>> getAll() {
		List<Brand> brands = brandRepository.findAll();
		List<GetAllBrandsResponse> responses = brands.stream()
				.map(brand -> modelMapperService.forResponse().map(brand, GetAllBrandsResponse.class)).toList();
		return new SuccessDataResult<List<GetAllBrandsResponse>>(responses, Messages.BrandListed);
	}

	@Override
	public DataResult<CreateBrandResponse> add(CreateBrandRequest createBrandRequest) {
		checkIfBrandExistsByName(createBrandRequest.getName());
		Brand brand = modelMapperService.forRequest().map(createBrandRequest, Brand.class);
		brand.setId(UUID.randomUUID().toString());
		brandRepository.save(brand);

		CreateBrandResponse createBrandResponse = modelMapperService.forResponse().map(brand,
				CreateBrandResponse.class);
		return new SuccessDataResult<CreateBrandResponse>(createBrandResponse, Messages.BrandAdded);
	}

	@Override
	public DataResult<UpdateBrandResponse> update(UpdateBrandRequest updateBrandRequest) {
		checkIfBrandNotExistsById(updateBrandRequest.getId());
		Brand brand = modelMapperService.forRequest().map(updateBrandRequest, Brand.class);
		brandRepository.save(brand);

		updateMongo(brand.getId(), brand.getName());

		UpdateBrandResponse response = modelMapperService.forResponse().map(brand, UpdateBrandResponse.class);
		return new SuccessDataResult<UpdateBrandResponse>(response, Messages.BrandUpdated);
	}

	@Override
	public DataResult<GetBrandResponse> getById(String id) {
		checkIfBrandNotExistsById(id);
		Brand brand = brandRepository.findById(id).get();
		GetBrandResponse response = modelMapperService.forResponse().map(brand, GetBrandResponse.class);
		return new SuccessDataResult<GetBrandResponse>(response);
	}

	@Override
	public DataResult<GetBrandResponse> getByName(String name) {
		checkIfBrandNotExistsByName(name);
		Brand brand = brandRepository.findByName(name).get();
		GetBrandResponse responses = modelMapperService.forResponse().map(brand, GetBrandResponse.class);
		return new SuccessDataResult<GetBrandResponse>(responses, Messages.BrandListed);
	}

	@Override
	public Result deleteById(String id) {
		checkIfBrandNotExistsById(id);
		this.brandRepository.deleteById(id);

		deleteMongo(id);

		return new SuccessResult(Messages.BrandDeleted);

	}

	private void updateMongo(String id, String name) {
		BrandUpdatedEvent event = new BrandUpdatedEvent();
		event.setId(id);
		event.setName(name);
		producer.sendMessage(event);
	}

	private void deleteMongo(String id) {
		BrandDeletedEvent event = new BrandDeletedEvent();
		event.setBrandId(id);
		producer.sendMessage(event);
	}

	private void checkIfBrandExistsByName(String name) {
		if (this.brandRepository.findByName(name).isPresent()) {
			throw new BusinessException("BRAND.EXISTS");
		}
	}

	private void checkIfBrandNotExistsByName(String name) {
		if (!this.brandRepository.findByName(name).isPresent()) {
			throw new BusinessException("BRAND.NOT.EXISTS");
		}
	}

	private void checkIfBrandNotExistsById(String id) {
		if (!this.brandRepository.findById(id).isPresent()) {
			throw new BusinessException("BRAND.NOT.EXISTS");
		}
	}

}
