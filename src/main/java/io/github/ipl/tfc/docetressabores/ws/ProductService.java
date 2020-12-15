package io.github.ipl.tfc.docetressabores.ws;

import io.github.ipl.tfc.docetressabores.entities.Product;
import io.github.ipl.tfc.docetressabores.dtos.ProductDTO;
import io.github.ipl.tfc.docetressabores.dtos.VariantDTO;
import io.github.ipl.tfc.docetressabores.ejbs.ProductBean;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/products")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class ProductService
{
	@EJB
	private ProductBean productBean;

	public static ProductDTO toDTO(Product product)
	{
		List<VariantDTO> variantDTOs = VariantService.toDTOs(product.getVariants());
		return new ProductDTO(product.getId(), product.getName(), variantDTOs, product.getFamily().getId());
	}

	public static List<ProductDTO> toDTOs(List<Product> products)
	{
		return products
			.stream()
			.map(ProductService::toDTO)
			.collect(Collectors.toList());
	}

	@GET
	@Path("/")
	@Transactional
	public Response getAllProductsWS(@DefaultValue("") @QueryParam("filter") String filter)
	{
		return Response.ok(toDTOs(productBean.getAllProducts(filter))).build();
	}

	@GET
	@Path("/{id}")
	@Transactional
	public Response getProductWS(@PathParam("id") int id)
	{
		Product product = productBean.findProduct(id);

		return (
			product == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.ok(toDTO(product))
			).build();
	}

	@POST
	@Path("/")
	@Transactional
	public Response postProductWS(ProductDTO productDTO) {
		Product product = productBean.create(productDTO.getFamilyId(), productDTO.getName());

		return (
			product == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.ok(toDTO(product))
			).build();
	}

	@PUT
	@Path("/{id}")
	@Transactional
	public Response updateProductWS(@PathParam("id") int id, ProductDTO productDTO) {
		productDTO.setId(id);
		Product product = productBean.update(productDTO);

		return (
			product == null
				? Response.status(Response.Status.BAD_REQUEST)
				: Response.ok(toDTO(product))
		).build();
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public Response deleteProductWS(@PathParam("id") int id) {
		return (
			productBean.delete(id)
				? Response.noContent()
				: Response.status(Response.Status.BAD_REQUEST)
		).build();
	}
}
