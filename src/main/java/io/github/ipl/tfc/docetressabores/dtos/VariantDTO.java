package io.github.ipl.tfc.docetressabores.dtos;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Plain Java DTO to represent a product Variant
 * @see entities.Variant
 */
public class VariantDTO implements Serializable
{

	/**
	 * This value represents the code of the variant
	 */
	private int code;

	/**
	 * This represents the product name where this variant is appliable.
	 */
	private int productId;

	/**
	 * Variant name
	 */
	private String name;

	/**
	 * Flexão positiva efetiva de uma secção transversal
	 * Valor medido em mm^3
	 * @see weff_n
	 */
	private double weff_p;

	/**
	 * Flexão negativa efetiva de uma secção transversal
	 * Valor medido em mm^3
	 * @see weff_p
	 */
	private double weff_n;

	/**
	 * Section area in mm^2
	 */
	private double ar;

	private double sigmaC;

	/**
	 * Value based on modulus of rigidity and section area
	 * @see ar
	 * @see G
	 */
	private double pp;

	/**
	 * Par L -> Mcr+
	 * L
	 *   Comprimento da barra
	 *   Valor medido em mm
	 *
	 * Mcr+
	 *   Valor crítico do Momento elástico de encurvadura lateral
	 *   Valor medido em kN.cm
	 *
	 * @see mcr_n
	 */
	private LinkedHashMap<Double,Double> mcr_p;

	/**
	 * Par L -> Mcr-
	 * L
	 *   Comprimento da barra
	 *   Valor medido em mm
	 *
	 * Mcr-
	 *   Valor crítico do Momento elástico de encurvadura lateral
	 *   Valor medido em kN.cm
	 *
	 * @see mcr_p
	 */
	private LinkedHashMap<Double,Double> mcr_n;


	/**
	 * Default constructor for a Variant DTO
	 */
	public VariantDTO()
	{
		this.mcr_p = new LinkedHashMap<Double,Double>();
		this.mcr_n = new LinkedHashMap<Double,Double>();
	}

	/**
	 * Constructs a product Variant DTO based on it's attributes
	 * @param code variant code
	 * @param productId associated product name
	 * @param name variant name
	 * @param weff_p Weff+
	 * @param weff_n Weff-
	 * @param ar Area
	 * @param sigmaC sigma C
	 * @param pp PP
	 * @param mcr_p Mcr+
	 * @param mcr_n Mcr-
	 */
	public VariantDTO(
		int code,
		int productId,
		String name,
		double weff_p,
		double weff_n,
		double ar,
		double sigmaC,
		double pp,
		LinkedHashMap<Double,Double> mcr_p,
		LinkedHashMap<Double,Double> mcr_n)
	{
		this.code = code;
		this.productId = productId;
		this.name = name;
		this.weff_p = weff_p;
		this.weff_n = weff_n;
		this.ar = ar;
		this.sigmaC = sigmaC;
		this.pp = pp;
		this.mcr_p = mcr_p;
		this.mcr_n = mcr_n;
	}

	/**
	 * Sets the variant code
	 * @param code variant code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Sets the variant code
	 * @return variant code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Sets the associated product name
	 * @param productId product name
	 */
	public void setProductName(int productId) {
		this.productId = productId;
	}

	/**
	 * Gets the associated product name
	 * @return product name
	 */
	public int getProductName() {
		return productId;
	}

	/**
	 * Gets the variant name
	 * @return variant name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the variant name
	 * @param name variant name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
