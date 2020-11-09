package io.github.ipl.tfc.docetressabores.ws;

import io.github.ipl.tfc.docetressabores.dtos.VariantDTO;
import io.github.ipl.tfc.docetressabores.ejbs.VariantBean;
import io.github.ipl.tfc.docetressabores.entities.Variant;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.stream.Collectors;

@Path("/variants")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class VariantService
{
	@EJB
	private VariantBean variantBean;

	public static VariantDTO toDTO(Variant variant)
	{
		return new VariantDTO
		(
			variant.getCode(),
			variant.getProduct().getName(),
			variant.getName(),
			variant.getWeff_p(),
			variant.getWeff_n(),
			variant.getAr(),
			variant.getSigmaC(),
			variant.getPp(),
			variant.getMcr_p(),
			variant.getMcr_n()
		);
	}

	public static List<VariantDTO> toDTOs(List<Variant> variants)
	{
		return variants
			.stream()
			.map(VariantService::toDTO)
			.collect(Collectors.toList());
	}

	@GET
	@Path("/all")
	public List<VariantDTO> getAllVariantsWS()
	{
		return toDTOs(variantBean.getAllVariants());
	}
}
