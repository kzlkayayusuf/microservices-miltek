package com.kodlamaio.invoiceService.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.invoiceService.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceService.business.requests.create.CreateInvoiceRequest;
import com.kodlamaio.invoiceService.business.requests.update.UpdateInvoiceRequest;
import com.kodlamaio.invoiceService.business.responses.create.CreateInvoiceResponse;
import com.kodlamaio.invoiceService.business.responses.get.GetAllInvoicesResponse;
import com.kodlamaio.invoiceService.business.responses.update.UpdateInvoiceResponse;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/invoices")
public class InvoicesController {
	private final InvoiceService invoiceService;

	@GetMapping
	public ResponseEntity<?> getAll() {
		DataResult<List<GetAllInvoicesResponse>> result = invoiceService.getAll();
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);

		}
		return ResponseEntity.badRequest().body(result);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) {
		Result result = invoiceService.getById(id);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@PostMapping
	public ResponseEntity<?> add(@Valid @RequestBody CreateInvoiceRequest request) {
		DataResult<CreateInvoiceResponse> result = invoiceService.add(request);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);

		}
		return ResponseEntity.badRequest().body(result);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody UpdateInvoiceRequest request) {
		DataResult<UpdateInvoiceResponse> result = invoiceService.update(request);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable String id) {
		Result result = invoiceService.deleteById(id);
		if (result.isSuccess()) {
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.badRequest().body(result);
	}
}
