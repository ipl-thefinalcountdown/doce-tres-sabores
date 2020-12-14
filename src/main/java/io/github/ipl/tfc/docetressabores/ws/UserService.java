package io.github.ipl.tfc.docetressabores.ws;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.github.ipl.tfc.docetressabores.dtos.UserDTO;
import io.github.ipl.tfc.docetressabores.ejbs.ClientBean;
import io.github.ipl.tfc.docetressabores.ejbs.DesignerBean;
import io.github.ipl.tfc.docetressabores.ejbs.UserBean;
import io.github.ipl.tfc.docetressabores.entities.User;

@Path("/users")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class UserService {
	@EJB UserBean userBean;
	@EJB ClientBean clientBean;
	@EJB DesignerBean designerBean;

	public static UserDTO toDTO(User client) {
		return new UserDTO(
			client.getId(),
			client.getName(),
			null,
			null,
			null,
			null,
			null,
			null,
			client.getClass().getSimpleName()
		);
	}

	public static List<UserDTO> toDTOs(List<User> client) {
		return client
			.stream()
			.map(UserService::toDTO)
			.collect(Collectors.toList());
	}

	@GET
	@Path("/")
	@Transactional
	public Response getAllUsersWS() {
		return Response.ok(toDTOs(userBean.getAllUsers())).build();
	}

	@GET
	@Path("/{id}")
	@Transactional
	public Response getUserWS(@PathParam("id") int id)
		throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		User user = userBean.findUser(id);
		if (user == null) return Response.status(Response.Status.BAD_REQUEST).build();
		else {
			// using Java's reflection here
			// with reflection obtaining the correct class instance is very easy
			// using that knowledge with other classes it's possible to automate
			// some code
			// because we have a cosntant naming convention we know for each
			// entity we have a service:
			//     User -> UserService
			//     Designer -> DesignerService
			// with this and using reflection to obtain the real object from user
			// as well as it's service class to get to the method which always
			// follows a name convention the same way classes do, it's possible
			// to redirect any user to it's respective subclass methods
			try {
				String classname = user.getClass().getSimpleName();
				Method serviceMethod = Class
					.forName("io.github.ipl.tfc.docetressabores.ws." + classname + "Service")
					.getMethod("toDTO", user.getClass());

				return Response.ok(serviceMethod.invoke(null, user.getClass().cast(user))).build();
			} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}
	}

	// FIXME: a user can only delete himself
	// TODO: add an authenticate method on user
	// TODO: add a password param on @DELETE
	@DELETE
	@Path("/{id}")
	@Transactional
	public Response deleteUserWS(@PathParam("id") int id) {
		return (
			userBean.delete(id)
				? Response.noContent()
				: Response.status(Response.Status.BAD_REQUEST)
		).build();
	}
}
