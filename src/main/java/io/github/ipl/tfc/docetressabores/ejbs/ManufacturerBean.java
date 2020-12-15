package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.dtos.UserDTO;
import io.github.ipl.tfc.docetressabores.entities.Manufacturer;

@Stateless
public class ManufacturerBean {
	@PersistenceContext EntityManager entityManager;

	public Manufacturer create(String name, String phoneNumber, String email, String username, String password) {
		Manufacturer manufacturer = new Manufacturer(name, phoneNumber, email, username, password);
		entityManager.persist(manufacturer);
		return manufacturer;
	}

	public Manufacturer create(UserDTO userDTO) {
		return create(
			userDTO.getName(),
			userDTO.getPhoneNumber(),
			userDTO.getEmail(),
			userDTO.getUsername(),
			userDTO.getPassword()
		);
	}

	public boolean delete(int id) {
		Manufacturer manufacturer = findManufacturer(id);

		if (manufacturer == null) return false;
		entityManager.remove(manufacturer);
		return true;
	}

	public Manufacturer findManufacturer(int id) {
		return entityManager.find(Manufacturer.class, id);
	}

	public List<Manufacturer> getAllManufacturers() {
		return entityManager
				.createNamedQuery("getAllManufacturers", Manufacturer.class)
				.getResultList();
	}
}
