package io.github.ipl.tfc.docetressabores.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.*;

import io.github.ipl.tfc.docetressabores.entities.structures.Structure;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllMaterials",
		query = "SELECT m FROM Material m ORDER BY m.id"
	)
})
@Table(
	name = "MATERIALS"
)
public class Material {
	@Id private int id;
	@OneToMany(mappedBy = "material", cascade = CascadeType.REMOVE) protected List<Family> families;
	@OneToMany(mappedBy = "material", cascade = CascadeType.REMOVE) protected List<Structure> structures;

	public Material() {
		this.families = new ArrayList<>();
		this.structures = new ArrayList<>();
	}

	public Material(int type) {
		this.id = type;
	}


	public int getId() {
		return id;
	}
/*
	public MaterialType getType() {
		return type;
	}*/
}
