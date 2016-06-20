package server.resource;

import java.util.ArrayList;

import server.model.VersionedJar;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import server.service.VersionedJarService;


@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VersionedJarResource {

	private static VersionedJarService versionedJarService = new VersionedJarService();

	@GET
	public Response detectAll(@Context UriInfo uriInfo) {
		ArrayList<VersionedJar> response = versionedJarService.detectAll();
		
		if (response == null) {
			return Response.status(Response.Status.NO_CONTENT).build();
		}
		else {
			return Response.status(Response.Status.OK).entity(response).build();
		}
	}
	
	@GET
	@Path("replace/")
	public Response replaceAll(@Context UriInfo uriInfo) {

		boolean response = versionedJarService.replaceAll();
		
		if (!response) {
			return Response.status(Response.Status.NO_CONTENT).build();
		}
		else {
			return Response.status(Response.Status.OK).entity(response).build();
		}	
	}
}