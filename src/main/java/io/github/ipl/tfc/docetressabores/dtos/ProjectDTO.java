package io.github.ipl.tfc.docetressabores.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectDTO implements Serializable {
	private int id;
	private String name;
	private String clientUsername;
	private String designerUsername;
	private String clientName;
	private String designerName;
	private Boolean completed;
	private List<StructureDTO> structures;

	// TODO: File list

	public ProjectDTO() {
		structures = new ArrayList<>();
	}

	public ProjectDTO(
		int id,
		String name,
		String clientUsername,
		String designerUsername,
		String clientName,
		String designerName,
		Boolean completed,
		List<StructureDTO> structures
	) {
		this.id = id;
		this.name = name;
		this.clientUsername = clientUsername;
		this.designerUsername = designerUsername;
		this.clientName = clientName;
		this.designerName = designerName;
		this.completed = completed;
		this.structures = structures;
	}

	// getters
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<StructureDTO> getStructures() {
		return structures;
	}

	public String getClientUsername() {
		return clientUsername;
	}

	public String getDesignerUsername() {
		return designerUsername;
	}

	public String getClientName() {
		return clientName;
	}

	public String getDesignerName() {
		return designerName;
	}

	public Boolean getCompleted() {
		return completed;
	}

	// setters
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStructures(List<StructureDTO> structures) {
		this.structures = structures;
	}

	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	}

	public void setDesignerUsername(String designerUsername) {
		this.designerUsername = designerUsername;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void setDesignerName(String designerName) {
		this.designerName = designerName;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
}
