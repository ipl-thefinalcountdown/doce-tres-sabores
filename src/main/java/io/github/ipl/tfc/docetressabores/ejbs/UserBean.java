package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.entities.User;

@Stateless
public class UserBean {
	@PersistenceContext EntityManager entityManager;

	public User findUser(int id) {
		return entityManager.find(User.class, id);
	}

	public List<User> getAllUsers() {
		return entityManager
				.createNamedQuery("getAllUsers", User.class)
				.getResultList();
	}
}
