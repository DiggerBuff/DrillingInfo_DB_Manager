package server.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.server.Uri;

import server.model.VersionedJar;
import server.service.VersionedJarService;


@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VersionedJarResource {

	VersionedJarService versionedJarService = new VersionedJarService();

	@GET
	@Path("/{vjName}")
	public Response detect(@PathParam("vjName") String vjName, @Context UriInfo uriInfo) {
		String response = versionedJarService.detect(vjName);
		if (response == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		else {
			return Response.status(Response.Status.OK).entity(response).build();
		}
	}
	
	@GET
	public Response detectAll(@Context UriInfo uriInfo) {
		String response = versionedJarService.detectAll();
		if (response == null) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		else {
			return Response.status(Response.Status.OK).entity(response).build();
		}
	}
	
	/*
	private String getUriForSelf(UriInfo uriInfo, Person person) {
		String uri = uriInfo.getBaseUriBuilder()
							.path(PeopleResource.class)
							.path(Long.toString(person.getId()))
							.build()
							.toString();
		return uri;
	}
	
	private String getUriForAddress(UriInfo uriInfo, Person person) {
		String uri = uriInfo.getBaseUriBuilder()
							.path(PeopleResource.class)
							.path(PeopleResource.class, "getAddressResource")
							.path(AddressResource.class)
							.resolveTemplate("personId", person.getId())
							.build()
							.toString();
		return uri;
	}
	
	@POST
	public Response postPerson(Person person, @Context UriInfo uriInfo) {
		Person newPerson = peopleService.addPerson(person);
		String newId = String.valueOf(newPerson.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
				.entity(newPerson)
				.build();
		//return peopleService.addPerson(person);
	}
	
	@PUT
	@Path("/{personId}")
	public Person putPerson(Person person, @PathParam("personId") long id) {
		person.setId(id);
		return peopleService.updatePerson(person);
	}

	@DELETE
	@Path("/{personId}")
	public void deletePerson(@PathParam("personId") long id) {
		System.out.println("Delete was called");
		peopleService.deletePerson(id);
	}
	*/
	
	/*
	@Path("/{personId}/addresses")
	public AddressResource getAddressResource() {
		return new AddressResource(peopleService);
	}
	*/
}