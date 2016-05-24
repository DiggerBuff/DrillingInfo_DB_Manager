package com.deh.b2r.server;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.JSONP;

/**
 * Utility methods.
 *
 */

@Path("utility")
public class TestRes
{
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

  @GET
  @Path("streets")
  @Produces(MediaType.APPLICATION_JSON)
  public List<SharedRep.Address> getQuantities() throws Exception
  {
    List<SharedRep.Address> streets = new ArrayList<>();
    // TODO replace with call to storage mechanism
    streets.add(new SharedRep.Address("Street1"));
    return streets;
  }

  @POST
  @Path("addstreet")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public SharedRep.Address addStreet(SharedRep.Address street) throws Exception
  {
    System.out.println("Add Street: " + street);
    // TODO storage mechanism
    return street;
  }
  
  @POST
  @Path("test")
  @Consumes("text/plain")
  //@Produces(MediaType.APPLICATION_JSON)
  public Response test(String testing) throws Exception
  {
	  System.out.println("Test: " + testing);
	  
	  return Response.created(new URI("test/"+testing)).build();
  }
}
