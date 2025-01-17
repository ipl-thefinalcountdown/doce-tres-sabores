package io.github.ipl.tfc.docetressabores.ejbs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.entities.Family;
import io.github.ipl.tfc.docetressabores.entities.Material;

import java.util.List;

@Stateless
public class FamilyBean {
	@PersistenceContext EntityManager entityManager;

	public Family create(int type, String name) {
		if (name == null) return null;

		Material material = entityManager.find(Material.class, type);
		Family family = new Family(material, name);
		entityManager.persist(family);

		return family;
	}

	public Family findFamily(int id) {
		return entityManager.find(Family.class, id);
	}

	public List<Family> getAllFamilies()
	{
		return entityManager
			.createNamedQuery("getAllFamilies", Family.class)
			.getResultList();
	}
}
