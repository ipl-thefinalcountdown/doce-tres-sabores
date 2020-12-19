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

	public Project create(String name, String clientUsername, String designerUsername)
	{
		if (name == null || clientUsername == null || designerUsername == null) return null;

		Client client = entityManager.find(Client.class, clientUsername);
		Designer designer = entityManager.find(Designer.class, designerUsername);

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

		if (projectDTO.getName() != null) project.setName(projectDTO.getName());
		if (projectDTO.getCompleted() != null) project.setCompleted(projectDTO.getCompleted());
		if (projectDTO.getStructures() != null)
			project.setStructures(
				projectDTO.getStructures()
					.stream()
					.map(s -> entityManager.find(Structure.class, s.getId()))
					.collect(Collectors.toSet())
			);
		if (projectDTO.getObservations() != null) project.setObservations(projectDTO.getObservations());

		return project;
	}

	public Project delete(int id) {
		Project project = findProject(id);

		if (project != null)
			entityManager.remove(project);

		return project;
	}

	public Project create(ProjectDTO projectDTO) {
		return create(projectDTO.getName(), projectDTO.getClientUsername(), projectDTO.getDesignerUsername());
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
