package com.deh.b2r.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


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
