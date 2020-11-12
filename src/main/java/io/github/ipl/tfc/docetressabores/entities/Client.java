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
@Table(
	name = "CLIENTS",
	uniqueConstraints = @UniqueConstraint(
		columnNames = {
			"EMAIL"
		}
	))
public class Client {
	// TODO: documentation
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
	@NotNull @NotEmpty private String phoneNumber;
	@NotNull @NotEmpty private String name;
	@NotNull @Email private String email;
	@NotNull @NotEmpty private String address;
	@OneToMany(mappedBy = "client") private List<Project> projects;

	public Client() {
		projects = new ArrayList<>();
	}

	public Client(
		@NotNull @NotEmpty String name,
		@NotNull @NotEmpty String phoneNumber,
		@NotNull @Email String email,
		@NotNull @NotEmpty String address
	) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
		this.projects = new ArrayList<>();
	}


	// getters
	public int getId() {
		return id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public List<Project> getProjects() {
		return projects;
	}


	// setters
	public void setId(int id) {
		this.id = id;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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
