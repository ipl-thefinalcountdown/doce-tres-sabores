package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.dtos.ProjectDTO;
import io.github.ipl.tfc.docetressabores.entities.Client;
import io.github.ipl.tfc.docetressabores.entities.Project;

@Stateless
public class ProjectBean {
	// TODO: documentation
	@PersistenceContext EntityManager entityManager;

	// TODO: throws
	public Project create(String name, int clientId)
	{
		Client client = entityManager.find(Client.class, clientId);

		if (client == null) {
			// FIXME: throw exception
			return null;
		} else {
			Project project = new Project(name, client);
			entityManager.persist(project);
			return project;
		}
	}

	public void create(ProjectDTO projectDTO) {
		create(projectDTO.getName(), projectDTO.getClientId());
	}

	public Project findProject(int id) {
		return entityManager.find(Project.class, id);
	}

	public List<Project> getAllProjects() {
		return entityManager
			.createNamedQuery("getAllProjects", Project.class)
			.getResultList();
	}
}
