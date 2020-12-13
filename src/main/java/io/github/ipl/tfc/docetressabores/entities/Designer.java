package io.github.ipl.tfc.docetressabores.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllDesigners",
		query = "SELECT d FROM Designer d ORDER BY d.id"
	)
})
public class Designer {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
	@NotNull @NotEmpty private String name;
	@OneToMany(mappedBy = "designer", cascade = CascadeType.REMOVE) private List<Project> projects;

	public Designer() {
		projects = new ArrayList<>();
	}

	public Designer(@NotNull @NotEmpty String name) {
		this.name = name;
		this.projects = new ArrayList<>();
	}


	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Project> getProjects() {
		return projects;
	}


	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProjects(@NotNull List<Project> projects) {
		this.projects = projects;
	}
}
