package com.drillinginfo.rest.control;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Register the exception handlers.
 */
public class GenericExceptionMapper
{
  /**
   * Register the exception handlers with the config.
   *
   * @param config The {@code ResourceConfig}.
   */
  public static void register(ResourceConfig config) {
    config.register(new ThrowableMapper());
  }

  /**
   * Catch all handler.
   */
  public static class ThrowableMapper implements ExceptionMapper<Throwable>
  {
    @Override
    public Response toResponse(final Throwable exp) {
      return Response.status(404).entity(exp.getMessage())
          .type("text/plain").build();
    }
  }
}
