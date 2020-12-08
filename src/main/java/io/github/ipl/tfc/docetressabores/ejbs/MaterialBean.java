package io.github.ipl.tfc.docetressabores.ejbs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.entities.Material;

@Stateless
public class MaterialBean {
	@PersistenceContext EntityManager entityManager;

	public Material create(int type) {
		Material material = new Material(type);
		entityManager.persist(material);
		return material;
	}

	public Material findMaterial(int id) {
		return entityManager.find(Material.class, id);
	}
}
