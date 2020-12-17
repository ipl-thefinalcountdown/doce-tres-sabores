package io.github.ipl.tfc.docetressabores.ws;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;

import io.github.ipl.tfc.docetressabores.dtos.DocumentDTO;
import io.github.ipl.tfc.docetressabores.ejbs.DocumentBean;
import io.github.ipl.tfc.docetressabores.entities.Document;

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
}
