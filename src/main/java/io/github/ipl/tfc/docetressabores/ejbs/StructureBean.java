package io.github.ipl.tfc.docetressabores.ejbs;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.github.ipl.tfc.docetressabores.entities.Structure;
import io.github.ipl.tfc.docetressabores.entities.Variant;

@Stateless
public class StructureBean {
	// TODO: documentation
	@PersistenceContext EntityManager entityManager;

	// TODO: throws
	public Structure create(List<Integer> variantIds) {
		Supplier<Stream<Variant>> variantRange = () -> (Stream<Variant>) variantIds
				.stream()
				.map(vId -> entityManager.find(Variant.class, vId));

		if (variantRange.get().filter(v -> v == null).findAny().isPresent())
		{
			// FIXME: throw exception
			return null;
		} else {
			Structure structure = new Structure(variantRange.get().collect(Collectors.toList()));
			entityManager.persist(structure);
			return structure;
		}
	}

	public Structure findStructure(int id) {
		return entityManager.find(Structure.class, id);
	}

	public List<Structure> getAllStructures() {
		return entityManager
			.createNamedQuery("getAllStructures", Structure.class)
			.getResultList();
	}
}
