package control;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ResourceConfig;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

import exception.ErrorMessage;
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
		config.register(new AmazonServiceMapper());
		config.register(new AmazonClientMapper());
	}

	/**
	 * Catch all handler.
	 */
	@Provider
	public static class ThrowableMapper implements ExceptionMapper<Throwable>
	{
		//Implements:  ExceptionMapper	
		@Override
		public Response toResponse(Throwable exp) {
			ErrorMessage errorMessage = new ErrorMessage(exp.getMessage(), 404, "http://info.drillinginfo.com");
			return Response.status(errorMessage.getErrorCode()).entity(errorMessage).build();
		}
	}

	@Provider
	public static class LocalFilesMapper implements ExceptionMapper<LocalFileError>
	{
		@Override
		public Response toResponse(LocalFileError exp) {
			ErrorMessage errorMessage = new ErrorMessage(exp.getMessage(), 500, "http://info.drillinginfo.com");
			return Response.status(errorMessage.getErrorCode()).entity(errorMessage).build();
		}
	}

	@Provider
	public static class AmazonServiceMapper implements ExceptionMapper<AmazonServiceException>
	{
		@Override
		public Response toResponse(AmazonServiceException ase) {
			ErrorMessage errorMessage = new ErrorMessage("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.\n" + 
					ase.getMessage(), ase.getStatusCode(), "http://info.drillinginfo.com");
			return Response.status(errorMessage.getErrorCode()).entity(errorMessage).build();
		}
	}

	@Provider
	public static class AmazonClientMapper implements ExceptionMapper<AmazonClientException>
	{
		@Override
		public Response toResponse(AmazonClientException ace) {
			ErrorMessage errorMessage = new ErrorMessage("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.\n" + 
					ace.getMessage(), 404, "http://info.drillinginfo.com");
			return Response.status(errorMessage.getErrorCode()).entity(errorMessage).build();
		}
	}
}
