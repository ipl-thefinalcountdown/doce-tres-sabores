package io.github.ipl.tfc.docetressabores.dtos;

import java.io.Serializable;
import java.util.*;

/**
 * Plain Java DTO to represent a Product
 * @see entities.Product
 */
public class ProductDTO implements Serializable
{
	/**
	 * Name of the product
	 */
	private String name;

	/**
	 * List of product variants
	 */
	private List<VariantDTO> variants;

	/**
	 * Default constructor for a Product DTO
	 */
	public ProductDTO()
	{
		this.variants = new ArrayList<>();
	}

	/**
	 * Construct a Product DTO based on it's attributes
	 * @param name product name
	 * @param variants product variants
	 */
	public ProductDTO(String name, List<VariantDTO> variants)
	{
		this.name = name;
		this.variants = variants;
	}

	/**
	 * Sets a product name
	 * @param name product name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets a product name
	 * @return product name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets a product variant
	 * @param variants product variant
	 */
	public void setVariants(List<VariantDTO> variants) {
		this.variants = variants;
	}

	/**
	 * Gets a product variant
	 * @return product variant
	 */
	public List<VariantDTO> getVariants() {
		return variants;
	}
}
