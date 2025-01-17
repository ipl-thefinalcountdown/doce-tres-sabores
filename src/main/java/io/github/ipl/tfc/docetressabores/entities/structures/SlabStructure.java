package io.github.ipl.tfc.docetressabores.entities.structures;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import io.github.ipl.tfc.docetressabores.entities.Material;
import io.github.ipl.tfc.docetressabores.entities.Variant;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllSlabStructures",
		query = "SELECT s FROM SlabStructure s "
			+ "ORDER BY s.id"
	)
})
public class SlabStructure extends Structure {
	private int maximumHeight;

	public SlabStructure() {
		super();
	}

	public SlabStructure(
		Material material,
		String name,
		int beamAmount,
		double beamLength,
		int beamImposedLoad,
		int maximumHeight,
		@NotNull Set<Variant> variants
	) {
		super(material, name, beamAmount, beamLength, beamImposedLoad, variants);
		this.maximumHeight = maximumHeight;
	}

	public int getMaximumHeight() {
		return maximumHeight;
	}

	public void setMaximumHeight(int maximumHeight) {
		this.maximumHeight = maximumHeight;
	}
}
