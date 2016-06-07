package com.drillinginfo.rest.api;

import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.drillinginfo.rest.control.RESTServer;

/**
 * Server level resources.
 */
public class Server
{
  @POST
  @Produces(MediaType.TEXT_PLAIN)
  public Response post(@QueryParam("shutdown") final boolean stopService, 
      @QueryParam("echo") final String message) 
      throws Exception
  {
    if (stopService)
    {
      Thread shutdown = new Thread(new DelayedShutDown());
      shutdown.start();
      return Response.ok("Server will shutdown in 5 seconds...").build();
    }
    if (message != null)
    {
      return Response.ok("[echo] " + message).build();
    }
    return Response.status(Status.NOT_IMPLEMENTED).build();
  }

  private class DelayedShutDown implements Runnable
  {
    public void run()
    {
      try {Thread.sleep(5000);} catch (InterruptedException ignore) { }
      RESTServer.shutdown();
    }
  }
}
