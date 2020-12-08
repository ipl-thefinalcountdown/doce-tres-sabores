package io.github.ipl.tfc.docetressabores.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path("/")
public class RootService
{
	@GET
	@Path("/")
	public String getRoot()
	{
		return "Pudim API v1";
	}
}
