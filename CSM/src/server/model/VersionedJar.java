package server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VersionedJar {
	@JsonProperty("name")
	private String name;
	
	public VersionedJar() {
		this.name = "Test";
	}
	
	public VersionedJar(@JsonProperty("name") String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}