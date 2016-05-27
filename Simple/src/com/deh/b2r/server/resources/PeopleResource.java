package com.deh.b2r.server.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.deh.b2r.server.model.Person;
import com.deh.b2r.server.services.PeopleService;

@Path("/people")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PeopleResource {

	PeopleService peopleService = new PeopleService();

	@GET
	public List<Person> getPeople() {
		return peopleService.getAllPeople();
	}
	
	@GET
	@Path("/{personId}")
	public Person getPerson(@PathParam("personId") long id) {
		return peopleService.getPerson(id);
	}
	
	@POST
	public Person postPerson(Person person) {
		return peopleService.addPerson(person);
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
		peopleService.deletePerson(id);
	}
}
