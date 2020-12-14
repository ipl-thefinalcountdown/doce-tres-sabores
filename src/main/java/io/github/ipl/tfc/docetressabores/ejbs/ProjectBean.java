package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import io.github.ipl.tfc.docetressabores.dtos.ProjectDTO;
import io.github.ipl.tfc.docetressabores.entities.Client;
import io.github.ipl.tfc.docetressabores.entities.Designer;
import io.github.ipl.tfc.docetressabores.entities.Project;
import io.github.ipl.tfc.docetressabores.entities.structures.Structure;

@Stateless
public class ProjectBean {
	// TODO: documentation
	@PersistenceContext EntityManager entityManager;

	public Project create(String name, int clientId, int designerId)
	{
		Client client = entityManager.find(Client.class, clientId);
		Designer designer = entityManager.find(Designer.class, designerId);

		if (client == null || designer == null) {
			return null;
		} else {
			Project project = new Project(name, client, designer);
			entityManager.persist(project);
			return project;
		}
	}

	public Project update(ProjectDTO projectDTO) {
		Project project = findProject(projectDTO.getId());

		if(projectDTO.getName() != null) project.setName(projectDTO.getName());

		if (projectDTO.getStructures() != null)
			project.setStructures(
				projectDTO.getStructures()
					.stream()
					.map(s -> entityManager.find(Structure.class, s.getId()))
					.collect(Collectors.toSet())
			);

		return project;
	}

	public Project delete(int id) {
		Project project = findProject(id);

		if (project != null)
			entityManager.remove(project);

		return project;
	}

	public Project create(ProjectDTO projectDTO) {
		return create(projectDTO.getName(), projectDTO.getClientId(), projectDTO.getDesignerId());
	}

	public Project findProject(int id) {
		return entityManager.find(Project.class, id);
	}

	public List<Project> getAllProjects(@NotNull String filter) {
		return entityManager
			.createNamedQuery("getAllProjects", Project.class)
			.setParameter("filter", "%"+filter+"%")
			.getResultList();
	}
}
