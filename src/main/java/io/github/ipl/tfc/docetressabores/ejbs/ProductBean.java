package io.github.ipl.tfc.docetressabores.ejbs;

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

	public Product create(String name){
		Product product = new Product(name);
		entityManager.persist(product);
		return product;
	}

	public Product findProduct(String name)
	{
		return entityManager.find(Product.class, name);
	}

	public List<Product> getAllProducts()
	{
		return entityManager
			.createNamedQuery("getAllProducts", Product.class)
			.getResultList();
	}
}
