package com.drillinginfo.rest.control;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * The managed REST HTTP Server.
 */
public class RESTServer
{
  /**
   * Singleton for REST Server.
   */
  private static RESTServer _RESTServer = null;
  
  /**
   * Internal HTTP Server.
   */
  private final HttpServer _server;
  
  /**
   * Private constructor. Use initialize.
   */
  private RESTServer()
  {
    _server = startServer();
  }
  
  /**
   * Initializes the REST Server if it is null.
   */
  public static void initialize()
  {
    if (_RESTServer == null)
    {
      _RESTServer = new RESTServer();
    }
  }
  
  /**
   * Internal access to the HTTP Server.
   * 
   * @return The server.
   */
  private HttpServer getHttpServer()
  {
    return _server;
  }
  
  /**
   * Shutdown the REST Server.
   */
  public static void shutdown()
  {
    if (_RESTServer != null)
    {
      _RESTServer.getHttpServer().shutdownNow();
    }
  }
  
  /**
   * Determine if the REST Server is running.
   * 
   * @return {@code true} if the server is running.
   */
  public static boolean isRunning()
  {
    if (_RESTServer != null)
    {
      return _RESTServer.getHttpServer().isStarted();
    }
    return false;
  }
  
  /**
   * Configure and start the HTTP Server.
   *
   * @return The {@code HttpServer}.
   */
  private HttpServer startServer() {
    try 
    {
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
    catch(Throwable e) 
    {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Register the resources.
   *
   * @return A Set of resource classes.
   */
  private Set<Class<?>> getClasses() 
  {
    // TODO allow configuration or detection
    Set<Class<?>> classes = new HashSet<>();
    classes.add(RootResource.class);
    return classes;
  }

  /**
   * Return the port to use for the service.
   * 
   * @return The port.
   */
  private int getPort() 
  {
    // TODO allow configuration or detection
    return 9898;
  }
}
