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

	public User authenticate(UserDTO userDTO) {
		List<User> user = findUserBy(userDTO.getUsername());
		if (user.isEmpty() || !BCrypt.verifyer().verify(userDTO.getPassword().toCharArray(), user.get(0).getPassword()).verified) return null;
		return user.get(0);
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

	public List<User> findUserBy(String username) {
		return entityManager
			.createNamedQuery("getUserByUsername", User.class)
			.setParameter("username", username)
			.getResultList();
	}

	public List<User> getAllUsers() {
		return entityManager
				.createNamedQuery("getAllUsers", User.class)
				.getResultList();
	}
}
