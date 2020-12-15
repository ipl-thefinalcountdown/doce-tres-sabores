package io.github.ipl.tfc.docetressabores.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectDTO implements Serializable {
	private int id;
	private String name;
	private int clientId;
	private int designerId;
	private String clientName;
	private String designerName;
	private List<StructureDTO> structures;

	// TODO: File list

	public ProjectDTO() {
		structures = new ArrayList<>();
	}

	public ProjectDTO(
		int id,
		String name,
		int clientId,
		int designerId,
		String clientName,
		String designerName,
		List<StructureDTO> structures
	) {
		this.id = id;
		this.name = name;
		this.clientId = clientId;
		this.designerId = designerId;
		this.clientName = clientName;
		this.designerName = designerName;
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

	public int getDesignerId() {
		return designerId;
	}

	public String getClientName() {
		return clientName;
	}

	public String getDesignerName() {
		return designerName;
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

	public void setDesignerId(int designerId) {
		this.designerId = designerId;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void setDesignerName(String designerName) {
		this.designerName = designerName;
	}

	public void addStructure(StructureDTO structure) {
		this.structures.add(structure);
	}

	public void removeStructure(StructureDTO structure) {
		this.structures.remove(structure);
	}
}
