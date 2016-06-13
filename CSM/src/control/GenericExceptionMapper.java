package control;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.glassfish.jersey.server.ResourceConfig;

import exception.LocalFileError;

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
		config.register(new LocalFilesMapper());
	}

	/**
	 * Catch all handler.
	 */
	public static class ThrowableMapper implements ExceptionMapper<Throwable>
	{
		// Implements:  ExceptionMapper	
		@Override
		public Response toResponse(Throwable exp) {
			return Response.status(404).entity(exp.getMessage()).type("text/plain").build();
		}
	}

	public static class LocalFilesMapper implements ExceptionMapper<LocalFileError>
	{
		@Override
		public Response toResponse(LocalFileError exp) {
			return Response.status(500).entity(exp.getMessage()).type("text/plain").build();
		}
	}

}
