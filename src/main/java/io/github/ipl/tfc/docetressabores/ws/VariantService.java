package io.github.ipl.tfc.docetressabores.ws;

import io.github.ipl.tfc.docetressabores.dtos.VariantDTO;
import io.github.ipl.tfc.docetressabores.ejbs.VariantBean;
import io.github.ipl.tfc.docetressabores.entities.MaterialType;
import io.github.ipl.tfc.docetressabores.entities.Variant;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

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
			variant.getProduct().getId(),
			variant.getProduct().getName(),
			variant.getName(),
			variant.getWeff_p(),
			variant.getWeff_n(),
			variant.getAr(),
			variant.getSigmaC(),
			variant.getPp(),
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
	public Response getAllVariantsWS(
		@QueryParam("type") Integer type,
		@DefaultValue("") @QueryParam("filter") String filter)
	{
		if(type == null)
			return Response.ok(toDTOs(variantBean.getAllVariants(filter))).build();

		switch (type) {
			case MaterialType.LIGHT_STEEL:
			case MaterialType.PROFILED_SHEETING:
			case MaterialType.SLAB:
			case MaterialType.SANDWICH_PANEL:
				return Response.ok(toDTOs(variantBean.getVariantsFilteredBy(type, filter))).build();
			default:
				return Response.ok(toDTOs(variantBean.getAllVariants(filter))).build();
		}
	}

	@GET
	@Path("/{id}")
	@Transactional
	public Response getVariantWS(@PathParam("id") int id) {
		Variant variant = variantBean.findVariant(id);

		return (variant == null ? Response.status(Response.Status.BAD_REQUEST) : Response.ok(toDTO(variant, false)))
				.build();
	}

	@POST
	@Path("/")
	@Transactional
	public Response postVariantWS(VariantDTO variantDTO) {
		Variant variant = variantBean.create(variantDTO);

		return (variant == null ? Response.status(Response.Status.BAD_REQUEST) : Response.ok(toDTO(variant, true)))
				.build();
	}

	@PUT
	@Path("/{id}")
	@Transactional
	public Response updateVariantWS(@PathParam("id") int id, VariantDTO variantDTO) {
		variantDTO.setId(id);
		Variant variant = variantBean.update(variantDTO);

		return (
			variant == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.ok(toDTO(variant, true))
		).build();
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public Response deleteVariantWS(@PathParam("id") int id) {
		return (
			variantBean.delete(id)
				? Response.noContent()
				: Response.status(Response.Status.BAD_REQUEST)
		).build();
	}
}
