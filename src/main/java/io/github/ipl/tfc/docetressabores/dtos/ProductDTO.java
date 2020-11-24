package io.github.ipl.tfc.docetressabores.dtos;

import java.io.Serializable;
import java.util.*;

/**
 * Plain Java DTO to represent a Product
 * @see entities.Product
 */
public class ProductDTO implements Serializable
{
	private int id;

	/**
	 * Name of the product
	 */
	private String name;

	/**
	 * List of product variants
	 */
	private List<VariantDTO> variants;

	private int familyId;

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
	public ProductDTO(int id, String name, List<VariantDTO> variants, int familyId)
	{
		this.id = id;
		this.name = name;
		this.variants = variants;
		this.familyId = familyId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getFamilyId() {
		return familyId;
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

	public void setFamilyId(int familyId) {
		this.familyId = familyId;
	}
}
