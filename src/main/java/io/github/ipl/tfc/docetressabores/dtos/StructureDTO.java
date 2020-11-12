package io.github.ipl.tfc.docetressabores.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StructureDTO implements Serializable {
	private int id;
	private List<VariantDTO> variants;

	public StructureDTO() {
		variants = new ArrayList<>();
	}

	public StructureDTO(int id, List<VariantDTO> variants) {
		this.id = id;
		this.variants = variants;
	}


	// getters
	public int getId() {
		return id;
	}

	public List<VariantDTO> getVariants() {
		return variants;
	}


	// setters
	public void setId(int id) {
		this.id = id;
	}

	public void setVariants(List<VariantDTO> variants) {
		this.variants = variants;
	}


	public void addVariant(VariantDTO variant) {
		this.variants.add(variant);
	}

	public void removeVariant(VariantDTO variant) {
		this.variants.remove(variant);
	}
}
