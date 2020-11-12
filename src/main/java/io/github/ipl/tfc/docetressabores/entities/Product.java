package io.github.ipl.tfc.docetressabores.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Product entity
 */
@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllProducts",
		query = "SELECT p FROM Product p ORDER BY p.name"
	)
})
@Table(name = "PRODUCTS")
public class Product {
	// TODO: change @Id to int and @GeneratedValue(strategy=GenerationType.IDENTITY)
	/**
	 * Primary key of the product
	 * This represents the name of the product
	 */
	@Id private String name;

	/**
	 * Relationship of product variants
	 * This represents a list of product variants
	 */
	@OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE) private List<Variant> variants;

	/**
	 * Default constructor for a Product entity
	 */
	public Product()
	{
		variants = new LinkedList<>();
	}

	/**
	 * Constructs a product entity with a given name
	 * @param name product name
	 */
	public Product(String name)
	{
		this.name = name;
		variants = new LinkedList<>();
	}

	/**
	 * Gets the product name
	 * @return product name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the product name
	 * @param name product name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the product variants
	 * @return list of the product variants
	 */
	public List<Variant> getVariants() {
		return variants;
	}

	/**
	 * Sets a list of product variants
	 * @param especimen list of product variants
	 */
	public void setVariants(List<Variant> especimen) {
		this.variants = especimen;
	}

	/**
	 * Add a variant to the list of product variants
	 * @param s a product variant
	 */
	public void addVariant(Variant s) {
		variants.add(s);
	}

	/**
	 * Removes a variant from the list of product variants
	 * @param s an existing product variant
	 */
	public void removeVariant(Variant s) {
		variants.remove(s);
	}

}
