package com.kodlamaio.common.events.inventories.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelUpdatedEvent {

	private String id;
	private String name;
	private String brandId;
}
