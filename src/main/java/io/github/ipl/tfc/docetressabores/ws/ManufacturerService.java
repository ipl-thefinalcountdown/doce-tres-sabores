package io.github.ipl.tfc.docetressabores.ws;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.github.ipl.tfc.docetressabores.dtos.UserDTO;
import io.github.ipl.tfc.docetressabores.ejbs.ManufacturerBean;
import io.github.ipl.tfc.docetressabores.entities.Manufacturer;

@Path("/manufacturers")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class ManufacturerService {
	@EJB ManufacturerBean manufacturerBean;

	public static UserDTO toDTO(Manufacturer manufacturer) {
		return new UserDTO(
			manufacturer.getName(),
			manufacturer.getPhoneNumber(),
			manufacturer.getEmail(),
			manufacturer.getUsername(),
			manufacturer.getPassword(),
			null,
			null,
			manufacturer.getClass().getSimpleName()
		);
	}

	public static List<UserDTO> toDTOs(List<Manufacturer> manufacturer) {
		return manufacturer
			.stream()
			.map(ManufacturerService::toDTO)
			.collect(Collectors.toList());
	}

	@GET
	@Path("/")
	@Transactional
	public Response getAllManufacturersWS() {
		return Response.ok(toDTOs(manufacturerBean.getAllManufacturers())).build();
	}

	@GET
	@Path("/{username}")
	@Transactional
	public Response getClientWS(@PathParam("username") String username) {
		Manufacturer manufacturer = manufacturerBean.findManufacturer(username);

		return (
			manufacturer == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.ok(toDTO(manufacturer))
			).build();
	}
}
