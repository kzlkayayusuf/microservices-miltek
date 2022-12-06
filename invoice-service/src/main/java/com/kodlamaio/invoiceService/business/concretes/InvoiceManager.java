package com.kodlamaio.invoiceService.business.concretes;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.invoiceService.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceService.business.requests.create.CreateInvoiceRequest;
import com.kodlamaio.invoiceService.business.requests.update.UpdateInvoiceRequest;
import com.kodlamaio.invoiceService.business.responses.create.CreateInvoiceResponse;
import com.kodlamaio.invoiceService.business.responses.get.GetAllInvoicesResponse;
import com.kodlamaio.invoiceService.business.responses.get.GetInvoiceResponse;
import com.kodlamaio.invoiceService.business.responses.update.UpdateInvoiceResponse;
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
		List<Invoice> invoices = invoiceRepository.findAll();
		List<GetAllInvoicesResponse> response = invoices.stream()
				.map(invoice -> mapperService.forResponse().map(invoice, GetAllInvoicesResponse.class)).toList();

		return response;
	}

	@Override
	public GetInvoiceResponse getById(String id) {
		checkIfInvoiceExists(id);
		Invoice invoice = invoiceRepository.findById(id).orElseThrow();
		GetInvoiceResponse response = mapperService.forResponse().map(invoice, GetInvoiceResponse.class);

		return response;
	}

	@Override
	public CreateInvoiceResponse add(CreateInvoiceRequest request) {
		Invoice invoice = mapperService.forRequest().map(request, Invoice.class);
		invoice.setId(UUID.randomUUID().toString());
		invoiceRepository.save(invoice);
		CreateInvoiceResponse response = mapperService.forResponse().map(invoice, CreateInvoiceResponse.class);

		return response;
	}

	@Override
	public UpdateInvoiceResponse update(UpdateInvoiceRequest request, String id) {
		checkIfInvoiceExists(id);
		Invoice invoice = mapperService.forRequest().map(request, Invoice.class);
		invoice.setId(id);
		invoiceRepository.save(invoice);
		UpdateInvoiceResponse response = mapperService.forResponse().map(invoice, UpdateInvoiceResponse.class);

		return response;
	}

	@Override
	public void deleteById(String id) {
		checkIfInvoiceExists(id);
		invoiceRepository.deleteById(id);
	}

	@Override
	public void createInvoice(Invoice invoice) {
		invoice.setId(UUID.randomUUID().toString());
		invoiceRepository.save(invoice);
	}

	private void checkIfInvoiceExists(String id) {
		if (!invoiceRepository.existsById(id)) {
			throw new BusinessException("INVOICE_NOT_FOUND");
		}
	}
}
