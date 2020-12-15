package io.github.ipl.tfc.docetressabores.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllClients",
		query = "SELECT c FROM Client c ORDER BY c.id"
	)
})
public class Client extends User {
	// TODO: documentation
	@NotNull @NotEmpty private String address;
	@OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE) private List<Project> projects;

	public Client() {
		super();
		projects = new ArrayList<>();
	}

	public Client(
		@NotNull @NotEmpty String name,
		@NotNull @NotEmpty String phoneNumber,
		@NotNull @Email String email,
		@NotNull @NotEmpty String address,
		@NotNull @NotEmpty String username,
		@NotNull @NotEmpty String password
	) {
		super(name, phoneNumber, email, username, password);
		this.address = address;
		this.projects = new ArrayList<>();
	}


	// getters
	public String getAddress() {
		return address;
	}

	public List<Project> getProjects() {
		return projects;
	}


	// setters
	public void setAddress(String address) {
		this.address = address;
	}

	public void setProjects(@NotNull List<Project> projects) {
		this.projects = projects;
	}


	public void addProject(@NotNull Project project) {
		this.projects.add(project);
	}

	public void removeProject(@NotNull Project project) {
		this.projects.remove(project);
	}
}
