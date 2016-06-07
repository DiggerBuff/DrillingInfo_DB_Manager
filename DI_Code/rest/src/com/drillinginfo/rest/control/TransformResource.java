package com.drillinginfo.rest.control;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.drillinginfo.repair.rest.api.Repair;
import com.drillinginfo.rest.api.Server;

/**
 * Core resource mapper. This class should establish paths to other known
 * resources.
 */
public class TransformResource
{
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response get() throws Exception
  {
    // simple method included to ensure OPTIONS are generated for all paths
    return Response.ok().build();
  }

  // TODO look at annotation processing to auto-generate this file with path.
  
  @Path("server")
  public Server server() {
    return new Server();
  }
  
  @Path("repair")
  public Repair repair() {
    return new Repair();
  }
}
