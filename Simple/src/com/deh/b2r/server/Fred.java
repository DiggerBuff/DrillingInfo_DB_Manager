package com.deh.b2r.server;


import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class Fred
{
  public static void main(String... args) {
    Fred fred = new Fred();
    fred.startServer();

    System.out.println("Hi");

    while (true) {
    }

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
      config.register(JacksonJsonProvider.class, MessageBodyReader.class,
                      MessageBodyWriter.class);
      config.register(new ObjectMapperResolver());
      GenericExceptionMapper.register(config);
      config.register(new CORSResponseFilter());
      return GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
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
