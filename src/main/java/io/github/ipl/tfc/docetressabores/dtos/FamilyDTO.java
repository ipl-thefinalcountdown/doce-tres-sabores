package io.github.ipl.tfc.docetressabores.dtos;

import java.util.ArrayList;
import java.util.List;

public class FamilyDTO {
	private int id;
	private String name;
	private List<ProductDTO> products;

	public FamilyDTO() {
		this.products = new ArrayList<>();
	}

	public FamilyDTO(int id, String name, List<ProductDTO> products) {
		this.id = id;
		this.name = name;
		this.products = products;
	}


	// getters
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<ProductDTO> getProducts() {
		return products;
	}


	// setters
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}
}
