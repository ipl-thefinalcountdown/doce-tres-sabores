package io.github.ipl.tfc.docetressabores.ejbs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.entities.Designer;

@Stateless
public class DesignerBean {
	@PersistenceContext EntityManager entityManager;

	public Designer create(String name, String phoneNumber, String email, String username, String password) {
		Designer designer = new Designer(name, phoneNumber, email, username, password);
		entityManager.persist(designer);
		return designer;
	}
}
