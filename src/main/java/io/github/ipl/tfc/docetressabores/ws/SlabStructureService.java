package io.github.ipl.tfc.docetressabores.ws;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ws.rs.*;

import io.github.ipl.tfc.docetressabores.dtos.StructureDTO;
import io.github.ipl.tfc.docetressabores.ejbs.SlabStructureBean;
import io.github.ipl.tfc.docetressabores.entities.structures.SlabStructure;

public class SlabStructureService {
	@EJB SlabStructureBean slabStructureBean;

	public static StructureDTO toDTO(SlabStructure structure, boolean critical) {
		return new StructureDTO(
			structure.getId(),
			structure.getMaterial().getId(),
			structure.getName(),
			critical ? null : structure.getBeamAmount(),
			critical ? null : structure.getBeamLength(),
			critical ? null : structure.getBeamImposedLoad(),
			null,
			critical ? null : structure.getMaximumHeight(),
			critical ? null : VariantService.toDTOs(structure.getVariants()));
	}

	public static StructureDTO toDTO(SlabStructure structure) {
		return toDTO(structure, true);
	}

	public static List<StructureDTO> toDTOs(List<SlabStructure> structures, boolean critical) {
		return structures
			.stream()
			.map(s -> SlabStructureService.toDTO(s, critical))
			.collect(Collectors.toList());
	}

	public static List<StructureDTO> toDTOs(List<SlabStructure> structure) {
		return toDTOs(structure, true);
	}
}
