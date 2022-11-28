package com.kodlamaio.discoveryServer.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="models")
public class Model {
	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="name")
	private String name;
	
	@ManyToOne
	@JoinColumn(name="brand_id")
	private Brand brand;
	
	@OneToMany(mappedBy="model")
	List<Car> cars;
}
