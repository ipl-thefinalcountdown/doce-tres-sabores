package io.github.ipl.tfc.docetressabores.ws;

import io.github.ipl.tfc.docetressabores.dtos.VariantDTO;
import io.github.ipl.tfc.docetressabores.ejbs.VariantBean;
import io.github.ipl.tfc.docetressabores.entities.Variant;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/variants")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class VariantService
{
	@EJB
	private VariantBean variantBean;

	public static VariantDTO toDTO(Variant variant, boolean critical)
	{
		return new VariantDTO
		(
			variant.getId(),
			critical ? null : variant.getProduct().getId(),
			variant.getName(),
			critical ? null : variant.getWeff_p(),
			critical ? null : variant.getWeff_n(),
			critical ? null : variant.getAr(),
			critical ? null : variant.getSigmaC(),
			critical ? null : variant.getPp(),
			critical ? null : variant.getMcr_p(),
			critical ? null : variant.getMcr_n()
		);
	}

	public static List<VariantDTO> toDTOs(List<Variant> variants, boolean critical)
	{
		return variants
			.stream()
			.map(v -> toDTO(v, critical))
			.collect(Collectors.toList());
	}

	public static List<VariantDTO> toDTOs(List<Variant> variants) {
		return toDTOs(variants, true);
	}

	@GET
	@Path("/")
	@Transactional
	public Response getAllVariantsWS()
	{
		return Response.ok(toDTOs(variantBean.getAllVariants())).build();
	}
}
