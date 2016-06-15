package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import server.resource.TestRes;
import server.resource.VersionedJarResource;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import control.CORSResponseFilter;
import control.GenericExceptionMapper;
import control.ObjectMapperResolver;
import exception.ServerError;

import java.util.Scanner;

/**
 * This is our server class and client. It sets up everything/ has main.
 * This is where we add our resources.
 *
 */
public class Fred
{
	//private static final int port = getPort();
	private static final int port = 9999;
	private static String defaultUri = "http://";

	public static void main(String... args) {
		// Gets local ip. Commented out so that we just use localhost
		/*
		try {
			String localIP = InetAddress.getLocalHost().getHostAddress();
			defaultUri += localIP;
		} catch (UnknownHostException e) {
			throw new ServerError("Unable to get local IP to create connection.");
		}
		*/
		defaultUri += "localhost";
		
		Fred fred = new Fred();
		Scanner kb = new Scanner(System.in);
		String location = "";

		HttpServer server = fred.startServer();
		server.getListener("grizzly").createManagementObject();

		while(!server.isStarted()){}
		System.out.println("Type \"shutdown\" to quit server.");

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(defaultUri + ":" + port);

		while (true){
			System.out.print("Where do you want to go: ");
			location = kb.nextLine();
			if (location.equals("shutdown")) break;
			else {
				Response response = target.path(location).request(MediaType.APPLICATION_JSON_TYPE).get();
				System.out.println(response);
			}
		}

		System.out.println();
		server.shutdown();

		//It takes time to shut down. This ensures it shuts down before continuing.
		while(!server.shutdown().isDone()){}

		kb.close();
	}

	/**
	 * Start the HTTP Server.
	 * Configures the server.
	 * 
	 * @return    The <code>HttpServer</code>. Returns null if exception was thrown.
	 */
	private HttpServer startServer() {
		try {
			URI baseUri = UriBuilder.fromUri(defaultUri).port(port).build();
			ResourceConfig config = new ResourceConfig(getClasses());

			config.register(JacksonJsonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);
			config.register(new ObjectMapperResolver());
			GenericExceptionMapper.register(config);
			config.register(new CORSResponseFilter());
			HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config, false);
			server.start();

			return server;
		}
		catch(Throwable exp) {
			throw new ServerError("Unable to start local server to connect to DrillingInfo.");
		}
	}

	/**
	 * Register the resources.
	 *
	 * @return     A Set of resource classes.
	 */
	private Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();
		classes.add(TestRes.class);
		classes.add(VersionedJarResource.class);
		return classes;
	}

	/**
	 * Return the port to use for the service.
	 *
	 * @return    The port. -1 if there was an error. 
	 */
	private static int getPort() {
		try {
			ServerSocket s = new ServerSocket(0);
			s.close();
			return s.getLocalPort();
		} catch (IOException e) {
			throw new ServerError("Unable to get an available port to start server.");
		}
	}
}
