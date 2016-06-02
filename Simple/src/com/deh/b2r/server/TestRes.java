package com.deh.b2r.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Utility methods. Currently holds updater methods but we will change this once 
 * we have a better understanding of the resource structure of the database.
 * 
 * TODO move updater to its own resource.
 */
@Path("/")
public class TestRes
{
	static DBConnector updater = new DBConnector();

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
	public void getUpdate(@PathParam("jar") String jar) throws Exception
	{
		updater.get(jar);
	}
}