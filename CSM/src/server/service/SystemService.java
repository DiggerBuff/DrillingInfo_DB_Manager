package server.service;

import javax.ws.rs.core.Response;

public class SystemService {
	public Response restartDIServer() {
		try {
			System.out.println(System.getProperty("user.dir"));
			Runtime.getRuntime().exec("../jre/bin/java -cp * com.drillinginfo.rest.DIServer");
			//Runtime.getRuntime().exec("export CLASSPATH=$CLASSPATH:*");
			Runtime.getRuntime().exec("../jre/bin/java com.drillinginfo.rest.DIServer");
		} catch (Exception e) {
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to start DI Server").build();
			e.printStackTrace();
		}

		return Response.status(Response.Status.OK).entity("Started DI Server").build();
	}
}
