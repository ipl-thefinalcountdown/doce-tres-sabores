package io.github.ipl.tfc.docetressabores.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Serializable {
	private String name;
	private String phoneNumber;
	private String email;
	private String username;
	private String password;
	private String address; // Client only!
	private List<ProjectDTO> projects; // Client and Designer only!
	private String userType;

	public UserDTO() {
		projects = new ArrayList<>();
	}


	public UserDTO(
		String name,
		String phoneNumber,
		String email,
		String username,
		String password,
		String address,
		List<ProjectDTO> projects,
		String userType
	) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.username = username;
		this.password = password;
		this.address = address;
		this.projects = projects;
		this.userType = userType;
	}


	// getters
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getAddress() {
		return address;
	}

	public List<ProjectDTO> getProjects() {
		return projects;
	}

	public String getUserType() {
		return userType;
	}


	// setters
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setProjects(List<ProjectDTO> projects) {
		this.projects = projects;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
}
