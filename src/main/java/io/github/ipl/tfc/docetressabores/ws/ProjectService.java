package io.github.ipl.tfc.docetressabores.ws;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import io.github.ipl.tfc.docetressabores.dtos.ProjectDTO;
import io.github.ipl.tfc.docetressabores.ejbs.DocumentBean;
import io.github.ipl.tfc.docetressabores.ejbs.EmailBean;
import io.github.ipl.tfc.docetressabores.ejbs.ProjectBean;
import io.github.ipl.tfc.docetressabores.ejbs.StructureBean;
import io.github.ipl.tfc.docetressabores.entities.Document;
import io.github.ipl.tfc.docetressabores.entities.Project;
import io.github.ipl.tfc.docetressabores.entities.structures.Structure;

@Path("/projects")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class ProjectService {
	@EJB ProjectBean projectBean;
	@EJB StructureBean structureBean;
	@EJB DocumentBean documentBean;
	@EJB EmailBean emailBean;
	@Context SecurityContext securityContext;

	public static final File DOCUMENT_DIR = new File("/srv/http/docetressabores/storage/");

	public static ProjectDTO toDTO(Project project, boolean critical) {
		return new ProjectDTO(
			project.getId(),
			project.getName(),
			project.getClient().getUsername(),
			project.getDesigner().getUsername(),
			project.getClient().getName(),
			project.getDesigner().getName(),
			project.getObservations(),
			project.getCompleted(),
			critical ? null : StructureService.toDTOs(project.getStructures()),
			critical ? null : DocumentService.toDTOs(project.getDocuments())
		);
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
				: Response.ok(toDTO(project, false))
			).build();
	}

	@POST
	@Path("/")
	@RolesAllowed({"Designer"})
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
	@RolesAllowed({"Designer", "Client"})
	@Transactional
	public Response updateProjectWS(@PathParam("id") int id, ProjectDTO projectDTO) {
		Principal principal = securityContext.getUserPrincipal();

		Project project = projectBean.findProject(id);

		if (principal == null
			|| (project != null
				&& (!project.getDesigner().getUsername().equals(principal.getName())
					&& !project.getClient().getUsername().equals(principal.getName())
				)
			)
		) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		if (project == null) return Response.status(Response.Status.BAD_REQUEST).build();

		if (!project.getCompleted() && securityContext.isUserInRole("Client"))
			return Response.status(Response.Status.FORBIDDEN).build();


		if (projectDTO.getCompleted() != null && projectDTO.getCompleted() && !project.getCompleted()) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Hi %s,", project.getClient().getName()));
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append(String.format("We inform you that your project \"%s\" ", project.getName()));
			sb.append(String.format("with our designer %s has just been completed ", project.getDesigner().getName()));
			sb.append(String.format("and is ready for your review."));
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append(String.format("Thanks,"));
			sb.append(System.lineSeparator());
			sb.append(String.format("Doce Tres Sabores Team"));

			emailBean.send(
				project.getClient().getEmail(),
				"Project " + project.getName() + " status",
				sb.toString());
		}

		projectDTO.setId(id);
		projectBean.update(projectDTO);

		return Response.ok(toDTO(project, true)).build();
	}

	@DELETE
	@Path("/{id}")
	@RolesAllowed({"Designer"})
	@Transactional
	public Response deleteProjectWS(@PathParam("id") int id) {
		Principal principal = securityContext.getUserPrincipal();

		Project project = projectBean.findProject(id);

		if (principal == null
			|| (project != null
				&& !project.getDesigner().getUsername().equals(principal.getName())
			)
		) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		return (
			projectBean.delete(id) == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.noContent()
		).build();
	}

	@GET
	@Path("/{id}/structures")
	@Transactional
	public Response getStructuresWS(@PathParam("id") int id) {
		Project project = projectBean.findProject(id);

		return (
			project == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.ok(StructureService.toDTOs(project.getStructures()))
		).build();
	}

	@PUT
	@Path("/{project_id}/structures/{structure_id}")
	@RolesAllowed({"Designer"})
	@Transactional
	public Response putStructureWS(
		@PathParam("project_id") int projectId,
		@PathParam("structure_id") int structureId
	) {
		Project project = projectBean.findProject(projectId);

		if (project == null) return Response.status(Response.Status.BAD_REQUEST).build();

		Structure structure = structureBean.findStructure(structureId);

		if (structure == null) return Response.status(Response.Status.BAD_REQUEST).build();

		if (project.getStructures().contains(structure)) return Response.status(Response.Status.BAD_REQUEST).build();

		project.addStructure(structure);
		return Response.noContent().build();
	}

	@DELETE
	@Path("/{project_id}/structures/{structure_id}")
	@RolesAllowed({"Designer"})
	@Transactional
	public Response deleteStructureWS(
		@PathParam("project_id") int projectId,
		@PathParam("structure_id") int structureId
	) {
		Principal principal = securityContext.getUserPrincipal();

		Project project = projectBean.findProject(projectId);

		if (principal == null
			|| (project != null
				&& project.getDesigner().getUsername() != principal.getName()
			)
		) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		if (project == null) return Response.status(Response.Status.BAD_REQUEST).build();

		Optional<Structure> structure = project.getStructures().stream().filter(s -> s.getId() == structureId).findFirst();

		if (!structure.isPresent()) return Response.status(Response.Status.BAD_REQUEST).build();

		project.removeStructure(structure.get());
		return Response.noContent().build();
	}

	@GET
	@Path("/{id}/documents")
	@Transactional
	public Response getDocumentsWS(@PathParam("id") int id) {
		Project project = projectBean.findProject(id);

		return (
			project == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.ok(DocumentService.toDTOs(project.getDocuments()))
		).build();
	}

	@POST
	@Path("/{id}/documents")
	@RolesAllowed({"Client"})
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Transactional
	public Response uploadDocumentWS(@PathParam("id") int id, MultipartFormDataInput input) {
		Principal principal = securityContext.getUserPrincipal();

		Project project = projectBean.findProject(id);

		if (principal == null
			|| (project != null
				&& !project.getClient().getUsername().equals(principal.getName())
			)
		) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		if (project == null) return Response.status(Response.Status.BAD_REQUEST).build();

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");

		try {
			for (InputPart inputPart : inputParts) {
				String filename = Arrays
					.asList(inputPart.getHeaders()
						.getFirst("Content-Disposition")
						.split(";"))
					.stream()
					.filter(str -> str.trim().startsWith("filename"))
					.map(str -> str.split("=")[1])
					.findFirst()
					.get()
					.replaceAll("\"", "");

				InputStream is = inputPart.getBody(InputStream.class, null);
				byte[] buff = IOUtils.toByteArray(is);

				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				byteArrayOutputStream.writeBytes(buff);

				File file;
				Document document = documentBean.getProjectDocumentBy(id, filename);

				if (document == null) {
					do { file = new File(DOCUMENT_DIR, UUID.randomUUID().toString() + "-" + filename); } while(file.exists());
					documentBean.create(id, filename, file.getName());
				}
				else file = new File(DOCUMENT_DIR, document.getFilePath());

				try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
					byteArrayOutputStream.writeTo(fileOutputStream);
				}
			}
		} catch(IOException e) {
			System.err.println(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.noContent().build();
	}

	@GET
	@Path("/{project_id}/documents/{document_id}")
	@RolesAllowed({"Designer", "Client"})
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Transactional
	public Response downloadDocumentWS(
		@PathParam("project_id") int project_id,
		@PathParam("document_id") int document_id
	) {
		Principal principal = securityContext.getUserPrincipal();

		Project project = projectBean.findProject(project_id);

		if (principal == null
			|| (project != null
				&& (!project.getDesigner().getUsername().equals(principal.getName())
					&& !project.getClient().getUsername().equals(principal.getName())
				)
			)
		) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		if (project == null) return Response.status(Response.Status.BAD_REQUEST).build();

		Document document = documentBean.findDocument(document_id);

		return (
			(document == null || !document.getProject().equals(project))
				? Response.status(Response.Status.BAD_REQUEST)
				: Response
					.ok(new File(DOCUMENT_DIR, document.getFilePath()))
					.header("Content-Disposition", "attachment;filename=" + document.getFileName())
		).build();
	}
}
