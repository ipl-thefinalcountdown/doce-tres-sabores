package io.github.ipl.tfc.docetressabores.entities.structures;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import io.github.ipl.tfc.docetressabores.entities.Material;
import io.github.ipl.tfc.docetressabores.entities.Variant;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllStructures",
		query = "SELECT s FROM Structure s "
			+ "ORDER BY s.id"
	),
	@NamedQuery(
		name = "getStructuresFilteredByMaterialId",
		query = "SELECT s FROM Structure s "
			+ "WHERE s.material.id = :materialId "
			+ "ORDER BY s.id"
	)
})
@Table(name = "STRUCTURES")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Structure {
	// TODO: documentation
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) protected int id;
	@ManyToMany protected List<Variant> variants;
	@ManyToOne @JoinColumn(name = "MATERIAL_TYPE") protected Material material;
	protected int beamAmount;
	protected int beamLength;
	protected int beamImposedLoad;

	public Structure() {
		variants = new ArrayList<>();
	}

	public Structure(
		Material material,
		int beamAmount,
		int beamLength,
		int beamImposedLoad,
		@NotNull List<Variant> variants
	) {
		this.material = material;
		this.beamAmount = beamAmount;
		this.beamLength = beamLength;
		this.beamImposedLoad = beamImposedLoad;
		this.variants = variants;
	}


	// getters
	public int getId() {
		return id;
	}

	public Material getMaterial() {
		return material;
	}

	public int getBeamAmount() {
		return beamAmount;
	}

	public int getBeamLength() {
		return beamLength;
	}

	public int getBeamImposedLoad() {
		return beamImposedLoad;
	}

	public List<Variant> getVariants() {
		return variants;
	}


	// setters
	public void setId(int id) {
		this.id = id;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public void setBeamAmount(int beamAmount) {
		this.beamAmount = beamAmount;
	}

	public void setBeamLength(int beamLength) {
		this.beamLength = beamLength;
	}

	public void setBeamImposedLoad(int beamImposedLoad) {
		this.beamImposedLoad = beamImposedLoad;
	}

	public void setVariants(List<Variant> variants) {
		this.variants = variants;
	}


	public void addVariant(Variant variant) {
		variants.add(variant);
	}

	public void removeVariant(Variant variant) {
		variants.remove(variant);
	}
}