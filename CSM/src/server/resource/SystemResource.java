package server.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import server.service.SystemService;

/**
 * Utility methods. Currently holds updater methods but we will change this once 
 * we have a better understanding of the resource structure of the database.
 * 
 * TODO move updater to its own resource.
 */
@Path("system")
public class SystemResource
{
	static SystemService systemService = new SystemService();

	//This is the path for shutdown
	@GET
	@Path("{command}")
	public Response getDatatype(@PathParam("command") String system) throws Exception
	{
		if (system.equals("shutdown")) {
			System.exit(0);
		}
		return Response.status(Response.Status.OK).entity("Unknown Command").build();
	}
	
	@GET
	@Path("restart")
	public Response restart() throws Exception
	{
		return systemService.restartDIServer();
	}
}