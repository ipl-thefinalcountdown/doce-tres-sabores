package io.github.ipl.tfc.docetressabores.entities.structures;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import io.github.ipl.tfc.docetressabores.entities.Material;
import io.github.ipl.tfc.docetressabores.entities.Project;
import io.github.ipl.tfc.docetressabores.entities.Variant;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllStructures",
		query = "SELECT s FROM Structure s "
			+ "WHERE UPPER(s.name) LIKE UPPER(:filter) OR "
			+ "CAST(s.id AS string) LIKE :filter "
			+ "ORDER BY s.id"
	),
	@NamedQuery(
		name = "getStructuresFilteredByMaterialId",
		query = "SELECT s FROM Structure s "
			+ "WHERE s.material.id = :materialId "
			+ "AND (UPPER(s.name) LIKE UPPER(:filter) OR "
			+ "CAST(s.id AS string) LIKE :filter) "
			+ "ORDER BY s.id"
	)
})
@Table(name = "STRUCTURES")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Structure {
	// TODO: documentation
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) protected int id;
	@NotNull @NotEmpty private String name;

	@ManyToMany(cascade = {
		CascadeType.PERSIST,
		CascadeType.MERGE
	})
	@JoinTable(
		name = "STRUCTURE_VARIANT",
		joinColumns = @JoinColumn(name = "structure_id"),
		inverseJoinColumns = @JoinColumn(name = "variant_id")
	)
	protected Set<Variant> variants;

	@ManyToMany(mappedBy = "structures") Set<Project> projects;

	@ManyToOne @JoinColumn(name = "MATERIAL_TYPE") protected Material material;
	protected int beamAmount;
	protected int beamLength;
	protected int beamImposedLoad;

	public Structure() {
		variants = new HashSet<>();
		projects = new HashSet<>();
	}

	public Structure(
		Material material,
		String name,
		int beamAmount,
		int beamLength,
		int beamImposedLoad,
		@NotNull Set<Variant> variants
	) {
		this.material = material;
		this.name = name;
		this.beamAmount = beamAmount;
		this.beamLength = beamLength;
		this.beamImposedLoad = beamImposedLoad;
		this.variants = variants;
	}


	// getters
	public int getId() {
		return id;
	}

	public Material getMaterial() {
		return material;
	}

	public int getBeamAmount() {
		return beamAmount;
	}

	public int getBeamLength() {
		return beamLength;
	}

	public int getBeamImposedLoad() {
		return beamImposedLoad;
	}

	public List<Variant> getVariants() {
		return variants.stream().collect(Collectors.toList());
	}

	public String getName() {
		return name;
	}

	public List<Project> getProjects() {
		return projects.stream().collect(Collectors.toList());
	}

	// setters
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public void setBeamAmount(int beamAmount) {
		this.beamAmount = beamAmount;
	}

	public void setBeamLength(int beamLength) {
		this.beamLength = beamLength;
	}

	public void setBeamImposedLoad(int beamImposedLoad) {
		this.beamImposedLoad = beamImposedLoad;
	}

	public void setVariants(Set<Variant> variants) {
		this.variants = variants;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}


	public void addVariant(Variant variant) {
		variants.add(variant);
	}

	public void removeVariant(Variant variant) {
		variants.remove(variant);
	}

	public void addProject(Project project) {
		this.projects.add(project);
		project.addStructure(this);
	}

	public void removeProject(Project project) {
		System.out.println(project.getId());
		this.projects.remove(project);
		project.removeStructure(this);
	}
}
