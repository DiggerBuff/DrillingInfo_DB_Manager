package com.deh.b2r.server;

import java.net.URI;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
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
//import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
//import org.glassfish.jersey.client.*;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/*import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;*/

import java.util.Scanner;

public class Fred
{
  public static void main(String... args) {
    Fred fred = new Fred();
    Scanner kb = new Scanner(System.in);
    String location = "";
    String request = "";
    String input = "";
    
    HttpServer server = fred.startServer();
    server.getListener("grizzly").createManagementObject();
    
    while(!server.isStarted()){}
    System.out.println("Type \"utility/shutdown\" to quit server.");
    
    AddressBookClient client = new AddressBookClient();
   /* Client client2 = ClientBuilder.newClient();
    WebTarget target = client2.target("http://138.67.186.222:9898/");
    //Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity("{\"street\":\"aoeu\"}", MediaType.APPLICATION_JSON));
    Response response = target.path("streets").request(MediaType.APPLICATION_JSON_TYPE).get();
    System.out.println(response.toString());*/
     
    
    //client.get("streets");
    while (true){
    	System.out.print("Where do you want to go: ");
    	location = kb.nextLine();
    	if (location.equals("utility/shutdown")) break;
    	System.out.print("What do you want to do (l to list options): ");
    	request = kb.nextLine();
    	while (!request.equals("g") && !request.equals("p")) {
    		System.out.println("g - GET\np - POST\nl - List options");
    		System.out.print("What do you want to do: ");
        	request = kb.nextLine();
    	}
    	if (request.equals("p")){
    		System.out.print("Where do you want to post: ");
        	input = kb.nextLine();
        	client.post(location, "{\"street\":\""+input+"\"}");
    	}
    	if (request.equals("g")) {
    		System.out.println(client.get(location));
    		//client.post(location, "{\"street\":\""+input+" \"}");
    	}
    	
    }
    
    System.out.println();
    server.shutdown();
    
    //It takes time to shut down. This ensures it shuts down before continuing.
    while(!server.shutdown().isDone()){}
    
   /* System.out.println("Type \"end\" to quit program.");
    while (!location.equals("end")) {
    	location = kb.next();
    }*/
    
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
