package com.deh.b2r.server.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VersionedJar {
	@JsonProperty("id")
	private long id;
	private List<Link> links = new ArrayList<>();
		
	public VersionedJar() {
		
	}

	public VersionedJar(@JsonProperty("id") long id) {
		super();
		this.id = id;
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
}