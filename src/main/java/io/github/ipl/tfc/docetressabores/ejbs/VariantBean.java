package io.github.ipl.tfc.docetressabores.ejbs;

import io.github.ipl.tfc.docetressabores.dtos.ProjectDTO;
import io.github.ipl.tfc.docetressabores.dtos.VariantDTO;
import io.github.ipl.tfc.docetressabores.entities.Product;
import io.github.ipl.tfc.docetressabores.entities.Variant;
import io.github.ipl.tfc.docetressabores.entities.structures.Structure;

import javax.ejb.Stateless;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.LinkedHashMap;

@Stateless
public class VariantBean {
	// TODO: documentation
	@PersistenceContext
	private EntityManager entityManager;

	public Variant create(
			int productId,
			String name,
			double weff_p, double weff_n,
			double ar, double sigmaC,
			LinkedHashMap<Double,Double> mcr_p, LinkedHashMap<Double,Double> mcr_n
	) {
		Product product = entityManager.find(Product.class, productId);

		if (product == null) {
			return null;
		} else {
			Variant variant = new Variant(product, name, weff_p, weff_n, ar, sigmaC);
			if(mcr_p != null) variant.setMcr_p(mcr_p);
			if(mcr_n != null) variant.setMcr_n(mcr_n);
			entityManager.persist(variant);
			return variant;
		}
	}

	public Variant create(
			int productId,
			String name,
			double weff_p, double weff_n,
			double ar, double sigmaC
	) {
		return create(productId, name, weff_p, weff_n, ar, sigmaC, null, null);
	}

	public Variant create(VariantDTO variantDTO)
	{
		return create(
				variantDTO.getProductId(), variantDTO.getName(),
				variantDTO.getWeff_p(), variantDTO.getWeff_n(),
				variantDTO.getAr(), variantDTO.getSigmaC(),
				variantDTO.getMcr_p(), variantDTO.getMcr_n()
			);
	}

	public Variant update(VariantDTO variantDTO) {
		Variant variant = findVariant(variantDTO.getId());

		if(variantDTO.getName() != null) variant.setName(variantDTO.getName());
		if(variantDTO.getProductId() != null) variant.setProduct(entityManager.find(Product.class, variantDTO.getProductId()));
		if(variantDTO.getWeff_p() != null) variant.setWeff_p(variantDTO.getWeff_p());
		if(variantDTO.getWeff_n() != null) variant.setWeff_n(variantDTO.getWeff_n());
		if(variantDTO.getAr() != null) variant.setAr(variantDTO.getAr());
		if(variantDTO.getSigmaC() != null) variant.setSigmaC(variantDTO.getSigmaC());
		if (variantDTO.getMcr_p() != null)variant.setMcr_p(variantDTO.getMcr_p());
		if(variantDTO.getMcr_n() != null) variant.setMcr_n(variantDTO.getMcr_n());

		return variant;
	}

	public boolean delete(int id) {
		Variant variant = findVariant(id);

		if (variant == null) return false;

		for (Structure s : variant.getStructures()) variant.removeStructure(s);
		entityManager.remove(variant);

		return true;
	}

	public Variant getVariant(int code){
		return entityManager.find(Variant.class, code);
	}

	public Variant findVariant(int code)
	{
		return entityManager.find(Variant.class, code);
	}

	public List<Variant> getAllVariants(@NotNull String filter)
	{
		return entityManager
			.createNamedQuery("getAllVariants", Variant.class)
			.setParameter("filter", "%"+filter+"%")
			.getResultList();
	}

	public List<Variant> getVariantsFilteredBy(@NotNull Integer id, @NotNull String filter)
	{
		return entityManager
			.createNamedQuery("getVariantsFilteredByMaterialId", Variant.class)
			.setParameter("materialId", id)
			.setParameter("filter", "%"+filter+"%")
			.getResultList();
	}

	public Variant getVariantBy(@NotNull @NotEmpty String name)
	{
		List<Variant> res = entityManager
			.createNamedQuery("getVariantByName", Variant.class)
			.setParameter("name", name)
			.getResultList();

		return res.isEmpty() ? null : res.get(0);
	}
}
