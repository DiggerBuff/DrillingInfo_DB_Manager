package com.deh.b2r.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.deh.b2r.server.database.DatabaseClass;
import com.deh.b2r.server.model.Person;

public class PeopleService {
	
	private Map<Long, Person> people = DatabaseClass.getPeople();
	
	public PeopleService(){
		people.put(1L, new Person(1, 23, "Harry"));
		people.put(2L, new Person(2, 21, "Alan"));
		people.put(3L, new Person(3, 25, "Adam"));
	}
	
	public List<Person> getAllPeople() {
		return new ArrayList<Person>(people.values());
	}
	
	public Person getPerson(long id) {
		return people.get(id);
	}
	
	public Person addPerson(Person newPerson) {
		newPerson.setId(people.size() + 1);
		people.put(newPerson.getId(), newPerson);
		return newPerson;
	}
	
	public Person updatePerson(Person person) {
		if(person.getId() <= 0) {
			return null;
		}
		people.put(person.getId(),  person);
		return person;
	}
	
	public Person deletePerson(long id) {
		return people.remove(id);
	}
}
