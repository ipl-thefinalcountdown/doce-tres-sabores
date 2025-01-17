package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import io.github.ipl.tfc.docetressabores.entities.structures.LightSteelStructure;
import io.github.ipl.tfc.docetressabores.dtos.StructureDTO;
import io.github.ipl.tfc.docetressabores.entities.Material;
import io.github.ipl.tfc.docetressabores.entities.MaterialType;
import io.github.ipl.tfc.docetressabores.entities.Variant;

@Stateless
public class LightSteelStructureBean {
	@PersistenceContext EntityManager entityManager;

	public LightSteelStructure create(String name, int beamAmount, double beamLength, int beamImposedLoad, int beamSpacing, List<Integer> variantIds) {
		Supplier<Stream<Variant>> variantRange = () -> (Stream<Variant>) variantIds
			.stream()
			.map(vId -> entityManager.find(Variant.class, vId));

		if (variantRange.get().anyMatch(Objects::isNull) || name == null)
		{
			return null;
		} else {
			Material material = entityManager.find(Material.class, MaterialType.LIGHT_STEEL);

			LightSteelStructure structure = new LightSteelStructure
			(
				material, name, beamAmount, beamLength, beamImposedLoad, beamSpacing,
				variantRange.get().collect(Collectors.toSet())
			);

			entityManager.persist(structure);
			return structure;
		}
	}


	public LightSteelStructure create(StructureDTO structureDTO) {
		List<Integer> variantIds = structureDTO.getVariants().stream().map(s -> s.getId()).collect(Collectors.toList());

		if (structureDTO.getBeamAmount() == null
			|| structureDTO.getBeamLength() == null
			|| structureDTO.getBeamImposedLoad() == null
			|| structureDTO.getBeamSpacing() == null
		) {
			return null;
		}

		return create(
			structureDTO.getName(),
			structureDTO.getBeamAmount(),
			structureDTO.getBeamLength(),
			structureDTO.getBeamImposedLoad(),
			structureDTO.getBeamSpacing(),
			variantIds
		);
	}


	public LightSteelStructure update(StructureDTO structureDTO) {
		LightSteelStructure structure = findStructure(structureDTO.getId());

		if (structureDTO.getName() != null) structure.setName(structureDTO.getName());
		if (structureDTO.getBeamAmount() != null) structure.setBeamAmount(structureDTO.getBeamAmount());
		if (structureDTO.getBeamLength() != null) structure.setBeamLength(structureDTO.getBeamLength());
		if (structureDTO.getBeamImposedLoad() != null) structure.setBeamImposedLoad(structureDTO.getBeamImposedLoad());
		if (structureDTO.getBeamSpacing() != null) structure.setBeamSpacing(structureDTO.getBeamSpacing());

		Supplier<Stream<Variant>> variantRange = () -> (Stream<Variant>) structureDTO.getVariants()
			.stream()
			.map(v -> entityManager.find(Variant.class, v.getId()));

		if (variantRange.get().anyMatch(Objects::isNull)) {
			return null;
		}

		structure.setVariants(variantRange.get().collect(Collectors.toSet()));
		return structure;
	}


	public LightSteelStructure findStructure(int id) {
		return entityManager.find(LightSteelStructure.class, id);
	}

	public List<LightSteelStructure> getAllLightSteelStructures() {
		return entityManager
			.createNamedQuery("getAllLightSteelStructures", LightSteelStructure.class)
			.getResultList();
	}
}
