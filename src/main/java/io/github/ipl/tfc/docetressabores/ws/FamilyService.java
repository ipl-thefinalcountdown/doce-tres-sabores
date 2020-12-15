package io.github.ipl.tfc.docetressabores.ws;

import io.github.ipl.tfc.docetressabores.entities.Family;
import io.github.ipl.tfc.docetressabores.dtos.FamilyDTO;
import io.github.ipl.tfc.docetressabores.dtos.ProductDTO;
import io.github.ipl.tfc.docetressabores.ejbs.FamilyBean;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/families")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class FamilyService
{
	@EJB
	private FamilyBean familyBean;

	public static FamilyDTO toDTO(Family family, boolean critical)
	{
		List<ProductDTO> productDTOs = ProductService.toDTOs(family.getProducts());
		return new FamilyDTO(
			family.getId(),
			family.getMaterial().getId(),
			family.getName(),
			critical ? null : productDTOs);
	}

	public static List<FamilyDTO> toDTOs(List<Family> families, boolean critical)
	{
		return families
			.stream()
			.map(f -> FamilyService.toDTO(f, critical))
			.collect(Collectors.toList());
	}

	@GET
	@Path("/")
	@Transactional
	public Response getAllFamiliesWS(@DefaultValue("true") @QueryParam("critical") boolean critical)
	{
		return Response.ok(toDTOs(familyBean.getAllFamilies(), critical)).build();
	}

	@GET
	@Path("/{id}")
	@Transactional
	public Response getFamilyWS(@PathParam("id") int id)
	{
		Family family = familyBean.findFamily(id);

		return (
			family== null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.ok(toDTO(family, false))
			).build();
	}
}
