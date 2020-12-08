package io.github.ipl.tfc.docetressabores.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.LinkedHashMap;

/**
 * Product variant entity
 * @see Product
 */
@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllVariants",
		query = "SELECT v FROM Variant v ORDER BY v.name"
	)
})
@Table(name = "VARIANTS")
public class Variant {

	/**
	 * Constant modulus of rigidity
	 * This value represents the shear modulus
	 * of cast steel.
	 */
	public static final double G = 78.5;

	/**
	 * Primary key of the product variant
	 * This represents the id of the variant
	 */
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;

	/**
	 * Relationship to the product
	 * This represents the product where this variant is appliable.
	 */
	@ManyToOne @JoinColumn(name = "PRODUCT_ID") @NotNull private Product product;

	// FIXME: unique constraint
	/**
	 * Variant name
	 *
	 * Note: this value can't be null on the database
	 */
	@NotNull private String name;

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
	 *   Support beam length (mm)
	 *
	 * Mcr+
	 *   Critical lateral curvature momentum value (kN.cm)
	 *
	 * Note: Store raw data in the database
	 *
	 * @see mcr_n
	 */
	@Lob private LinkedHashMap<Double,Double> mcr_p;

	/**
	 * Par L -> Mcr-
	 * L
	 *   Support beam length (mm)
	 *
	 * Mcr-
	 *   Critical lateral curvature momentum value (kN.cm)
	 *
	 * Note: Store raw data in the database
	 *
	 * @see mcr_p
	 */
	@Lob private LinkedHashMap<Double,Double> mcr_n;

	/**
	 * Default constructor for a Variant entity
	 */
	public Variant()
	{
		this.mcr_p = new LinkedHashMap<>();
		this.mcr_n = new LinkedHashMap<>();
	}

	public Variant(
		@NotNull Product product,
		@NotNull String name,
		double weff_p,
		double weff_n,
		double ar,
		double sigmaC
	) {
		this.product = product;
		this.name = name;
		this.weff_p = weff_p;
		this.weff_n = weff_n;
		this.ar = ar;
		this.sigmaC = sigmaC;
		this.pp = G * ar * Math.pow(10, -6);
		this.mcr_p = new LinkedHashMap<>();
		this.mcr_n = new LinkedHashMap<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getWeff_p() {
		return weff_p;
	}

	public void setWeff_p(double weff_p) {
		this.weff_p = weff_p;
	}

	public double getWeff_n() {
		return weff_n;
	}

	public void setWeff_n(double weff_n) {
		this.weff_n = weff_n;
	}

	public double getAr() {
		return ar;
	}

	public void setAr(double ar) {
		this.ar = ar;
	}

	public double getSigmaC() {
		return sigmaC;
	}

	public void setSigmaC(double sigmaC) {
		this.sigmaC = sigmaC;
	}

	public double getPp() {
		return pp;
	}

	public void setPp(double pp) {
		this.pp = pp;
	}

	public LinkedHashMap<Double, Double> getMcr_p() {
		return mcr_p;
	}

	public void setMcr_p(LinkedHashMap<Double, Double> mcr_p) {
		this.mcr_p = mcr_p;
	}

	public void addMcr_p(Double L, Double mcr_pValue){
		mcr_p.put(L, mcr_pValue);
	}

	public void removeMcr_p(Double LToRemove){
		mcr_p.remove(LToRemove);
	}

	public LinkedHashMap<Double, Double> getMcr_n() {
		return mcr_n;
	}

	public void setMcr_n(LinkedHashMap<Double, Double> mcr_n) {
		this.mcr_n = mcr_n;
	}

	public void addMcr_n(Double L, Double mcr_nValue){
		mcr_n.put(L, mcr_nValue);
	}

	public void removeMcr_n(Double LToRemove){
		mcr_n.remove(LToRemove);
	}


}
