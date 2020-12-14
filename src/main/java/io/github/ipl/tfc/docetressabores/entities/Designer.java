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
public class Designer extends User {
	@OneToMany(mappedBy = "designer", cascade = CascadeType.REMOVE) private List<Project> projects;

	public Designer() {
		projects = new ArrayList<>();
	}

	public Designer(
		String name,
		String phoneNumber,
		String email,
		String username,
		String password
	) {
		super(name, phoneNumber, email, username, password);
		this.projects = new ArrayList<>();
	}


	public List<Project> getProjects() {
		return projects;
	}


	public void setProjects(@NotNull List<Project> projects) {
		this.projects = projects;
	}
}
