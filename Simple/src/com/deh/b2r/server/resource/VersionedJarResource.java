package com.deh.b2r.server.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

import com.deh.b2r.server.model.VersionedJar;
import com.deh.b2r.server.service.VersionedJarService;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VersionedJarResource {

	VersionedJarService versionedJarService = new VersionedJarService();

	@GET
	public List<VersionedJar> getVersionedJars() {
		System.out.println("Get was called");
		return versionedJarService.getAllVersionedJars();
	}

	@GET
	@Path("/{vjId}")
	public VersionedJar getVersionedJar(@PathParam("vjID") long id, @Context UriInfo uriInfo) {
		VersionedJar vj = versionedJarService.getVersionedJar(id);
		//vj.addLink(getUriForSelf(uriInfo, vj), "self");
		//vj.addLink(getUriForAddress(uriInfo, vj), "address");
		return vj;
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