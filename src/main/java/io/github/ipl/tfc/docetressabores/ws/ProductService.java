package io.github.ipl.tfc.docetressabores.ws;

import io.github.ipl.tfc.docetressabores.entities.Product;
import io.github.ipl.tfc.docetressabores.entities.Variant;
import io.github.ipl.tfc.docetressabores.dtos.ProductDTO;
import io.github.ipl.tfc.docetressabores.dtos.VariantDTO;
import io.github.ipl.tfc.docetressabores.ejbs.ProductBean;
import io.github.ipl.tfc.docetressabores.ejbs.VariantBean;

import javax.ejb.EJB;
import javax.persistence.Enumerated;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/products")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class ProductService
{
	@EJB private ProductBean productBean;
	@EJB private VariantBean variantBean;

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

	@POST
	@Path("/{id}/import")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Transactional
	public Response importProductsWS(@PathParam("id") int id, MultipartFormDataInput input) {
		if (productBean.findProduct(id) == null) return Response.status(Response.Status.BAD_REQUEST).build();

		Map<String, List<InputPart>> importFile = input.getFormDataMap();
		var variants = toDictionary(importFile.get("fileVariant"));
		var resistances = toDictionary(importFile.get("fileResistance"));

		Enumeration<String> vKeys = variants.keys();
		while (vKeys.hasMoreElements()) {
			String variantName = vKeys.nextElement();
			Dictionary<String, Double> variantData = variants.get(variantName).get(0);

			Variant v;

			if ((v = variantBean.getVariantBy(variantName)) == null) {
				v = variantBean.create(
					id,
					variantName,
					variantData.get("weff_p"),
					variantData.get("weff_n"),
					variantData.get("ar"),
					variantData.get("sigmac")
					);

				if (resistances.get(variantName) != null)
					for (Dictionary<String, Double> vResistanceData : resistances.get(variantName)) {
						v.addMcr_p(vResistanceData.get("l_p"), vResistanceData.get("mcr_p"));
						v.addMcr_n(vResistanceData.get("l_n"), vResistanceData.get("mcr_n"));
					}
			} else {
				VariantDTO variantDTO = new VariantDTO();
				variantDTO.setId(v.getId());
				variantDTO.setProductId(id);
				variantDTO.setName(variantName);
				variantDTO.setWeff_p(variantData.get("weff_p"));
				variantDTO.setWeff_n(variantData.get("weff_n"));
				variantDTO.setAr(variantData.get("ar"));
				variantDTO.setSigmaC(variantData.get("sigmac"));
				variantDTO.setPp(v.getPp());

				if (resistances.get(variantName) != null)
					for (Dictionary<String, Double> vResistanceData : resistances.get(variantName)) {
						variantDTO.addMcr_p(vResistanceData.get("l_p"), vResistanceData.get("mcr_p"));
						variantDTO.addMcr_n(vResistanceData.get("l_n"), vResistanceData.get("mcr_n"));
					}

				variantBean.update(variantDTO);
			}
		}

		return Response.noContent().build();
	}

	private Dictionary<String, List<Dictionary<String, Double>>> toDictionary(List<InputPart> iParts) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		try {
			for (InputPart ip : iParts) {
				InputStream is = ip.getBody(InputStream.class, null);
				byte[] buff = IOUtils.toByteArray(is);
				byteArrayOutputStream.writeBytes(buff);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		Dictionary<String, List<Dictionary<String, Double>>> dict = new Hashtable<>();
		try {
			File file = File.createTempFile("deleteme", ".tmp");
			file.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(file);
			byteArrayOutputStream.writeTo(fos);
			CSVReader reader = new CSVReader(new FileReader(file, StandardCharsets.UTF_8));

			String[] headers = reader.readNext();
			String[] line;
			while((line = reader.readNext()) != null) {
				int i = 1;
				String product = line[0];
				Dictionary<String, Double> records = new Hashtable<>();

				for (String str : Arrays.copyOfRange(line, 1, line.length)) {
					records.put(headers[i].toLowerCase(), Double.valueOf(str));
					i++;
				}

				if (dict.get(product) == null)
					dict.put(product, new ArrayList<>());

				dict.get(product).add(records);
			}
		} catch(IOException | CsvValidationException e) {
			System.err.println(e.getMessage());
		}

		return dict;
	}
}
