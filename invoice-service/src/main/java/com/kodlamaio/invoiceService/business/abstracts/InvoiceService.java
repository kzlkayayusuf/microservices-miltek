package com.kodlamaio.invoiceService.business.abstracts;

import java.util.List;

import com.kodlamaio.invoiceService.business.requests.create.CreateInvoiceRequest;
import com.kodlamaio.invoiceService.business.requests.update.UpdateInvoiceRequest;
import com.kodlamaio.invoiceService.business.responses.create.CreateInvoiceResponse;
import com.kodlamaio.invoiceService.business.responses.get.GetAllInvoicesResponse;
import com.kodlamaio.invoiceService.business.responses.get.GetInvoiceResponse;
import com.kodlamaio.invoiceService.business.responses.update.UpdateInvoiceResponse;
import com.kodlamaio.invoiceService.entities.Invoice;

public interface InvoiceService {

	List<GetAllInvoicesResponse> getAll();

	GetInvoiceResponse getById(String id);

	CreateInvoiceResponse add(CreateInvoiceRequest request);

	UpdateInvoiceResponse update(UpdateInvoiceRequest request, String id);

	void deleteById(String id);

	void createInvoice(Invoice invoice);
}
