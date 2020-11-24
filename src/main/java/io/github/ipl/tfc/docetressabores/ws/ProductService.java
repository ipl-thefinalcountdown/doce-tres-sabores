package io.github.ipl.tfc.docetressabores.ws;

import io.github.ipl.tfc.docetressabores.entities.Product;
import io.github.ipl.tfc.docetressabores.dtos.ProductDTO;
import io.github.ipl.tfc.docetressabores.dtos.VariantDTO;
import io.github.ipl.tfc.docetressabores.ejbs.ProductBean;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
	public Response getAllProductsWS()
	{
		return Response.ok(toDTOs(productBean.getAllProducts())).build();
	}
}
