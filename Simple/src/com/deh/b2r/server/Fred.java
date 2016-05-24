package com.deh.b2r.server;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
//import org.glassfish.jersey.client.*;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import java.util.Scanner;

public class Fred
{
  public static void main(String... args) {
    Fred fred = new Fred();
    Scanner kb = new Scanner(System.in);
    String input = "";
    
    HttpServer server = fred.startServer();
    server.getListener("grizzly").createManagementObject();
    
    while(!server.isStarted()){}
    
    /*Client client = ClientBuilder.newClient();
    WebTarget target = client.target("http://localhost:9898/utility/streets");
    //SharedRep.Address address = new SharedRep.Address("Street");
    //Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity("Street", MediaType.APPLICATION_JSON));
    ClientResponse response = target.request().get(ClientResponse.class);
    System.out.println(response.getClass().toString());
    System.out.println(response.toString());*/
    
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target("http://localhost:9898/utility/test");
    String text = "Alan";
    Response response = target.request().post(Entity.text(text));
    System.out.println(response.toString());
    
    System.out.println("Type \"kill\" to quit server.");
    while (!input.equals("kill")) {
    	input = kb.next();
    	System.out.println(input);
    }
    
    System.out.println();
    server.shutdown();
    
    //It takes time to shut down. This ensures it shuts down before continuing.
    while(!server.shutdown().isDone()){}
    
    System.out.println("Type \"end\" to quit program.");
    while (!input.equals("end")) {
    	input = kb.next();
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
      
      URI baseUri = UriBuilder.fromUri("http://localhost/").port(port).build();
      ResourceConfig config = new ResourceConfig(getClasses());
      
      config.register(JacksonJsonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);
      config.register(new ObjectMapperResolver());
      GenericExceptionMapper.register(config);
      config.register(new CORSResponseFilter());
      HttpServer serve = GrizzlyHttpServerFactory.createHttpServer(baseUri, config, false);
      serve.start();
      //System.out.println(getClasses().toString());
      
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
    return 9898;
  }
}
