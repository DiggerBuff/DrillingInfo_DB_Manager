package server.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VersionedJar {
	@JsonProperty("id")
	private String version;
	private List<Link> links = new ArrayList<>();
		
	public VersionedJar() {
		
	}

	public VersionedJar(@JsonProperty("id") String version, File file) {
		super();
		this.version = version;
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