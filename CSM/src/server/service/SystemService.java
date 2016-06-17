package server.service;

import java.io.File;

import javax.ws.rs.core.Response;

public class SystemService {
	public Response restartDIServer() {
		try {
			System.out.println(System.getProperty("user.dir"));
			
			String OS = System.getProperty("os.name");
			if (OS.startsWith("Windows")) {
				//Runtime.getRuntime().exec("cd ..\\production\\Transform\\plugins");
				Runtime.getRuntime().exec("java -cp ..\\production\\Transform\\plugins\\* com.drillinginfo.rest.DIServer");
			}
			else {
				Runtime.getRuntime().exec("../production/Transform/jre/bin/java -cp ../production/Transform/plugins/* com.drillinginfo.rest.DIServer");
			}
		} catch (Exception e) {
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to start DI Server").build();
			e.printStackTrace();
		}

		return Response.status(Response.Status.OK).entity("Started DI Server").build();
	}
}
