package io.github.ipl.tfc.docetressabores.entities;

import javax.persistence.*;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllManufacturers",
		query = "SELECT m FROM Manufacturer m ORDER BY m.id"
	)
})
public class Manufacturer extends User {
	public Manufacturer() {
		super();
	}

	public Manufacturer(String name, String phoneNumber, String email, String username, String password) {
		super(name, phoneNumber, email, username, password);
	}
}
