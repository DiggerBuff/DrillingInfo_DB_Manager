package com.deh.b2r.server.resources;

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

import com.deh.b2r.server.model.Person;
import com.deh.b2r.server.services.PeopleService;

@Path("/people")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PeopleResource {

	PeopleService peopleService = new PeopleService();

	@GET
	public List<Person> getPeople() {
		System.out.println("Get was called");
		return peopleService.getAllPeople();
	}

	@GET
	@Path("/{personId}")
	public Person getPerson(@PathParam("personId") long id, @Context UriInfo uriInfo) {
		Person person = peopleService.getPerson(id);
		person.addLink(getUriForSelf(uriInfo, person), "self");
		person.addLink(getUriForAddress(uriInfo, person), "address");
		return person;
	}

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
	
	@Path("/{personId}/addresses")
	public AddressResource getAddressResource() {
		return new AddressResource(peopleService);
	}
}
