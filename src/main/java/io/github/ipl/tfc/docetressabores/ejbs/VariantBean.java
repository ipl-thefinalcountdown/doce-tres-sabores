package io.github.ipl.tfc.docetressabores.ejbs;

import io.github.ipl.tfc.docetressabores.entities.Product;
import io.github.ipl.tfc.docetressabores.entities.Variant;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class VariantBean {

	@PersistenceContext
	private EntityManager entityManager;

	public void create(int code, String productName, String name, double weff_p, double weff_n, double ar, double sigmaC){
		Product product = entityManager.find(Product.class, productName);
		Variant p = new Variant(code, product, name, weff_p, weff_n, ar, sigmaC);
		entityManager.persist(p);
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
		return (List<Variant>) entityManager.createNamedQuery("getAllVariants").getResultList();
	}
}
