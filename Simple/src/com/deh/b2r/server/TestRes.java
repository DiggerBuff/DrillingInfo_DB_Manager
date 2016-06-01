package com.deh.b2r.server;

import javax.ws.rs.GET;

//import java.io.FileNotFoundException;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.net.URI;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;

//import java.net.URI;
//import java.util.ArrayList;

//import java.util.List;

//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//import javax.ws.rs.core.Response;


/**
 * Utility methods.
 *
 */

@Path("/")
public class TestRes
{
	static Updater updater = new Updater();
	
	//This is the path for shutdown
  @GET
  @Path("{system}")
  @Produces(MediaType.TEXT_PLAIN)
  public String getDatatype(@PathParam("system") String system) throws Exception
  {
	  	if (system.equals("shutdown")) {
    		System.exit(0);
    	}
    	return "Unknown System Command";
  }
  
  //This is the path that gets jars
  @GET
  @Path("update/{jar}")
  //@Produces(MediaType.APPLICATION_JSON)
  public String getUpdate(@PathParam("jar") String jar) throws Exception
  {
    updater.get(jar);
    return "";
  }
}





