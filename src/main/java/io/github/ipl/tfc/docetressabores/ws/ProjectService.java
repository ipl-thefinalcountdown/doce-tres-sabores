package io.github.ipl.tfc.docetressabores.ws;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.github.ipl.tfc.docetressabores.dtos.ProjectDTO;
import io.github.ipl.tfc.docetressabores.ejbs.ProjectBean;
import io.github.ipl.tfc.docetressabores.entities.Project;

@Path("/projects")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class ProjectService {
	@EJB ProjectBean projectBean;

	public static ProjectDTO toDTO(Project project, boolean critical) {
		return new ProjectDTO(
			project.getId(),
			project.getName(),
			project.getClient().getId(),
			critical ? null : StructureService.toDTOs(project.getStructures()));
	}

	public static ProjectDTO toDTO(Project project) {
		return toDTO(project, true);
	}

	public static List<ProjectDTO> toDTOs(List<Project> projects, boolean critical) {
		return projects
			.stream()
			.map(p -> toDTO(p, critical))
			.collect(Collectors.toList());
	}

	public static List<ProjectDTO> toDTOs(List<Project> projects) {
		return toDTOs(projects, true);
	}

	@GET
	@Path("/all")
	@Transactional
	public List<ProjectDTO> getAllProjectsWS(@DefaultValue("") @QueryParam("name") String name) {
		return toDTOs(projectBean.getAllProjects(name));
	}

	@GET
	@Path("/{id}")
	@Transactional
	public ProjectDTO getProjectWS(@PathParam("id") int id) {
		return toDTO(projectBean.findProject(id));
	}

	@POST
	@Path("/")
	@Transactional
	public void postProjectWS(ProjectDTO projectDTO) {
		projectBean.create(projectDTO);
	}

	@PUT
	@Path("/{id}")
	@Transactional
	public void updateProjectWS(@PathParam("id") int id, ProjectDTO projectDTO) {
		projectBean.update(projectDTO);
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public void deleteProjectWS(@PathParam("id") int id) {
		projectBean.delete(id);
	}
}
