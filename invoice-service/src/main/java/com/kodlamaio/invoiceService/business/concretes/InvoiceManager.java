package com.kodlamaio.invoiceService.business.concretes;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kodlamaio.common.utilities.exceptions.BusinessException;
import com.kodlamaio.common.utilities.mapping.ModelMapperService;
import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.common.utilities.results.SuccessDataResult;
import com.kodlamaio.common.utilities.results.SuccessResult;
import com.kodlamaio.invoiceService.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceService.business.constants.Messages;
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
	public DataResult<List<GetAllInvoicesResponse>> getAll() {
		List<Invoice> invoices = invoiceRepository.findAll();
		List<GetAllInvoicesResponse> response = invoices.stream()
				.map(invoice -> mapperService.forResponse().map(invoice, GetAllInvoicesResponse.class)).toList();

		return new SuccessDataResult<List<GetAllInvoicesResponse>>(response, Messages.InvoiceListed);
	}

	@Override
	public DataResult<GetInvoiceResponse> getById(String id) {
		checkIfInvoiceExists(id);
		Invoice invoice = invoiceRepository.findById(id).orElseThrow();
		GetInvoiceResponse response = mapperService.forResponse().map(invoice, GetInvoiceResponse.class);

		return new SuccessDataResult<GetInvoiceResponse>(response, Messages.InvoiceListedById);
	}

	@Override
	public DataResult<CreateInvoiceResponse> add(CreateInvoiceRequest request) {
		Invoice invoice = mapperService.forRequest().map(request, Invoice.class);
		invoice.setId(UUID.randomUUID().toString());
		invoiceRepository.save(invoice);
		CreateInvoiceResponse response = mapperService.forResponse().map(invoice, CreateInvoiceResponse.class);

		return new SuccessDataResult<CreateInvoiceResponse>(response, Messages.InvoiceCreated);
	}

	@Override
	public DataResult<UpdateInvoiceResponse> update(UpdateInvoiceRequest request) {
		checkIfInvoiceExists(request.getId());
		Invoice invoice = mapperService.forRequest().map(request, Invoice.class);
		invoiceRepository.save(invoice);
		UpdateInvoiceResponse response = mapperService.forResponse().map(invoice, UpdateInvoiceResponse.class);

		return new SuccessDataResult<UpdateInvoiceResponse>(response, Messages.InvoiceUpdated);
	}

	@Override
	public Result deleteById(String id) {
		checkIfInvoiceExists(id);
		invoiceRepository.deleteById(id);
		return new SuccessResult(Messages.InvoiceDeleted);
	}

	@Override
	public Result createInvoice(Invoice invoice) {
		invoice.setId(UUID.randomUUID().toString());
		invoiceRepository.save(invoice);
		return new SuccessResult(Messages.InvoiceCreated);
	}

	private void checkIfInvoiceExists(String id) {
		if (!invoiceRepository.existsById(id)) {
			throw new BusinessException("INVOICE_NOT_FOUND");
		}
	}
}
