package io.github.ipl.tfc.docetressabores.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllDocuments",
		query = "SELECT d FROM Document d ORDER BY d.id"
	),
	@NamedQuery(
		name = "getProjectDocumentByName",
		query = "SELECT d FROM Document d "
			+ "WHERE d.project.id = :projectId "
			+ "AND d.fileName = :fileName "
			+ "ORDER BY d.id"
	)
})
@Table(name = "DOCUMENTS")
public class Document {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
	private String fileName;
	private String filePath;
	@ManyToOne @JoinColumn(name = "project_id") @NotNull private Project project;

	public Document() {}

	public Document(Project project, String fileName, String filePath) {
		this.project = project;
		this.fileName = fileName;
		this.filePath = filePath;
	}


	public int getId() {
		return id;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public Project getProject() {
		return project;
	}


	public void setId(int id) {
		this.id = id;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
