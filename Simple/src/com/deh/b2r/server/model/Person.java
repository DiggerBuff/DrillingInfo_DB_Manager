package com.deh.b2r.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {
	@JsonProperty("id")
	private long id;
	@JsonProperty("age")
	private int age;
	@JsonProperty("name")
	private String name;
		
	public Person() {
		
	}

	public Person(@JsonProperty("id") long id, @JsonProperty("age")int age, 
			@JsonProperty("name") String name) {
		super();
		this.id = id;
		this.age = age;
		this.name = name;
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
}
