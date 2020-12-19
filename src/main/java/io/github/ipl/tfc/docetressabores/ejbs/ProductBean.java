package io.github.ipl.tfc.docetressabores.ejbs;

import io.github.ipl.tfc.docetressabores.dtos.ProductDTO;
import io.github.ipl.tfc.docetressabores.entities.Family;
import io.github.ipl.tfc.docetressabores.entities.Product;
import io.github.ipl.tfc.docetressabores.entities.Variant;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import java.util.List;

@Stateless
public class ProductBean {
	// TODO: documentation
	@PersistenceContext
	private EntityManager entityManager;

	public Product create(int familyId, String name) {
		Family family = entityManager.find(Family.class, familyId);
		Product product = new Product(family, name);
		entityManager.persist(product);
		return product;
	}

	public Product update(ProductDTO productDTO) {
		Product product = findProduct(productDTO.getId());

		if(productDTO.getName() != null) product.setName(productDTO.getName());

		return product;
	}

	public boolean delete(int id) {
		Product product = findProduct(id);

		if (product != null)
		{
			for (Variant variant : product.getVariants()) variant.removeAllStructures();
			entityManager.remove(product);
			return true;
		}

		return false;
	}

	public Product findProduct(int id)
	{
		return entityManager.find(Product.class, id);
	}

	public List<Product> getAllProducts(@NotNull String filter)
	{
		return entityManager
			.createNamedQuery("getAllProducts", Product.class)
			.setParameter("filter", "%"+filter+"%")
			.getResultList();
	}
}
