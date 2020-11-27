package io.github.ipl.tfc.docetressabores.ws;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.github.ipl.tfc.docetressabores.dtos.StructureDTO;
import io.github.ipl.tfc.docetressabores.ejbs.LightSteelStructureBean;
import io.github.ipl.tfc.docetressabores.entities.structures.LightSteelStructure;

public class LightSteelStructureService {
	@EJB LightSteelStructureBean lightSteelStructureBean;

	public static StructureDTO toDTO(LightSteelStructure structure, boolean critical) {
		return new StructureDTO(
			structure.getId(),
			structure.getMaterial().getId(),
			critical ? null : structure.getBeamAmount(),
			critical ? null : structure.getBeamLength(),
			critical ? null : structure.getBeamImposedLoad(),
			critical ? null : structure.getBeamSpacing(),
			null,
			critical ? null : VariantService.toDTOs(structure.getVariants()));
	}

	public static StructureDTO toDTO(LightSteelStructure structure) {
		return toDTO(structure, true);
	}

	public static List<StructureDTO> toDTOs(List<LightSteelStructure> structures, boolean critical) {
		return structures
			.stream()
			.map(s -> LightSteelStructureService.toDTO(s, critical))
			.collect(Collectors.toList());
	}

	public static List<StructureDTO> toDTOs(List<LightSteelStructure> structure) {
		return toDTOs(structure, true);
	}
}
