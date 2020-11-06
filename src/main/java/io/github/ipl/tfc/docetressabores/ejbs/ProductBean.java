package io.github.ipl.tfc.docetressabores.ejbs;

import io.github.ipl.tfc.docetressabores.entities.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class ProductBean {

	@PersistenceContext
	private EntityManager entityManager;

	public void create(String name){
		Product p = new Product(name);
		entityManager.persist(p);
	}

	public Product findProduct(String name)
	{
		return entityManager.find(Product.class, name);
	}

	public List<Product> getAllProducts()
	{
		return (List<Product>) entityManager.createNamedQuery("getAllProducts").getResultList();
	}
}
