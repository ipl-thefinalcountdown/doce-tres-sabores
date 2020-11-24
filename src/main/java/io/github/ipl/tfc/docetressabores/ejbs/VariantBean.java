package io.github.ipl.tfc.docetressabores.ejbs;

import io.github.ipl.tfc.docetressabores.entities.Product;
import io.github.ipl.tfc.docetressabores.entities.Variant;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class VariantBean {
	// TODO: documentation
	@PersistenceContext
	private EntityManager entityManager;

	public Variant create(int productId, String name, double weff_p, double weff_n, double ar, double sigmaC){
		Product product = entityManager.find(Product.class, productId);

		if (product == null) {
			return null;
		} else {
			Variant variant = new Variant(product, name, weff_p, weff_n, ar, sigmaC);
			entityManager.persist(variant);
			return variant;
		}
	}

	public Variant getVariant(int code){
		return entityManager.find(Variant.class, code);
	}

	public Variant findVariant(int code)
	{
		return entityManager.find(Variant.class, code);
	}

	public List<Variant> getAllVariants()
	{
		return entityManager
			.createNamedQuery("getAllVariants", Variant.class)
			.getResultList();
	}
}
