package io.github.ipl.tfc.docetressabores.ws;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import io.github.ipl.tfc.docetressabores.dtos.DocumentDTO;
import io.github.ipl.tfc.docetressabores.ejbs.DocumentBean;
import io.github.ipl.tfc.docetressabores.entities.Document;

@Path("/documents")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class DocumentService {
	@EJB DocumentBean documentBean;
	@Context SecurityContext securityContext;

	public static DocumentDTO toDTO(Document document) {
		return new DocumentDTO(
			document.getId(),
			null,
			document.getFileName(),
			null
		);
	}

	public static List<DocumentDTO> toDTOs(List<Document> documents) {
		return documents.stream().map(DocumentService::toDTO).collect(Collectors.toList());
	}


	@DELETE
	@Path("/{id}")
	@RolesAllowed({"Client", "Designer"})
	@Transactional
	public Response deleteDocumentWS(@PathParam("id") int id) {
		Principal principal = securityContext.getUserPrincipal();

		Document document = documentBean.findDocument(id);

		if (principal == null
			|| (document != null
				&& (!document.getProject().getClient().getUsername().equals(principal.getName())
					&& !document.getProject().getDesigner().getUsername().equals(principal.getName())
				)
			)
		) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		return (
			documentBean.deleteDocument(id)
				? Response.noContent()
				: Response.status(Response.Status.BAD_REQUEST)
			).build();
	}
}
