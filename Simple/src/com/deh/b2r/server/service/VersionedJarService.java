package com.deh.b2r.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.deh.b2r.server.model.VersionedJar;

public class VersionedJarService {
	
	//TODO populate verionedJar using database
	private Map<Long, VersionedJar> versionedJars = null; // Need to interface this with Alan's S3 stuff
	
	public Map<Long, VersionedJar> getVersionedJars() {
		return versionedJars;
	}

	public void setVersionedJars(Map<Long, VersionedJar> versionedJars) {
		this.versionedJars = versionedJars;
	}

	public List<VersionedJar> getAllVersionedJars() {
		return new ArrayList<VersionedJar>(versionedJars.values());
	}
	
	public VersionedJar getVersionedJar(String vjName) {
		return versionedJars.get(vjName);
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
