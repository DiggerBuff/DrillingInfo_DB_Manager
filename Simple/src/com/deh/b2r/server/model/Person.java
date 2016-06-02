package com.deh.b2r.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {
	@JsonProperty("id")
	private long id;
	@JsonProperty("age")
	private int age;
	@JsonProperty("name")
	private String name;
	private Map<String, Address> addresses = new HashMap<>();
	private List<Link> links = new ArrayList<>();
		
	public Person() {
		
	}

	public Person(@JsonProperty("id") long id, @JsonProperty("age")int age, 
			@JsonProperty("name") String name) {
		super();
		this.id = id;
		this.age = age;
		this.name = name;
	}
	
	public List<Link> getLinks() {
		return links;
	}
	
	public void addLink(String url, String rel) {
		Link link = new Link();
		link.setLink(url);
		link.setRel(rel);
		links.add(link);
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}


	public int getAge() {
		return age;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@XmlTransient
	public Map<String, Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Map<String, Address> addresses) {
		this.addresses = addresses;
	}
}
