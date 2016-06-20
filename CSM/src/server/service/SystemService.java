package server.service;

import javax.ws.rs.core.Response;

public class SystemService {
	public Response restartDIServer() {
		try {
			System.out.println(System.getProperty("user.dir"));
			
			/**
			 * We used this if else because the school computers (Linux) didn't have the correct jre but
			 * my laptop (Windows) does.
			 * 
			 * Replace the if else with this :
			 * Runtime.getRuntime().exec("java -cp * com.drillinginfo.rest.DIServer");
			 */
			String OS = System.getProperty("os.name");
			if (OS.startsWith("Windows")) {
				Runtime.getRuntime().exec("java -cp * com.drillinginfo.rest.DIServer");
			}
			else {
				Runtime.getRuntime().exec("../jre/bin/java -cp * com.drillinginfo.rest.DIServer");
			}
		} catch (Exception e) {
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to start DI Server").build();
			e.printStackTrace();
		}

		return Response.status(Response.Status.OK).entity("Started DI Server").build();
	}
}
