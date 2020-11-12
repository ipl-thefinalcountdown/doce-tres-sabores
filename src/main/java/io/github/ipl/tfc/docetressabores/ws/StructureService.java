package io.github.ipl.tfc.docetressabores.ws;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.github.ipl.tfc.docetressabores.dtos.StructureDTO;
import io.github.ipl.tfc.docetressabores.ejbs.StructureBean;
import io.github.ipl.tfc.docetressabores.entities.Structure;

@Path("/structures")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class StructureService {
	@EJB StructureBean structureBean;

	public static StructureDTO toDTO(Structure structure) {
		return new StructureDTO(
			structure.getId(),
			VariantService.toDTOs(structure.getVariants())
		);
	}

	public static List<StructureDTO> toDTOs(List<Structure> structures) {
		return structures
			.stream()
			.map(StructureService::toDTO)
			.collect(Collectors.toList());
	}

	@GET
	@Path("/all")
	@Transactional
	public List<StructureDTO> getAllStructures() {
		return toDTOs(structureBean.getAllStructures());
	}
}
