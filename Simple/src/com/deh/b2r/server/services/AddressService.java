package com.deh.b2r.server.services;

import java.util.ArrayList;
import java.util.Map;

import com.deh.b2r.server.database.DatabaseClass;
import com.deh.b2r.server.model.Address;
import com.deh.b2r.server.model.Person;

public class AddressService {

	private Map<Long, Person> people;

	public AddressService(PeopleService peopleService) {
		this.people = peopleService.getPeople();
	}

	public ArrayList<Address> getAllAddresses(long personId) {
		Map<String, Address> addresses = people.get(personId).getAddresses();
		return new ArrayList<Address>(addresses.values());
	}
	
	public Address getAddress(long personId, String name) {
		Map<String, Address> addresses = people.get(personId).getAddresses();
		return addresses.get(name);
	}
	
	public Address addAddress(long personId, Address address) {
		Map<String, Address> addresses = people.get(personId).getAddresses();
		addresses.put(address.getStreetName(), address);
		return address;
	}
	
	public Address updateAddress(long personId, Address address) {
		Map<String, Address> addresses = people.get(personId).getAddresses();
		if(address.getStreetName().equals(null)) {
			return null;
		}
		addresses.put(address.getStreetName(),  address);
		return address;
	}
	
	public Address deleteAddress(long personId, String streetName) {
		Map<String, Address> addresses = people.get(personId).getAddresses();
		return addresses.remove(streetName);
	}
}
