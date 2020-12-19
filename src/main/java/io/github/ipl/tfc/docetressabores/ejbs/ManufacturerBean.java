package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.dtos.UserDTO;
import io.github.ipl.tfc.docetressabores.entities.Manufacturer;

@Stateless
public class ManufacturerBean {
	@PersistenceContext EntityManager entityManager;

	public Manufacturer create(String name, String phoneNumber, String email, String username, String password) {
		if (Arrays
			.asList(name, phoneNumber, name, username, password)
			.stream()
			.anyMatch(Objects::isNull)
		) {
			return null;
		}

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

	public boolean delete(String username) {
		Manufacturer manufacturer = findManufacturer(username);

		if (manufacturer == null) return false;
		entityManager.remove(manufacturer);
		return true;
	}

	public Manufacturer findManufacturer(String username) {
		return entityManager.find(Manufacturer.class, username);
	}

	public List<Manufacturer> getAllManufacturers() {
		return entityManager
				.createNamedQuery("getAllManufacturers", Manufacturer.class)
				.getResultList();
	}
}
