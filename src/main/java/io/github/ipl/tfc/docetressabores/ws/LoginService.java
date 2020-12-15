package io.github.ipl.tfc.docetressabores.ws;

import java.text.ParseException;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;

import io.github.ipl.tfc.docetressabores.Jwt;
import io.github.ipl.tfc.docetressabores.ejbs.JwtBean;
import io.github.ipl.tfc.docetressabores.ejbs.UserBean;
import io.github.ipl.tfc.docetressabores.entities.User;

@Path("/auth")
@Consumes({ MediaType.APPLICATION_JSON })
public class LoginService {
	private static final Logger LOG = Logger.getLogger(LoginService.class.getName());
	@EJB UserBean userBean;
	@EJB JwtBean jwtBean;

	@POST
	@Path("/login")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public Response authenticateUserWS(@FormParam("username") String username, @FormParam("password") String password) {
		User user = userBean.authenticate(username, password);

		if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
		LOG.info("Authenticating: " + username + "...");

		String token = null;
		try {
			token = jwtBean.createJwt(username, new String[]{ user.getClass().getSimpleName() });
		} catch (JOSEException e) {
			LOG.warning(e.getMessage());
		}

		return (token == null
			? Response.status(Response.Status.UNAUTHORIZED)
			: Response.ok(new Jwt(token)))
		.build();
	}

	@GET
	@Path("/user")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response demonstrateClaims(@HeaderParam("Authorization") String auth) {
		if (auth != null && auth.startsWith("Bearer ")) {
			try {
				JWT j = JWTParser.parse(auth.substring(7));
				return Response.ok(j.getJWTClaimsSet().getClaims()).build();
				//Note: nimbusds converts token expiration time to milliseconds
			} catch (ParseException e) {
				LOG.warning(e.toString());
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}
		return Response.noContent().build(); //no jwt means no claims to extract
	}
}
