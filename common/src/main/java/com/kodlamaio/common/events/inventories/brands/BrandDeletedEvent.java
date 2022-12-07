package com.kodlamaio.common.events.inventories.brands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandDeletedEvent {

	private String brandId;
}
