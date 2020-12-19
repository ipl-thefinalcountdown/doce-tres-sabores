package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.dtos.UserDTO;
import io.github.ipl.tfc.docetressabores.entities.Designer;

@Stateless
public class DesignerBean {
	@PersistenceContext EntityManager entityManager;
	@EJB UserBean userBean;

	public Designer create(String name, String phoneNumber, String email, String username, String password) {
		if (Arrays.asList(name, phoneNumber, name, username, password)
				.stream()
				.anyMatch(Objects::isNull)
			|| findDesigner(username) != null
			|| userBean.getUserBy(email) != null
		) {
			return null;
		}

		Designer designer = new Designer(name, phoneNumber, email, username, password);
		entityManager.persist(designer);
		return designer;
	}

	public Designer create(UserDTO userDTO) {
		return create(
			userDTO.getName(),
			userDTO.getPhoneNumber(),
			userDTO.getEmail(),
			userDTO.getUsername(),
			userDTO.getPassword()
		);
	}

	public Designer findDesigner(String username) {
		return entityManager.find(Designer.class, username);
	}

	public List<Designer> getAllDesigners() {
		return entityManager
				.createNamedQuery("getAllDesigners", Designer.class)
				.getResultList();
	}
}
