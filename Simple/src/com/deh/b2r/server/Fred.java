package com.deh.b2r.server;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import com.deh.b2r.server.resources.PeopleResource;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import java.util.Scanner;

public class Fred
{
	public static void main(String... args) {
		Fred fred = new Fred();
		Scanner kb = new Scanner(System.in);

		HttpServer server = fred.startServer();
		server.getListener("grizzly").createManagementObject();

		while(!server.isStarted()){}
		System.out.println("Type \"utility/shutdown\" to quit server.");

		//It takes time to shut down. This ensures it shuts down before continuing.
		while(!server.shutdown().isDone()){}

		System.out.println("Type \"end\" to quit program.");
		String location = null;
		while (!location.equals("end")) {
			location = kb.next();
		}

		kb.close();
	}

	/**
	 * Start the HTTP Server.
	 *
	 * @return    The <code>HttpServer</code>.
	 */

	private HttpServer startServer() {
		try {
			int port = getPort();

			URI baseUri = UriBuilder.fromUri("http://138.67.186.221/").port(port).build();
			ResourceConfig config = new ResourceConfig(getClasses());

			config.register(JacksonJsonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);
			config.register(new ObjectMapperResolver());
			GenericExceptionMapper.register(config);
			config.register(new CORSResponseFilter());
			HttpServer serve = GrizzlyHttpServerFactory.createHttpServer(baseUri, config, false);
			serve.start();

			return serve;
		}
		catch(Throwable exp) {
			exp.printStackTrace();
		}
		return null;
	}

	/**
	 * Register the resources.
	 *
	 * @return     A Set of resource classes.
	 */

	private Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();

		classes.add(TestRes.class);
		classes.add(PeopleResource.class);

		return classes;
	}


	/**
	 * Return the port to use for the service.
	 * <P/>
	 * This should be smarter!
	 *
	 * @return    The port.
	 */

	private int getPort() {
		//return 0;
		return 9898;
	}
}
