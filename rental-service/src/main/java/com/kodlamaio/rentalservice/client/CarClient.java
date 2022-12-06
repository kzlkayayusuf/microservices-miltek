package com.kodlamaio.rentalservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kodlamaio.common.utilities.results.Result;

import feign.Headers;

@FeignClient(value = "carclient" , url = "http://localhost:7017" )
public interface CarClient {
	@RequestMapping(method = RequestMethod.GET,value = "/stock/api/cars/checkCarAvailable/{id}")
	@Headers(value = "Content-Type: application/json")
	Result checkCarAvailable(@PathVariable String id);
}
