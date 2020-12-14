package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.dtos.UserDTO;
import io.github.ipl.tfc.docetressabores.entities.Designer;

@Stateless
public class DesignerBean {
	@PersistenceContext EntityManager entityManager;

	public Designer create(String name, String phoneNumber, String email, String username, String password) {
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

	public Designer findDesigner(int id) {
		return entityManager.find(Designer.class, id);
	}

	public List<Designer> getAllDesigners() {
		return entityManager
				.createNamedQuery("getAllDesigners", Designer.class)
				.getResultList();
	}
}
