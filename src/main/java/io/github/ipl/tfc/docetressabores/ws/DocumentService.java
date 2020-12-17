package io.github.ipl.tfc.docetressabores.ws;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.github.ipl.tfc.docetressabores.dtos.DocumentDTO;
import io.github.ipl.tfc.docetressabores.ejbs.DocumentBean;
import io.github.ipl.tfc.docetressabores.entities.Document;

@Path("/documents")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class DocumentService {
	@EJB DocumentBean documentBean;

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
	@Transactional
	public Response downloadDocumentWS(@PathParam("id") int id) {
		return (
			documentBean.deleteDocument(id)
				? Response.noContent()
				: Response.status(Response.Status.BAD_REQUEST)
			).build();
	}
}
