package com.kodlamaio.invoiceService.business.concretes;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.invoiceService.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceService.business.requests.CreateInvoiceRequest;
import com.kodlamaio.invoiceService.business.responses.CreateInvoiceResponse;
import com.kodlamaio.invoiceService.business.responses.GetAllInvoicesResponse;
import com.kodlamaio.invoiceService.dataAccess.InvoiceRepository;
import com.kodlamaio.invoiceService.entities.Invoice;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InvoiceManager implements InvoiceService {

	private final InvoiceRepository invoiceRepository;
	private final ModelMapperService mapperService;

	@Override
	public List<GetAllInvoicesResponse> getAll() {
		return invoiceRepository.findAll().stream()
				.map(i -> mapperService.forResponse().map(i, GetAllInvoicesResponse.class))
				.collect(Collectors.toList());
	}

	@Override
	public CreateInvoiceResponse add(CreateInvoiceRequest request) {
		Invoice invoice = mapperService.forRequest().map(request, Invoice.class);
		invoice.setId(UUID.randomUUID().toString());
		invoiceRepository.save(invoice);
		return mapperService.forResponse().map(invoice, CreateInvoiceResponse.class);
	}
}
