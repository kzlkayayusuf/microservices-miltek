package com.kodlamaio.invoiceService;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ValidationException;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperManager;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.common.utilities.results.ErrorDataResult;

@SpringBootApplication
@EnableDiscoveryClient
@RestControllerAdvice
public class InvoiceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceServiceApplication.class, args);
	}

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	@Bean
	public ModelMapperService getModelMapperService(ModelMapper mapper) {
		return new ModelMapperManager(mapper);
	}

	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ErrorDataResult<Object> handleValidationException(
			MethodArgumentNotValidException methodArgumentNotValidException) {
		Map<String, String> validationErrors = new HashMap<String, String>();
		for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
			validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}

		ErrorDataResult<Object> errors = new ErrorDataResult<Object>(validationErrors, "VALIDATION.EXCEPTION");
		return errors;

	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDataResult<Object> handleValidationException(ValidationException exception) {
		return new ErrorDataResult<>(exception.getMessage(), "VALIDATION EXCEPTION");
	}

	@ExceptionHandler
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ErrorDataResult<Object> handleBusinessExceptions(BusinessException businessException) {
		ErrorDataResult<Object> errorDataResult = new ErrorDataResult<Object>(businessException.getMessage(),
				"BUSINESS.EXCEPTION");
		return errorDataResult;
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDataResult<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
		ErrorDataResult<Object> errorDataResult = new ErrorDataResult<>(exception.getMessage(),
				"DATA INTEGRITY EXCEPTION");
		return errorDataResult;
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorDataResult<Object> handleRuntimeEception(RuntimeException exception) {
		ErrorDataResult<Object> errorDataResult = new ErrorDataResult<>(exception.getMessage(), "RUNTIME EXCEPTION");

		return errorDataResult;
	}

}
