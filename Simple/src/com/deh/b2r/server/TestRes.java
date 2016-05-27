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

	@GET
	@Path("utility/{system}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getDatatype(@PathParam("system") String system) throws Exception
	{
		if (system.equals("shutdown")) {
			System.exit(0);
		}
		return "Unknown System Command";
	}

}
