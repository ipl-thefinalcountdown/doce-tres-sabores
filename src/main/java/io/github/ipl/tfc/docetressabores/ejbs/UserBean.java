package io.github.ipl.tfc.docetressabores.ejbs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserBean {
	@PersistenceContext EntityManager entityManager;
}
