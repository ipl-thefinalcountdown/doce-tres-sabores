package io.github.ipl.tfc.docetressabores.ejbs;

import io.github.ipl.tfc.docetressabores.entities.Family;
import io.github.ipl.tfc.docetressabores.entities.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class ProductBean {
	// TODO: documentation
	@PersistenceContext
	private EntityManager entityManager;

	public Product create(int familyId, String name){
		Family family = entityManager.find(Family.class, familyId);
		Product product = new Product(family, name);
		entityManager.persist(product);
		return product;
	}

	public Product findProduct(int id)
	{
		return entityManager.find(Product.class, id);
	}

	public List<Product> getAllProducts()
	{
		return entityManager
			.createNamedQuery("getAllProducts", Product.class)
			.getResultList();
	}
}
