package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.dtos.StructureDTO;
import io.github.ipl.tfc.docetressabores.entities.Material;
import io.github.ipl.tfc.docetressabores.entities.MaterialType;
import io.github.ipl.tfc.docetressabores.entities.Variant;
import io.github.ipl.tfc.docetressabores.entities.structures.SlabStructure;

@Stateless
public class SlabStructureBean {
	@PersistenceContext EntityManager entityManager;

	public SlabStructure create(String name, int beamAmount, double beamLength, int beamImposedLoad, int maximumHeight, List<Integer> variantIds) {
		Supplier<Stream<Variant>> variantRange = () -> (Stream<Variant>) variantIds
			.stream()
			.map(vId -> entityManager.find(Variant.class, vId));

		if (variantRange.get().anyMatch(Objects::isNull) || name == null)
		{
			return null;
		} else {
			Material material = entityManager.find(Material.class, MaterialType.SLAB);

			SlabStructure structure = new SlabStructure
			(
				material, name, beamAmount, beamLength, beamImposedLoad, maximumHeight,
				variantRange.get().collect(Collectors.toSet())
			);

			entityManager.persist(structure);
			return structure;
		}
	}


	public SlabStructure create(StructureDTO structureDTO) {
		List<Integer> variantIds = structureDTO.getVariants().stream().map(s -> s.getId()).collect(Collectors.toList());

		if (structureDTO.getBeamAmount() == null
			|| structureDTO.getBeamLength() == null
			|| structureDTO.getBeamImposedLoad() == null
			|| structureDTO.getMaximumHeight() == null
		) {
			return null;
		}

		return create(
			structureDTO.getName(),
			structureDTO.getBeamAmount(),
			structureDTO.getBeamLength(),
			structureDTO.getBeamImposedLoad(),
			structureDTO.getMaximumHeight(),
			variantIds
		);
	}


	public SlabStructure update(StructureDTO structureDTO) {
		SlabStructure structure = findStructure(structureDTO.getId());

		if (structureDTO.getName() != null) structure.setName(structureDTO.getName());
		if (structureDTO.getBeamAmount() != null) structure.setBeamAmount(structureDTO.getBeamAmount());
		if (structureDTO.getBeamLength() != null) structure.setBeamLength(structureDTO.getBeamLength());
		if (structureDTO.getBeamImposedLoad() != null) structure.setBeamImposedLoad(structureDTO.getBeamImposedLoad());
		if (structureDTO.getMaximumHeight() != null) structure.setMaximumHeight(structureDTO.getMaximumHeight());

		Supplier<Stream<Variant>> variantRange = () -> (Stream<Variant>) structureDTO.getVariants()
			.stream()
			.map(v -> entityManager.find(Variant.class, v.getId()));

		if (variantRange.get().anyMatch(Objects::isNull)) {
			return null;
		}

		structure.setVariants(variantRange.get().collect(Collectors.toSet()));
		return structure;
	}


	public SlabStructure findStructure(int id) {
		return entityManager.find(SlabStructure.class, id);
	}
}
