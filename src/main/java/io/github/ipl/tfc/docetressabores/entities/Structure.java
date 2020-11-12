package io.github.ipl.tfc.docetressabores.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllStructures",
		query = "SELECT s FROM Structure s"
	)
})
@Table(name = "STRUCTURES")
public class Structure {
	// TODO: documentation
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
	@ManyToMany private List<Variant> variants;

	public Structure() {
		variants = new ArrayList<>();
	}

	public Structure(
		@NotNull List<Variant> variants
	) {
		this.variants = variants;
	}


	// getters
	public int getId() {
		return id;
	}

	public List<Variant> getVariants() {
		return variants;
	}


	// setters
	public void setId(int id) {
		this.id = id;
	}

	public void setVariants(@NotNull List<Variant> variants) {
		this.variants = variants;
	}


	public void addVariant(@NotNull Variant variant) {
		variants.add(variant);
	}

	public void removeVariant(@NotNull Variant variant) {
		variants.remove(variant);
	}
}
