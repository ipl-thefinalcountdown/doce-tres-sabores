package io.github.ipl.tfc.docetressabores.ejbs;

import java.io.File;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.github.ipl.tfc.docetressabores.entities.Document;
import io.github.ipl.tfc.docetressabores.entities.Project;
import io.github.ipl.tfc.docetressabores.ws.ProjectService;

@Stateless
public class DocumentBean {
	@PersistenceContext EntityManager entityManager;

	public Document create(int productId, String filepath, String fileName) {
		Project project = entityManager.find(Project.class, productId);

		if (project == null || filepath == null || fileName == null) return null;

		Document document = new Document(project, filepath, fileName);
		entityManager.persist(document);
		return document;
	}

	public Document findDocument(int id) {
		return entityManager.find(Document.class, id);
	}

	public boolean deleteDocument(int id) {
		Document document = findDocument(id);

		if (document == null) return false;

		File file = new File(ProjectService.DOCUMENT_DIR, document.getFilePath());
		file.delete();
		entityManager.remove(document);

		return true;
	}

	public List<Document> getAllDocuments() {
		return entityManager
			.createNamedQuery("getAllDocuments", Document.class)
			.getResultList();
	}

	public Document getProjectDocumentBy(int projectId, @NotNull @NotEmpty String fileName) {
		List<Document> res = entityManager
			.createNamedQuery("getProjectDocumentByName", Document.class)
			.setParameter("projectId", projectId)
			.setParameter("fileName", fileName)
			.getResultList();

		return res.isEmpty() ? null : res.get(0);
	}
}
