package io.github.ipl.tfc.docetressabores.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectDTO implements Serializable {
	private int id;
	private String name;
	private int clientId;
	private String clientName;
	private List<StructureDTO> structures;

	// TODO: File list

	public ProjectDTO() {
		structures = new ArrayList<>();
	}

	public ProjectDTO(int id, String name, int clientId, String clientName, List<StructureDTO> structures) {
		this.id = id;
		this.name = name;
		this.clientId = clientId;
		this.clientName = clientName;
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

	public int getClientId() {
		return clientId;
	}

	public String getClientName() {
		return clientName;
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

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void addStructure(StructureDTO structure) {
		this.structures.add(structure);
	}

	public void removeStructure(StructureDTO structure) {
		this.structures.remove(structure);
	}
}
