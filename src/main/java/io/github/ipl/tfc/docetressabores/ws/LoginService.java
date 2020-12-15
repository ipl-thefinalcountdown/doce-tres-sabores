package io.github.ipl.tfc.docetressabores.ws;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.github.ipl.tfc.docetressabores.ejbs.UserBean;
import io.github.ipl.tfc.docetressabores.entities.User;

@Path("/auth")
@Consumes({ MediaType.APPLICATION_JSON })
public class LoginService {
	private static final Logger LOG = Logger.getLogger(LoginService.class.getName());
	@EJB UserBean userBean;

	@POST
	@Path("/login")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public Response authenticateUserWS(@FormParam("username") String username, @FormParam("password") String password) {
		User user = userBean.authenticate(username, password);

		if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
		LOG.info("Authenticating: " + username + "...");

		return Response.ok().build();
	}
}
