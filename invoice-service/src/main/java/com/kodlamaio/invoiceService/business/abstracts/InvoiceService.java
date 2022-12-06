package com.kodlamaio.invoiceService.business.abstracts;

import java.util.List;

import com.kodlamaio.common.utilities.results.DataResult;
import com.kodlamaio.common.utilities.results.Result;
import com.kodlamaio.invoiceService.business.requests.create.CreateInvoiceRequest;
import com.kodlamaio.invoiceService.business.requests.update.UpdateInvoiceRequest;
import com.kodlamaio.invoiceService.business.responses.create.CreateInvoiceResponse;
import com.kodlamaio.invoiceService.business.responses.get.GetAllInvoicesResponse;
import com.kodlamaio.invoiceService.business.responses.get.GetInvoiceResponse;
import com.kodlamaio.invoiceService.business.responses.update.UpdateInvoiceResponse;
import com.kodlamaio.invoiceService.entities.Invoice;

public interface InvoiceService {

	DataResult<List<GetAllInvoicesResponse>> getAll();

	DataResult<GetInvoiceResponse> getById(String id);

	DataResult<CreateInvoiceResponse> add(CreateInvoiceRequest request);

	DataResult<UpdateInvoiceResponse> update(UpdateInvoiceRequest request);

	Result deleteById(String id);

	Result createInvoice(Invoice invoice);
}
