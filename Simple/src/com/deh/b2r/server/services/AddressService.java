package com.deh.b2r.server.services;

import java.util.ArrayList;
import java.util.Map;

import com.deh.b2r.server.database.DatabaseClass;
import com.deh.b2r.server.model.Address;

public class AddressService {

	private Map<String, Address> addresses = DatabaseClass.getAddresses();

	public ArrayList<Address> getAllAddresses() {
		return new ArrayList<Address>(addresses.values());
	}
	
	public Address getAddress(String name) {
		return addresses.get(name);
	}
	
	public Address addAddress(Address address) {
		address.setId(addresses.size() + 1);
		addresses.put(address.getStreetName(), address);
		return address;
	}
	
	public Address updateAddress(Address address) {
		if(address.getId() <= 0) {
			return null;
		}
		addresses.put(address.getStreetName(),  address);
		return address;
	}
	
	public Address deleteAddress(String streetName) {
		return addresses.remove(streetName);
	}
}
