package com.deh.b2r.server.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Address {

	private long id;
	private int number;
	private String streetName;
	private String town;
	private String state;
	private int zip;
	
	public Address() {
		
	}
	
	public Address(int number, String streetName, String town, String state,
			int zip) {
		super();
		this.number = number;
		this.streetName = streetName;
		this.town = town;
		this.state = state;
		this.zip = zip;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
