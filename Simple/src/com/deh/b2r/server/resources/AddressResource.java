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

import com.deh.b2r.server.model.Address;
import com.deh.b2r.server.services.AddressService;
import com.deh.b2r.server.services.PeopleService;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AddressResource {
	
	private AddressService addressService;
	
	public AddressResource(PeopleService peopleService) {
		this.addressService = new AddressService(peopleService);
	}

	@GET
	public List<Address> getAddresses(@PathParam("personId") long id) {
		return addressService.getAllAddresses(id);
	}
	
	@GET
	@Path("/{addressName}")
	public Address getAddress(@PathParam("personId") long id, @PathParam("addressName") String name) {
		return addressService.getAddress(id, name);
	}
	
	@POST
	public Address postAddress(@PathParam("personId") long id, Address address) {
		return addressService.addAddress(id, address);
	}
	
	@PUT
	@Path("/{addressName}")
	public Address putAddress(@PathParam("personId") long id, Address address, @PathParam("addressName") String name) {
		address.setStreetName(name);
		return addressService.updateAddress(id, address);
	}

	@DELETE
	@Path("/{addressName}")
	public void deleteAddress(@PathParam("personId") long id, @PathParam("addressName") String name) {
		System.out.println("Delete was called");
		addressService.deleteAddress(id, name);
	}
}
