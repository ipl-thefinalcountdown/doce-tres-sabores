package io.github.ipl.tfc.docetressabores.ws;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.github.ipl.tfc.docetressabores.dtos.ProjectDTO;
import io.github.ipl.tfc.docetressabores.ejbs.ProjectBean;
import io.github.ipl.tfc.docetressabores.entities.Project;

@Path("/projects")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class ProjectService {
	@EJB ProjectBean projectBean;

	public static ProjectDTO toDTO(Project project) {
		return new ProjectDTO(
			project.getId(),
			project.getName(),
			project.getClient().getId(),
			StructureService.toDTOs(project.getStructures()));
	}

	public static List<ProjectDTO> toDTOs(List<Project> projects) {
		return projects
			.stream()
			.map(ProjectService::toDTO)
			.collect(Collectors.toList());
	}

	@GET
	@Path("/all")
	@Transactional
	public List<ProjectDTO> getAllProjectsWS() {
		return toDTOs(projectBean.getAllProjects());
	}
}
