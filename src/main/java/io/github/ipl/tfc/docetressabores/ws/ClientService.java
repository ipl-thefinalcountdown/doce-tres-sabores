package io.github.ipl.tfc.docetressabores.ws;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.github.ipl.tfc.docetressabores.dtos.ClientDTO;
import io.github.ipl.tfc.docetressabores.ejbs.ClientBean;
import io.github.ipl.tfc.docetressabores.entities.Client;

@Path("/clients")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class ClientService {
	@EJB ClientBean clientBean;

	public static ClientDTO toDTO(Client client) {
		return new ClientDTO(
			client.getId(),
			client.getName(),
			client.getPhoneNumber(),
			client.getEmail(),
			client.getAddress(),
			ProjectService.toDTOs(client.getProjects())
		);
	}

	public static List<ClientDTO> toDTOs(List<Client> client) {
		return client
			.stream()
			.map(ClientService::toDTO)
			.collect(Collectors.toList());
	}

	@GET
	@Path("/all")
	@Transactional
	public List<ClientDTO> getAllClientsWS() {
		return toDTOs(clientBean.getAllClients());
	}
}
