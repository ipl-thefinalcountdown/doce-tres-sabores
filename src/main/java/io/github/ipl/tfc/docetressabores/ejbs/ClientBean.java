package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.entities.Client;
import io.github.ipl.tfc.docetressabores.entities.Project;

@Stateless
public class ClientBean {
	// TODO: documentation
	@PersistenceContext EntityManager entityManager;

	public Client create(String phoneNumber, String name, String email, String address) {
		Client client = new Client(phoneNumber, name, email, address);
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
