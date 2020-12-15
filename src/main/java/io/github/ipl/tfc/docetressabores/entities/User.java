package io.github.ipl.tfc.docetressabores.entities;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllUsers",
		query = "SELECT u FROM User u ORDER BY u.id"
	)
})
@Table(
	name = "USERS",
	uniqueConstraints = @UniqueConstraint(
		columnNames = {
			"EMAIL"
		}
	)
)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE")
@OnDelete(action = OnDeleteAction.CASCADE)
public class User {
	@Id @NotNull @NotEmpty protected String username;
	@NotNull @NotEmpty protected String password;
	@NotNull @NotEmpty protected String name;
	@NotNull @NotEmpty protected String phoneNumber;
	@NotNull @Email protected String email;

	public User() {}

	public User(String name, String phoneNumber, String email, String username, String password) {
		this.name = name;
		this.phoneNumber= phoneNumber;
		this.email = email;
		this.username = username;
		this.password = hashPassword(password);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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


	public String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
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

	public static String hashPassword(String password) {
		return BCrypt.withDefaults().hashToString(12, password.toCharArray());
	}
}
