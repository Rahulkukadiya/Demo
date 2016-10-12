package com.sellnews.demo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpHeader;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.jboss.logging.annotations.Pos;

import com.google.common.base.Optional;

import ch.qos.logback.core.net.server.Client;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.validation.Validated;

@Path("/employees")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ApplicationResource {
	private EmployeDao employeDao;
	private final static Logger LOGGER = Logger.getLogger(EmployeDao.class.getName());

	public ApplicationResource(EmployeDao employeDao) {
		this.employeDao = employeDao;
	}

	@GET
	@Path("admin/allUser")
	@UnitOfWork
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataJ() {

		try {
			List<Employe> employe_data = employeDao.findAll();
			return Response.ok().entity(employe_data).build();
		} catch (Exception exception) {
			LOGGER.error(exception);
			return Response.serverError().entity(ResponseMap.generatMap(exception.getMessage())).build();
		}
	}
	@POST
	@Path("/admin")
	@UnitOfWork
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getUserByDate(@QueryParam("startdate") Timestamp startdate,
			@QueryParam("lastdate") Timestamp lastdate) {
		try {
			LastModifiedData lastModifiedData = new LastModifiedData();
			lastModifiedData.setLastdate(lastdate);
			lastModifiedData.setStartdate(startdate);
			List<Employe> user_data = employeDao.getUser(lastModifiedData);
			return Response.ok().entity(user_data).build();
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			LOGGER.error(exception);
			return Response.serverError().entity(ResponseMap.generatMap(exception.getMessage())).build();
		}
	}

	@GET
	@Path("/user/{id}")
	@UnitOfWork
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("id") long id) {
		try {
			List list= employeDao.getDataById(id);
			String message=(String)list.get(0);
			LOGGER.info(message);
			if ((list.size()>1)&&(message.equals(ConstantVariabls.DATA_FOUND))) {
				EmployePojo employePojo=(EmployePojo)list.get(1);
				return Response.ok(employePojo).build();
			} else if(message.equals(ConstantVariabls.USER_NOT_FOUND)) {
				return Response.status(404).entity(ResponseMap.generatMap(ConstantVariabls.USER_NOT_FOUND)).build();
			}
			else
			{
				LOGGER.info("else");
				return Response.status(Response.Status.UNAUTHORIZED).entity(ResponseMap.generatMap(ConstantVariabls.LOGINERROR)).build();
			}
		} catch (Exception exception) {
			LOGGER.error("exception");
			return Response.serverError().entity(ResponseMap.generatMap(exception.getMessage())).build();
		}
	}

	@POST
	@Path("/user/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public Response insert_userJ(@Validated @Auth EmployePojo employe) {
		try {
			String uuid = RandomStringUUID.generateUUID().toString();
			String result = employeDao.insert_empdetails(employe,uuid);			
				return Response.status(201).entity(ResponseMap.generatMap(result)).build();
		} catch (Exception exception) {
			LOGGER.error("here");
			return Response.serverError().entity(ResponseMap.generatMap(exception.getMessage())).build();
		}

	}

	@DELETE
	@Path("/user/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public Response removeUser(@PathParam("id") long id) {
		try {
			String result = employeDao.removeUser(id);
			if(result.equals(ConstantVariabls.UNAUTH))
			{
				return Response.status(Response.Status.UNAUTHORIZED).entity(ConstantVariabls.LOGINERROR).build();
			}
			else
			return Response.accepted().entity(ResponseMap.generatMap(result)).build();
		} catch (Exception exception) {
			LOGGER.error(exception);
			return Response.serverError().entity(ResponseMap.generatMap(exception.getMessage())).build();
		}
	}

	@PUT
	@Path("/user/{id}")
	@UnitOfWork
	@Consumes(MediaType.APPLICATION_JSON)
	public Response edit_Data(@PathParam("id") long id, @Validated EmployePojo new_employe_Pojo) {
		try {
			String result = employeDao.update_User(id, new_employe_Pojo);
			return Response.accepted().entity(ResponseMap.generatMap(result)).build();
		} catch (Exception exception) {
			LOGGER.error(exception);
			return Response.serverError().entity(ResponseMap.generatMap(exception.getMessage())).build();
		}
	}

	@POST
	@Path("/login/{id}/{email}")
	@UnitOfWork
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loginInTheAPI(@PathParam("id") long id, @PathParam("email") String email) {
		try {
			String uuid = RandomStringUUID.generateUUID().toString();
			String result = employeDao.insertUUID(uuid, id, email);
			if (result.equals(ConstantVariabls.VALID_STATUS)) {
				return Response.ok(uuid).build	();
			} else if (!(result.equals(ConstantVariabls.DATA_MISMATCH))) {
				return Response.status(404).entity(ResponseMap.generatMap(ConstantVariabls.DATA_MISMATCH)).build();
			} else {
				return Response.status(Response.Status.UNAUTHORIZED).entity(ResponseMap.generatMap("you must be login first")).build();
			}
			// return Response.accepted().build();
		} catch (Exception exception) {
			LOGGER.error(exception);
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
	@POST
	@Path("/logout/{id}/{email}")
	@UnitOfWork
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logOutInTheAPI(@PathParam("id") long id, @PathParam("email") String email) {
		try {
			LOGGER.error(email);
			String result = employeDao.logOut(id, email);
			if (result.equals(ConstantVariabls.VALID_STATUS)) {
				return Response.ok(ResponseMap.generateCodeMessage("your are logout")).build();
			} else if (!(result.equals(ConstantVariabls.DATA_MISMATCH))) {
				return Response.status(Response.Status.UNAUTHORIZED).entity(ConstantVariabls.LOGINERROR).build();
			} else {
				return Response.status(404).entity(ConstantVariabls.DATA_MISMATCH).build();
			}
		} catch (Exception exception) {
			LOGGER.error(exception);
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

}
