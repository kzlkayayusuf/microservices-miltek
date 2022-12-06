package com.kodlamaio.invoiceService.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kodlamaio.invoiceService.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceService.business.requests.create.CreateInvoiceRequest;
import com.kodlamaio.invoiceService.business.requests.update.UpdateInvoiceRequest;
import com.kodlamaio.invoiceService.business.responses.create.CreateInvoiceResponse;
import com.kodlamaio.invoiceService.business.responses.get.GetAllInvoicesResponse;
import com.kodlamaio.invoiceService.business.responses.get.GetInvoiceResponse;
import com.kodlamaio.invoiceService.business.responses.update.UpdateInvoiceResponse;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/invoices")
public class InvoicesController {
	private final InvoiceService invoiceService;

	@GetMapping
	public List<GetAllInvoicesResponse> getAll() {
		return invoiceService.getAll();
	}

	@GetMapping("/{id}")
	public GetInvoiceResponse getById(@PathVariable String id) {
		return invoiceService.getById(id);
	}

	@PostMapping
	public CreateInvoiceResponse add(@Valid @RequestBody CreateInvoiceRequest request) {
		return invoiceService.add(request);
	}

	@PutMapping("/{id}")
	public UpdateInvoiceResponse update(@Valid @RequestBody UpdateInvoiceRequest request, @PathVariable String id) {
		return invoiceService.update(request, id);
	}

	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable String id) {
		invoiceService.deleteById(id);
	}
}
