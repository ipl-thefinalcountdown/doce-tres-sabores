package io.github.ipl.tfc.docetressabores.entities.structures;

import java.util.List;

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
		int beamAmount,
		int beamLength,
		int beamImposedLoad,
		int maximumHeight,
		@NotNull List<Variant> variants
	) {
		super(material, beamAmount, beamLength, beamImposedLoad, variants);
		this.maximumHeight = maximumHeight;
	}

	public int getMaximumHeight() {
		return maximumHeight;
	}

	public void setMaximumHeight(int maximumHeight) {
		this.maximumHeight = maximumHeight;
	}
}
