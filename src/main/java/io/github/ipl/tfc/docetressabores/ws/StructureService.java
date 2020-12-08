package io.github.ipl.tfc.docetressabores.ws;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.github.ipl.tfc.docetressabores.dtos.StructureDTO;
import io.github.ipl.tfc.docetressabores.dtos.VariantDTO;
import io.github.ipl.tfc.docetressabores.ejbs.LightSteelStructureBean;
import io.github.ipl.tfc.docetressabores.ejbs.MaterialBean;
import io.github.ipl.tfc.docetressabores.ejbs.SimulationBean;
import io.github.ipl.tfc.docetressabores.ejbs.SlabStructureBean;
import io.github.ipl.tfc.docetressabores.ejbs.StructureBean;
import io.github.ipl.tfc.docetressabores.ejbs.VariantBean;
import io.github.ipl.tfc.docetressabores.entities.structures.*;
import io.github.ipl.tfc.docetressabores.entities.MaterialType;
import io.github.ipl.tfc.docetressabores.entities.Variant;

@Path("/structures")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class StructureService {
	@EJB StructureBean structureBean;
	@EJB LightSteelStructureBean lightSteelStructureBean;
	@EJB SlabStructureBean slabStructureBean;
	@EJB MaterialBean materialBean;
	@EJB VariantBean variantBean;
	@EJB SimulationBean simulationBean;

	public static StructureDTO toDTO(Structure structure, boolean critical) {
		return new StructureDTO(
			structure.getId(),
			structure.getMaterial().getId(),
			critical ? null : structure.getBeamAmount(),
			critical ? null : structure.getBeamLength(),
			critical ? null : structure.getBeamImposedLoad(),
			null,
			null,
			critical ? null : VariantService.toDTOs(structure.getVariants())
		);
	}

	public static StructureDTO toDTO(Structure structure) {
		return toDTO(structure, true);
	}

	public static List<StructureDTO> toDTOs(List<Structure> structures, boolean critical) {
		return structures
			.stream()
			.map(s -> StructureService.toDTO(s, critical))
			.collect(Collectors.toList());
	}

	public static List<StructureDTO> toDTOs(List<Structure> structures) {
		return toDTOs(structures, true);
	}

	@GET
	@Path("/")
	@Transactional
	public Response getAllStructuresWS(@QueryParam("type") Integer type) {
		if (type == null)
			return Response.ok(toDTOs(structureBean.getAllStructures())).build();

		switch (type) {
			case MaterialType.LIGHT_STEEL:
			case MaterialType.PROFILED_SHEETING:
			case MaterialType.SLAB:
			case MaterialType.SANDWICH_PANEL:
				return Response.ok(toDTOs(structureBean.getStructuresFilteredBy(type))).build();
			default:
				return Response.ok(toDTOs(structureBean.getAllStructures())).build();
		}
	}

	@GET
	@Path("/{id}")
	@Transactional
	public Response getStructureWS(@PathParam("id") int id) {
		Structure structure = structureBean.findStructure(id);

		if (structure == null)
			return Response.status(Response.Status.BAD_REQUEST).build();

		switch (structure.getMaterial().getId()) {
			case MaterialType.LIGHT_STEEL:
				return Response.ok(LightSteelStructureService.toDTO(lightSteelStructureBean.findStructure(id), false)).build();
			case MaterialType.SLAB:
				return Response.ok(SlabStructureService.toDTO(slabStructureBean.findStructure(id), false)).build();
			default:
				return Response.ok(toDTO(structure, false)).build();
		}
	}

	@POST
	@Path("/")
	@Transactional
	public Response postStructureWS(StructureDTO structureDTO) {
		switch (structureDTO.getMaterialId()) {
			case MaterialType.LIGHT_STEEL:
				LightSteelStructure lightSteelStructure = lightSteelStructureBean.create(structureDTO);
				return (
					lightSteelStructure == null
						? Response.status(Response.Status.BAD_REQUEST)
						: Response.ok(LightSteelStructureService.toDTO(lightSteelStructure))
				).build();
			case MaterialType.SLAB:
				SlabStructure slabStructure = slabStructureBean.create(structureDTO);
				return (
					slabStructure == null
						? Response.status(Response.Status.BAD_REQUEST)
						: Response.ok(SlabStructureService.toDTO(slabStructure))
				).build();
			default:
				Structure structure = structureBean.create(structureDTO);
				return (
					structure == null
						? Response.status(Response.Status.BAD_REQUEST)
						: Response.ok(toDTO(structure))
				).build();
		}
	}

	@PUT
	@Path("/{id}")
	@Transactional
	public Response putStructureWS(@PathParam("id") int id, StructureDTO structureDTO) {
		Structure structure = structureBean.findStructure(id);

		if (structure == null) return Response.status(Response.Status.BAD_REQUEST).build();

		structureDTO.setId(id);
		switch (structure.getMaterial().getId()) {
			case MaterialType.LIGHT_STEEL:
				LightSteelStructure lightSteelStructure = lightSteelStructureBean.update(structureDTO);
				return (
					lightSteelStructure == null
						? Response.status(Response.Status.BAD_REQUEST)
						: Response.ok(LightSteelStructureService.toDTO(lightSteelStructure))
				).build();
			case MaterialType.SLAB:
				SlabStructure slabStructure = slabStructureBean.update(structureDTO);
				return (
					slabStructure == null
						? Response.status(Response.Status.BAD_REQUEST)
						: Response.ok(SlabStructureService.toDTO(slabStructure))
				).build();
			default:
				structure = structureBean.update(structureDTO);
				return (
					structure == null
						? Response.status(Response.Status.BAD_REQUEST)
						: Response.ok(toDTO(structure))
				).build();
		}
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public Response deleteStructureWS(@PathParam("id") int id) {
		return (
			structureBean.delete(id)
				? Response.noContent()
				: Response.status(Response.Status.BAD_REQUEST)
		).build();
	}

	@GET
	@Path("/{id}/simulation")
	@Transactional
	public Response simulationWS(@PathParam("id") int id, List<VariantDTO> variantsDTOs) {
		Structure structure = structureBean.findStructure(id);

		if (structure == null) return Response.status(Response.Status.BAD_REQUEST).build();

		Supplier<Stream<Variant>> variantStream = () -> variantsDTOs
			.stream()
			.map(v -> variantBean.findVariant(v.getId()));

		if (variantStream.get().anyMatch(Objects::isNull)) return Response.status(Response.Status.BAD_REQUEST).build();

		return Response.ok(VariantService.toDTOs(
			variantStream.get()
			.filter(Objects::nonNull)
			.filter(v -> simulationBean.simulateVariant(structure.getBeamAmount(), structure.getBeamLength(), structure.getBeamImposedLoad(), v))
			.collect(Collectors.toList()))
		).build();
	}
}
