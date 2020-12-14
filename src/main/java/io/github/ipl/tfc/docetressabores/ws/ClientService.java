package io.github.ipl.tfc.docetressabores.ws;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.github.ipl.tfc.docetressabores.dtos.UserDTO;
import io.github.ipl.tfc.docetressabores.ejbs.ClientBean;
import io.github.ipl.tfc.docetressabores.entities.Client;

@Path("/clients")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class ClientService {
	@EJB ClientBean clientBean;

	public static UserDTO toDTO(Client client) {
		return new UserDTO(
			client.getId(),
			client.getName(),
			client.getPhoneNumber(),
			client.getEmail(),
			client.getUsername(),
			client.getPassword(),
			client.getAddress(),
			ProjectService.toDTOs(client.getProjects()),
			client.getClass().getSimpleName()
		);
	}

	public static List<UserDTO> toDTOs(List<Client> client) {
		return client
			.stream()
			.map(ClientService::toDTO)
			.collect(Collectors.toList());
	}

	@GET
	@Path("/")
	@Transactional
	public Response getAllClientsWS() {
		return Response.ok(toDTOs(clientBean.getAllClients())).build();
	}

	@GET
	@Path("/{id}")
	@Transactional
	public Response getClientWS(@PathParam("id") int id) {
		Client client = clientBean.findClient(id);

		return (
			client == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.ok(toDTO(client))
			).build();
	}
}
