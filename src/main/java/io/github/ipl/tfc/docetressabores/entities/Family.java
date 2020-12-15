package io.github.ipl.tfc.docetressabores.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllFamilies",
		query = "SELECT f FROM Family f ORDER BY f.id"
	)
})
@Table(
	name = "FAMILIES",
	uniqueConstraints = @UniqueConstraint(
		columnNames = {
			"NAME"
		}
	)
)
public class Family {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
	@NotEmpty @NotBlank private String name;
	@OneToMany(mappedBy = "family", cascade = CascadeType.REMOVE) private List<Product> products;
	@ManyToOne @JoinColumn(name = "MATERIAL_ID") private Material material;

	public Family() {
		this.products = new ArrayList<>();
	}

	public Family(
		Material material,
		@NotEmpty @NotBlank String name
	) {
		this.material = material;
		this.name = name.toUpperCase();
	}


	// getters
	public int getId() {
		return id;
	}

	public Material getMaterial() {
		return material;
	}

	public String getName() {
		return name;
	}

	public List<Product> getProducts() {
		return products;
	}


	// setters
	public void setId(int id) {
		this.id = id;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProducts(@NotNull List<Product> products) {
		this.products = products;
	}


	public void addProduct(@NotNull Product product) {
		this.products.add(product);
	}

	public void removeProduct(@NotNull Product product) {
		this.products.remove(product);
	}
}
