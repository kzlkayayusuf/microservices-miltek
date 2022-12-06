package com.kodlamaio.invoiceService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kodlamaio.common.dataAccess.GetCarResponse;

@FeignClient(value = "carclient", url = "http://localhost:7017/")
public interface CarClient {
	@RequestMapping(method = RequestMethod.GET, value = "stock/api/cars/getCarResponse/{id}")
	GetCarResponse getCarResponse(@PathVariable String id);
}
