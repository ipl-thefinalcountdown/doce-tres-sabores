package io.github.ipl.tfc.docetressabores.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientDTO implements Serializable {
	private int id;
	private String name;
	private String phoneNumber;
	private String email;
	private String address;
	private List<ProjectDTO> projects;

	public ClientDTO() {
		projects = new ArrayList<>();
	}


	public ClientDTO(
		int id,
		String name,
		String phoneNumber,
		String email,
		String address,
		List<ProjectDTO> projects
	) {
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
		this.projects = projects;
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

	public List<ProjectDTO> getProjects() {
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

	public void setProjects(List<ProjectDTO> projects) {
		this.projects = projects;
	}


	public void addProject(ProjectDTO project) {
		this.projects.add(project);
	}

	public void removeProject(ProjectDTO project) {
		this.projects.remove(project);
	}
}
