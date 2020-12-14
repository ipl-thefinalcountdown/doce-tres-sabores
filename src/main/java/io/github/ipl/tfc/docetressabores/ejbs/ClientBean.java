package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.entities.Client;

@Stateless
public class ClientBean {
	// TODO: documentation
	@PersistenceContext EntityManager entityManager;

	// FIXME: throws EntityExistsException
	public Client create(String phoneNumber, String name, String email, String address, String username, String password) {
		Client client = new Client(phoneNumber, name, email, address, username, password);
		entityManager.persist(client);
		return client;
	}

	public Client findClient(int id) {
		return entityManager.find(Client.class, id);
	}

	public List<Client> getAllClients() {
		return entityManager
				.createNamedQuery("getAllClients", Client.class)
				.getResultList();
	}
}
