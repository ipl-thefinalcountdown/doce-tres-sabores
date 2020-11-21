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
import javax.ws.rs.core.Response;

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
			project.getClient().getName(),
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
	@Path("/")
	@Transactional
	public Response getAllProjectsWS(@DefaultValue("") @QueryParam("filter") String filter) {
		return Response.ok(toDTOs(projectBean.getAllProjects(filter))).build();
	}

	@GET
	@Path("/{id}")
	@Transactional
	public Response getProjectWS(@PathParam("id") int id) {
		Project project = projectBean.findProject(id);

		return (
			project == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.ok(toDTO(project))
			).build();
	}

	@POST
	@Path("/")
	@Transactional
	public Response postProjectWS(ProjectDTO projectDTO) {
		Project project = projectBean.create(projectDTO);

		return (
			project == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.ok(toDTO(project, true))
			).build();
	}

	@PUT
	@Path("/{id}")
	@Transactional
	public Response updateProjectWS(@PathParam("id") int id, ProjectDTO projectDTO) {
		Project project = projectBean.update(projectDTO);

		return (
			project == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.ok(toDTO(project, true))
		).build();
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public Response deleteProjectWS(@PathParam("id") int id) {
		return (
			projectBean.delete(id) == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.noContent()
		).build();
	}
}
