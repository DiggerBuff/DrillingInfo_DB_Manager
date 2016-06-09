package control;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * TODO Flesh out exception handlers
 * 
 * Register the exception handlers.
 */
public class GenericExceptionMapper
{
	
  /**
   * Register the exception handlers with the config.
   *
   * @param   config    The <code>ResourceConfig</code>.
   */
  public static void register(ResourceConfig config) {
    config.register(new ThrowableMapper());
  }

  /**
   * Catch all handler.
   */
  public static class ThrowableMapper implements ExceptionMapper<Throwable>
  {
    // Implements:  ExceptionMapper	
    public Response toResponse(Throwable exp) {
      return Response.status(404).entity(exp.getMessage()).type("text/plain").build();
    }
  }
}
