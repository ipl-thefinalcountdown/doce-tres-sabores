package io.github.ipl.tfc.docetressabores.dtos;

public class DocumentDTO {
	private int id;
	private Integer projectId;
	private String filePath;
	private String fileName;

	public DocumentDTO() {}

	public DocumentDTO(int id, Integer projectId, String filePath, String fileName) {
		this.id = id;
		this.projectId = projectId;
		this.filePath = filePath;
		this.fileName = fileName;
	}


	public int getId() {
		return id;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getFileName() {
		return fileName;
	}


	public void setId(int id) {
		this.id = id;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
