package com.deh.b2r.server;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.core.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Provider of the Jackson <code>ObjectMapper</code>.
 *
 * @version      $Rev$
 */

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ObjectMapperResolver implements ContextResolver<ObjectMapper>
{
  private final ObjectMapper mapper;

  /**
   * Create the mapper - it should be a singleton.
   */

  public ObjectMapperResolver() {
    mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
  }

  @Override
  public ObjectMapper getContext(Class<?> type) {
    return mapper;
  }
}
