package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.dtos.UserDTO;
import io.github.ipl.tfc.docetressabores.entities.Client;

@Stateless
public class ClientBean {
	// TODO: documentation
	@PersistenceContext EntityManager entityManager;

	public Client create(String name, String phoneNumber, String email, String address, String username, String password) {
		if (Arrays
			.asList(name, phoneNumber, name, address, username, password)
			.stream()
			.anyMatch(Objects::isNull)
		) {
			return null;
		}

		Client client = new Client(name, phoneNumber, email, address, username, password);
		entityManager.persist(client);
		return client;
	}

	public Client create(UserDTO userDTO) {
		return create(
			userDTO.getName(),
			userDTO.getPhoneNumber(),
			userDTO.getEmail(),
			userDTO.getAddress(),
			userDTO.getUsername(),
			userDTO.getPassword()
		);
	}

	public Client findClient(String username) {
		return entityManager.find(Client.class, username);
	}

	public List<Client> getAllClients() {
		return entityManager
				.createNamedQuery("getAllClients", Client.class)
				.getResultList();
	}
}
