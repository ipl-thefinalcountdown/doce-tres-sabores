package io.github.ipl.tfc.docetressabores.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.github.ipl.tfc.docetressabores.entities.structures.Structure;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllProjects",
		query = "SELECT p FROM Project p "
			+ "JOIN p.client c "
			+ "WHERE UPPER(p.name) LIKE UPPER(:filter) OR "
			+ "UPPER(c.name) LIKE UPPER(:filter) OR "
			+ "CAST(p.id AS string) LIKE :filter "
			+ "ORDER BY p.id"
	),
	@NamedQuery(
		name = "getProjectsFromStructureId",
		query = "SELECT p FROM Project p "
			+ "JOIN p.structures s "
			+ "WHERE s.id = :structure_id"
	)
})
@Table(name = "PROJECTS")
public class Project {
	// TODO: documentation
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
	@NotNull @NotEmpty private String name;
	@ManyToMany(cascade = {
		CascadeType.PERSIST,
		CascadeType.MERGE
	})
	@JoinTable(
		name = "PROJECT_STRUCTURE",
		joinColumns = @JoinColumn(name = "project_id"),
		inverseJoinColumns = @JoinColumn(name = "structure_id")
	)
	private Set<Structure> structures;
	@ManyToOne @JoinColumn(name = "client_id") @NotNull private Client client;
	@ManyToOne @JoinColumn(name = "designer_id") @NotNull private Designer designer;

	private boolean completed;

	public Project() {
		structures = new HashSet<>();
	}

	public Project(String name, Client client, Designer designer) {
		this.name = name;
		this.client = client;
		this.designer = designer;
		this.completed = false;
		structures = new HashSet<>();
	}


	// getters
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Structure> getStructures() {
		return structures.stream().collect(Collectors.toList());
	}

	public Client getClient() {
		return client;
	}

	public Designer getDesigner() {
		return designer;
	}

	public boolean getCompleted() {
		return completed;
	}


	// setters
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStructures(@NotNull Set<Structure> structures) {
		this.structures = structures;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setDesigner(Designer designer) {
		this.designer = designer;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}


	public void addStructure(@NotNull Structure structure) {
		this.structures.add(structure);
	}

	public void removeStructure(@NotNull Structure structure) {
		this.structures.remove(structure);
	}
}
