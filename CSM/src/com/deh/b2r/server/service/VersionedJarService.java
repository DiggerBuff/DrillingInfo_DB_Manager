package com.deh.b2r.server.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.deh.b2r.server.DBConnector;
import com.deh.b2r.server.model.VersionedJar;

public class VersionedJarService {
	
	//TODO populate verionedJar using database
	private DBConnector dbConnector = new DBConnector(); // Need to interface this with Alan's S3 stuff
	/*
	public Map<Long, VersionedJar> getVersionedJars() {
		
		return versionedJars;
	}

	public void setVersionedJars(Map<Long, VersionedJar> versionedJars) {
		this.versionedJars = versionedJars;
	}

	public List<VersionedJar> getAllVersionedJars() {
		return new ArrayList<VersionedJar>(versionedJars.values());
	}
	*/
	public VersionedJar getVersionedJar(String vjName) {
		try {
			return dbConnector.getVersionedJar(vjName);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
