package com.drillinginfo.repair.rest.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.drillinginfo.repair.DataRepair;
import com.drillinginfo.repair.DataRepairs;

/**
 * Server level resources.
 */
public class Repair
{
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<String> getRepairNames()
  {
    return DataRepairs.getAllRepairNames();
  }
  
  @GET
  @Path("{repairName}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getSummary(@PathParam("repairName") String repairName)
  {
    DataRepair repair = DataRepairs.getRepairForName(repairName);
    if (repair != null)
    {
      return Response.ok(repair.getSummary()).build();
    }
    return Response.status(Response.Status.NOT_FOUND).build();
  }
  
  @POST
  @Path("{repairName}/detect")
  public Response postDetect(@PathParam("repairName") String repairName)
  {
    DataRepair repair = DataRepairs.getRepairForName(repairName);
    if (repair != null)
    {
      return Response.ok(repair.detect()).build();
    }
    return Response.status(Response.Status.NOT_FOUND).build();
  }
  
  @POST
  @Path("{repairName}/repair")
  public Response postRepair(@PathParam("repairName") String repairName)
  {
    DataRepair repair = DataRepairs.getRepairForName(repairName);
    if (repair != null)
    {
      if (!repair.detect())
      {
        return Response.status(Response.Status.NO_CONTENT).build();
      }
      return Response.ok(repair.repair()).build();
    }
    return Response.status(Response.Status.NOT_FOUND).build();
  }
}
