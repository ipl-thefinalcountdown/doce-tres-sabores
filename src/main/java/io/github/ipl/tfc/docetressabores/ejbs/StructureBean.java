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

import io.github.ipl.tfc.docetressabores.dtos.StructureDTO;
import io.github.ipl.tfc.docetressabores.entities.Material;
import io.github.ipl.tfc.docetressabores.entities.Project;
import io.github.ipl.tfc.docetressabores.entities.structures.Structure;
import io.github.ipl.tfc.docetressabores.entities.Variant;

@Stateless
public class StructureBean {
	// TODO: documentation
	@PersistenceContext EntityManager entityManager;

	public Structure create(int materialId, String name, int beamAmount, int beamLength, int beamImposedLoad, List<Integer> variantIds) {
		Supplier<Stream<Variant>> variantRange = () -> (Stream<Variant>) variantIds
			.stream()
			.map(vId -> entityManager.find(Variant.class, vId));

		if (variantRange.get().anyMatch(Objects::isNull))
		{
			return null;
		} else {
			Material material = entityManager.find(Material.class, materialId);
			Structure structure = new Structure(material, name, beamAmount, beamLength, beamImposedLoad, variantRange.get().collect(Collectors.toList()));
			entityManager.persist(structure);
			return structure;
		}
	}

	public Structure create(StructureDTO structureDTO) {
		List<Integer> variantIds = structureDTO.getVariants().stream().map(s -> s.getId()).collect(Collectors.toList());
		return create(structureDTO.getMaterialId(), structureDTO.getName(), structureDTO.getBeamAmount(), structureDTO.getBeamLength(), structureDTO.getBeamImposedLoad(), variantIds);
	}

	public Structure update(StructureDTO structureDTO) {
		Structure structure = findStructure(structureDTO.getId());

		if (structureDTO.getBeamAmount() != null) structure.setBeamAmount(structureDTO.getBeamAmount());
		if (structureDTO.getBeamLength() != null) structure.setBeamLength(structureDTO.getBeamLength());

		Supplier<Stream<Variant>> variantRange = () -> (Stream<Variant>) structureDTO.getVariants()
			.stream()
			.map(v -> entityManager.find(Variant.class, v.getId()));

			if (variantRange.get().anyMatch(Objects::isNull)) {
				return null;
			}

		structure.setVariants(variantRange.get().collect(Collectors.toList()));
		return structure;
	}

	public boolean delete(int id) {
		Structure structure = findStructure(id);

		if (structure == null) return false;

		List<Project> projects = entityManager
			.createNamedQuery("getProjectsFromStructureId", Project.class)
			.setParameter("structure_id", id)
			.getResultList();

		if (!projects.isEmpty()) return false;

		entityManager.remove(structure);
		return true;
	}

	public Structure findStructure(int id) {
		return entityManager.find(Structure.class, id);
	}

	public List<Structure> getAllStructures(@NotNull String filter) {
		return entityManager
			.createNamedQuery("getAllStructures", Structure.class)
			.setParameter("filter", "%"+filter+"%")
			.getResultList();
	}

	public List<Structure> getStructuresFilteredBy(@NotNull Integer id, @NotNull String filter) {
		return entityManager
			.createNamedQuery("getStructuresFilteredByMaterialId", Structure.class)
			.setParameter("materialId", id)
			.setParameter("filter", "%"+filter+"%")
			.getResultList();
	}
}
