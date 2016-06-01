package com.deh.b2r.server;

import java.io.FileNotFoundException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
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
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;


import java.util.Scanner;

public class Fred
{
  public static void main(String... args) {
    Fred fred = new Fred();
    Scanner kb = new Scanner(System.in);
    String location = "";
    
    HttpServer server = fred.startServer();
    server.getListener("grizzly").createManagementObject();
    
    while(!server.isStarted()){}
    System.out.println("Type \"shutdown\" to quit server.");
    
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target("http://138.67.186.222:9898/");
    
    //TODO Add place in URI to get jars. Make it restful
    //TODO Add pulling of correct jar
    //TODO Add security check for jars

    //Only allows for "update/JARNAMEHERE"
    while (true){
    	System.out.print("Where do you want to go: ");
    	location = kb.nextLine();
    	if (location.equals("shutdown")) break;
    	else {
    		Response response = target.path(location).request(MediaType.APPLICATION_JSON_TYPE).get();
			//System.out.println("Response is " + response.toString());   

			String path = Updater.getUpdatedJarName();
		    
			//Create MD5 checksum for jar file verification
			try {
				System.out.println("The checkSum for " + " is.. " + SecurityChecksum.hashPassword(path));
			} catch (NoSuchAlgorithmException | FileNotFoundException e) {
				
				System.out.println("File not found for checkSum!!!");
			}
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
   *
   * @return    The <code>HttpServer</code>.
   */

  private HttpServer startServer() {
    try {
      int port = getPort();
      
      URI baseUri = UriBuilder.fromUri("http://138.67.186.222/").port(port).build();
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
