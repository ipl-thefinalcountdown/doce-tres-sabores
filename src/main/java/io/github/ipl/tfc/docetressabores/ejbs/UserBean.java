package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.github.ipl.tfc.docetressabores.dtos.UserDTO;
import io.github.ipl.tfc.docetressabores.entities.User;

@Stateless
public class UserBean {
	@PersistenceContext EntityManager entityManager;

	public User authenticate(String username, String password) {
		User user = findUser(username);
		if (user == null || !BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()).verified) return null;
		return user;
	}

	public boolean delete(String username) {
		User user = findUser(username);

		if (user == null) return false;
		entityManager.remove(user);
		return true;
	}

	public User findUser(String username) {
		return entityManager.find(User.class, username);
	}

	public List<User> getAllUsers() {
		return entityManager
				.createNamedQuery("getAllUsers", User.class)
				.getResultList();
	}
}
