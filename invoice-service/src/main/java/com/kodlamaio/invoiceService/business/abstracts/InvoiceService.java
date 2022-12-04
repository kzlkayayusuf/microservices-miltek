package com.kodlamaio.invoiceService.business.abstracts;

import java.util.List;

import com.kodlamaio.invoiceService.business.requests.CreateInvoiceRequest;
import com.kodlamaio.invoiceService.business.responses.CreateInvoiceResponse;
import com.kodlamaio.invoiceService.business.responses.GetAllInvoicesResponse;

public interface InvoiceService {

	List<GetAllInvoicesResponse> getAll();

	CreateInvoiceResponse add(CreateInvoiceRequest request);
}
