package io.github.ipl.tfc.docetressabores.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllProjects",
		query = "SELECT p FROM Project p WHERE p.name LIKE :name"
	)
})
@Table(name = "PROJECTS")
public class Project {
	// TODO: documentation
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
	@NotNull @NotEmpty private String name;
	@ManyToMany private List<Structure> structures;
	@ManyToOne @JoinColumn(name = "client_id") @NotNull private Client client;

	public Project() {
		structures = new ArrayList<>();
	}

	public Project(String name, Client client) {
		this.name = name;
		this.client = client;
		structures = new ArrayList<>();
	}


	// getters
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Structure> getStructures() {
		return structures;
	}

	public Client getClient() {
		return client;
	}


	// setters
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStructures(@NotNull List<Structure> structures) {
		this.structures = structures;
	}

	public void setClient(Client client) {
		this.client = client;
	}


	public void addStructure(@NotNull Structure structure) {
		this.structures.add(structure);
	}

	public void removeStructure(@NotNull Structure structure) {
		this.structures.remove(structure);
	}
}
