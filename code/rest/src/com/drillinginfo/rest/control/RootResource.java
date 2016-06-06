package com.drillinginfo.rest.control;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Core root level resources. This class connects the Transform resource and
 * should generally not be modified. Please add new Transform resources to
 * the TransformResource class.
 */
@Path("/")
public class RootResource
{
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response get() throws Exception
  {
    // simple method included to ensure OPTIONS are generated for all paths
    return Response.ok().build();
  }

  @Path("transform")
  public TransformResource system() {
    return new TransformResource();
  }
}
