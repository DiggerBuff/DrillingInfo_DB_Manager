package com.deh.b2r.server;

import java.io.StringWriter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

public class AddressBookClient {
    
	private static Client client;
	private static WebTarget target;
	
    public AddressBookClient(){
    	client = ClientBuilder.newClient();
    	target = client.target("http://138.67.186.221:9898/");
    	//System.out.println(target.toString());
    	
	    //Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity("{\"street\":\"aoeu\"}", MediaType.APPLICATION_JSON));
	    //Response response2 = target.request(MediaType.APPLICATION_JSON_TYPE).get();
    }
    
    public AddressBookClient(int port){
    	client = ClientBuilder.newClient();
    	target = client.target("http://138.67.186.222:" + port + "/");
    	
	    //Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity("{\"street\":\"aoeu\"}", MediaType.APPLICATION_JSON));
	    //Response response2 = target.request(MediaType.APPLICATION_JSON_TYPE).get();
    }
    
    public String get(String location) {
    	Response response = target.path(location).request(MediaType.APPLICATION_JSON_TYPE).get();
    	return response.readEntity(String.class);
    }
    
    public String post(String location, String input) {
    	Response response = target.path(location).request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(input, MediaType.APPLICATION_JSON));
    	return response.readEntity(String.class);
    }
}
