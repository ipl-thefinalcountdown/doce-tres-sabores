package io.github.ipl.tfc.docetressabores.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StructureDTO implements Serializable {
	private int id;
	private int materialId;
	private String name;
	private Integer beamAmount;
	private Double beamLength;
	private Integer beamImposedLoad;
	private List<VariantDTO> variants;

	private Integer beamSpacing;

	private Integer maximumHeight;

	public StructureDTO() {
		variants = new ArrayList<>();
	}

	public StructureDTO(
		int id,
		int materialId,
		String name,
		Integer beamAmount,
		Double beamLength,
		Integer beamImposedLoad,
		Integer beamSpacing,
		Integer maximumHeight,
		List<VariantDTO> variants
	) {
		this.id = id;
		this.materialId = materialId;
		this.name = name;
		this.beamAmount = beamAmount;
		this.beamLength = beamLength;
		this.beamImposedLoad = beamImposedLoad;
		this.beamSpacing = beamSpacing;
		this.maximumHeight = maximumHeight;
		this.variants = variants;
	}


	// getters
	public int getId() {
		return id;
	}

	public int getMaterialId() {
		return materialId;
	}

	public String getName() {
		return name;
	}

	public Integer getBeamAmount() {
		return beamAmount;
	}

	public Double getBeamLength() {
		return beamLength;
	}

	public Integer getBeamImposedLoad() {
		return beamImposedLoad;
	}

	public List<VariantDTO> getVariants() {
		return variants;
	}

	public Integer getBeamSpacing() {
		return beamSpacing;
	}

	public Integer getMaximumHeight() {
		return maximumHeight;
	}


	// setters
	public void setId(int id) {
		this.id = id;
	}

	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setBeamAmount(Integer beamAmount) {
		this.beamAmount = beamAmount;
	}

	public void setBeamLength(Double beamLength) {
		this.beamLength = beamLength;
	}

	public void setBeamImposedLoad(Integer beamImposedLoad) {
		this.beamImposedLoad = beamImposedLoad;
	}

	public void setVariants(List<VariantDTO> variants) {
		this.variants = variants;
	}

	public void setBeamSpacing(Integer beamSpacing) {
		this.beamSpacing = beamSpacing;
	}

	public void setMaximumHeight(Integer maximumHeight) {
		this.maximumHeight = maximumHeight;
	}
}
