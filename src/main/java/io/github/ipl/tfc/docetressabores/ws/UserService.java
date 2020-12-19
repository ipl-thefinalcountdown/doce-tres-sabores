package io.github.ipl.tfc.docetressabores.ws;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.github.ipl.tfc.docetressabores.dtos.UserDTO;
import io.github.ipl.tfc.docetressabores.ejbs.ClientBean;
import io.github.ipl.tfc.docetressabores.ejbs.DesignerBean;
import io.github.ipl.tfc.docetressabores.ejbs.EmailBean;
import io.github.ipl.tfc.docetressabores.ejbs.UserBean;
import io.github.ipl.tfc.docetressabores.entities.User;

@Path("/users")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class UserService {
	@EJB UserBean userBean;
	@EJB ClientBean clientBean;
	@EJB DesignerBean designerBean;
	@EJB EmailBean emailBean;

	public static UserDTO toDTO(User client) {
		return new UserDTO(
			client.getName(),
			null,
			null,
			client.getUsername(),
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
	@Path("/{username}")
	@Transactional
	public Response getUserWS(@PathParam("username") String username)
		throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		User user = userBean.findUser(username);
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
					.getMethod("toDTO", user.getClass(), boolean.class);

				return Response.ok(serviceMethod.invoke(null, user.getClass().cast(user), false)).build();
			} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}
	}

	// FIXME: a user can only delete himself
	// TODO: add an authenticate method on user
	// TODO: add a password param on @DELETE
	@DELETE
	@Path("/{username}")
	@Transactional
	public Response deleteUserWS(@PathParam("username") String username) {
		return (
			userBean.delete(username)
				? Response.noContent()
				: Response.status(Response.Status.BAD_REQUEST)
		).build();
	}

	@POST
	@Path("/")
	@Transactional
	public Response postUserWS(UserDTO userDTO) {
		try {
			Field field = UserService.class.getDeclaredField(userDTO.getUserType().toLowerCase() + "Bean");
			field.setAccessible(true);

			Object fieldValue = field.get(this);
			Method method = fieldValue.getClass().getMethod("create", UserDTO.class);

			User user = (User) method.invoke(fieldValue, userDTO);

			if (user == null) return Response.status(Response.Status.BAD_REQUEST).build();

			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Hi %s,", user.getName()));
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append("Thank you for choosing us! We inform you ");
			sb.append("your account has been created successfuly!");
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append("Thanks,");
			sb.append("Doce Tres Sabores Team");

			emailBean.send(
				user.getEmail(),
				"Doce Tres Sabores Team",
				sb.toString());

			return Response.ok(toDTO(user)).build();
		} catch (NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Logger.getLogger(UserService.class.getName()).log(Level.WARNING, e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
