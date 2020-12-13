package io.github.ipl.tfc.docetressabores.dtos;

import java.util.ArrayList;
import java.util.List;

public class DesignerDTO {
	private int id;
	private String name;
	private List<ClientDTO> clients;

	public DesignerDTO() {
		clients = new ArrayList<>();
	}


	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<ClientDTO> getClients() {
		return clients;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setClients(List<ClientDTO> clients) {
		this.clients = clients;
	}
}
