package server.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import server.DBConnector;
import server.model.VersionedJar;


public class VersionedJarService {
	
	private DBConnector dbConnector = new DBConnector();

	public VersionedJar getVersionedJar(String vjName) {
		try {
			return dbConnector.getVersionedJar(vjName);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public List<String> getAllJars() {
		return dbConnector.getAllJars();
	}
	
	/*
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
	*/
}
