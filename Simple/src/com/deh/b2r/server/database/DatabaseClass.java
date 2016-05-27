package com.deh.b2r.server.database;

import java.util.HashMap;
import java.util.Map;

import com.deh.b2r.server.model.Address;
import com.deh.b2r.server.model.Person;

public class DatabaseClass {
	private static Map<Long, Person> people = new HashMap<>();
	private static Map<String, Address> addresses = new HashMap<>();
	
	public static Map<Long, Person> getPeople() {
		return people;
	}
	
	public static Map<String, Address> getAddresses() {
		return addresses;
	}
}
