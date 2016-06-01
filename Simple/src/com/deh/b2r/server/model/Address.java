package com.deh.b2r.server.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class Address {
	@JsonProperty("number")
	private int number;
	@JsonProperty("streetName")
	private String streetName;
	@JsonProperty("town")
	private String town;
	@JsonProperty("state")
	private String state;
	
	public Address() {
		
	}
	
	public Address(@JsonProperty("number") int number, @JsonProperty("streetName") String streetName, @JsonProperty("town") String town, @JsonProperty("state") String state) {
		super();
		this.number = number;
		this.streetName = streetName;
		this.town = town;
		this.state = state;
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
}
