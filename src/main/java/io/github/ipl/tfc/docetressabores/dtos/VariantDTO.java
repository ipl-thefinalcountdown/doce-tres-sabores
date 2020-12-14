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
	 * This value represents the id of the variant
	 */
	private int id;

	/**
	 * This represents the product id where this variant is appliable.
	 */
	private Integer productId;

	/**
	 * This represents the product name where this variant is appliable.
	 */
	private String productName;

	/**
	 * Variant name
	 */
	private String name;

	/**
	 * Flexão positiva efetiva de uma secção transversal
	 * Valor medido em mm^3
	 * @see weff_n
	 */
	private Double weff_p;

	/**
	 * Flexão negativa efetiva de uma secção transversal
	 * Valor medido em mm^3
	 * @see weff_p
	 */
	private Double weff_n;

	/**
	 * Section area in mm^2
	 */
	private Double ar;

	private Double sigmaC;

	/**
	 * Value based on modulus of rigidity and section area
	 * @see ar
	 * @see G
	 */
	private Double pp;

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
		this.mcr_p = new LinkedHashMap<>();
		this.mcr_n = new LinkedHashMap<>();
	}

	/**
	 * Constructs a product Variant DTO based on it's attributes
	 * @param id variant id
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
		int id,
		Integer productId,
		String productName,
		String name,
		Double weff_p,
		Double weff_n,
		Double ar,
		Double sigmaC,
		Double pp,
		LinkedHashMap<Double,Double> mcr_p,
		LinkedHashMap<Double,Double> mcr_n)
	{
		this.id = id;
		this.productId = productId;
		this.productName = productName;
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
	 * Sets the variant id
	 * @param id variant id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Sets the variant id
	 * @return variant id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the associated product id
	 * @param productId product id
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	/**
	 * Gets the associated product id
	 * @return product id
	 */
	public Integer getProductId() {
		return productId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
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

	public Double getAr() {
		return ar;
	}

	public void setAr(Double ar) {
		this.ar = ar;
	}

	public LinkedHashMap<Double, Double> getMcr_n() {
		return mcr_n;
	}

	public LinkedHashMap<Double, Double> getMcr_p() {
		return mcr_p;
	}

	public Double getPp() {
		return pp;
	}

	public Double getSigmaC() {
		return sigmaC;
	}

	public Double getWeff_n() {
		return weff_n;
	}

	public Double getWeff_p() {
		return weff_p;
	}

	public void setMcr_n(LinkedHashMap<Double, Double> mcr_n) {
		this.mcr_n = mcr_n;
	}

	public void setMcr_p(LinkedHashMap<Double, Double> mcr_p) {
		this.mcr_p = mcr_p;
	}

	public void setPp(Double pp) {
		this.pp = pp;
	}

	public void setSigmaC(Double sigmaC) {
		this.sigmaC = sigmaC;
	}

	public void setWeff_n(Double weff_n) {
		this.weff_n = weff_n;
	}

	public void setWeff_p(Double weff_p) {
		this.weff_p = weff_p;
	}
}
