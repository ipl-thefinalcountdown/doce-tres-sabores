package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.List;
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

	public SlabStructure create(int beamAmount, int beamLength, int beamImposedLoad, int maximumHeight, List<Integer> variantIds) {
		Supplier<Stream<Variant>> variantRange = () -> (Stream<Variant>) variantIds
			.stream()
			.map(vId -> entityManager.find(Variant.class, vId));

		if (variantRange.get().anyMatch(Objects::isNull))
		{
			return null;
		} else {
			Material material = entityManager.find(Material.class, MaterialType.SLAB);

			SlabStructure structure = new SlabStructure
			(
				material, beamAmount, beamLength, beamImposedLoad, maximumHeight,
				variantRange.get().collect(Collectors.toList())
			);

			entityManager.persist(structure);
			return structure;
		}
	}


	public SlabStructure create(StructureDTO structureDTO) {
		List<Integer> variantIds = structureDTO.getVariants().stream().map(s -> s.getId()).collect(Collectors.toList());
		return create(structureDTO.getBeamAmount(), structureDTO.getBeamLength(), structureDTO.getBeamImposedLoad(), structureDTO.getMaximumHeight(), variantIds);
	}


	public SlabStructure update(StructureDTO structureDTO) {
		SlabStructure structure = findStructure(structureDTO.getId());

		if (structureDTO.getBeamAmount() != null) structure.setBeamAmount(structureDTO.getBeamAmount());
		if (structureDTO.getBeamLength() != null) structure.setBeamLength(structureDTO.getBeamLength());
		if (structureDTO.getMaximumHeight() != null) structure.setMaximumHeight(structureDTO.getMaximumHeight());

		Supplier<Stream<Variant>> variantRange = () -> (Stream<Variant>) structureDTO.getVariants()
			.stream()
			.map(v -> entityManager.find(Variant.class, v.getId()));

		if (variantRange.get().anyMatch(Objects::isNull)) {
			return null;
		}

		structure.setVariants(variantRange.get().collect(Collectors.toList()));
		return structure;
	}


	public SlabStructure findStructure(int id) {
		return entityManager.find(SlabStructure.class, id);
	}
}
