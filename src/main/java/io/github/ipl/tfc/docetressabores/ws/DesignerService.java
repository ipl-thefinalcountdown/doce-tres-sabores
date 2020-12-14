package io.github.ipl.tfc.docetressabores.ws;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.github.ipl.tfc.docetressabores.dtos.UserDTO;
import io.github.ipl.tfc.docetressabores.ejbs.DesignerBean;
import io.github.ipl.tfc.docetressabores.entities.Designer;

@Path("/designers")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class DesignerService {
	@EJB DesignerBean designerBean;

	public static UserDTO toDTO(Designer designer) {
		return new UserDTO(
			designer.getId(),
			designer.getName(),
			designer.getPhoneNumber(),
			designer.getEmail(),
			designer.getUsername(),
			designer.getPassword(),
			null,
			ProjectService.toDTOs(designer.getProjects()),
			designer.getClass().getSimpleName()
		);
	}

	public static List<UserDTO> toDTOs(List<Designer> designer) {
		return designer
			.stream()
			.map(DesignerService::toDTO)
			.collect(Collectors.toList());
	}

	@GET
	@Path("/")
	@Transactional
	public Response getAllDesignersWS() {
		return Response.ok(toDTOs(designerBean.getAllDesigners())).build();
	}

	@GET
	@Path("/{id}")
	@Transactional
	public Response getDesignerWS(@PathParam("id") int id) {
		Designer designer = designerBean.findDesigner(id);

		return (
			designer == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.ok(toDTO(designer))
			).build();
	}
}
