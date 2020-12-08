package io.github.ipl.tfc.docetressabores.entities.structures;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import io.github.ipl.tfc.docetressabores.entities.Material;
import io.github.ipl.tfc.docetressabores.entities.Variant;

@Entity
@NamedQueries({
	@NamedQuery(
		name = "getAllLightSteelStructures",
		query = "SELECT s FROM LightSteelStructure s "
			+ "ORDER BY s.id"
	)
})
public class LightSteelStructure extends Structure {
	private int beamSpacing;

	public LightSteelStructure() {
		super();
	}

	public LightSteelStructure(
		Material material,
		int beamAmount,
		int beamLength,
		int beamImposedLoad,
		int beamSpacing,
		@NotNull List<Variant> variants
	) {
		super(material, beamAmount, beamLength, beamImposedLoad, variants);
		this.beamSpacing = beamSpacing;
	}


	public int getBeamSpacing() {
		return beamSpacing;
	}

	public void setBeamSpacing(int beamSpacing) {
		this.beamSpacing = beamSpacing;
	}
}
