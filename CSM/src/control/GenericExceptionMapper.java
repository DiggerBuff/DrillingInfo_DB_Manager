package control;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ResourceConfig;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

import exception.ErrorMessage;
import exception.LocalFileError;
import exception.SecurityError;
import exception.ServerError;

/**
 * Register the exception handlers.
 */
public class GenericExceptionMapper
{

	/**
	 * Register the exception handlers with the ResourceConfig.
	 *
	 * @param   config    The <code>ResourceConfig</code>.
	 */
	public static void register(ResourceConfig config) {
		config.register(new ThrowableMapper());
		config.register(new LocalFilesMapper());
		config.register(new AmazonServiceMapper());
		config.register(new AmazonClientMapper());
		config.register(new SecurityErrorMapper());
		config.register(new ServerErrorMapper());
	}

	/**
	 * Catch all handler. Puts a 404 error code.
	 */
	@Provider
	public static class ThrowableMapper implements ExceptionMapper<Throwable>
	{
		@Override
		public Response toResponse(Throwable exp) {
			ErrorMessage errorMessage = new ErrorMessage(exp.getMessage(), 404, "http://info.drillinginfo.com");
			return Response.status(errorMessage.getErrorCode()).entity(errorMessage).build();
		}
	}
	
	/**
	 * Catch the local file errors such as unable to write to file.
	 */
	@Provider
	public static class LocalFilesMapper implements ExceptionMapper<LocalFileError>
	{
		@Override
		public Response toResponse(LocalFileError exp) {
			ErrorMessage errorMessage = new ErrorMessage(exp.getMessage(), 500, "http://info.drillinginfo.com");
			return Response.status(errorMessage.getErrorCode()).entity(errorMessage).build();
		}
	}
	
	/**
	 * Catch the Amazon Server errors such as invalid credentials.
	 */
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
	
	/**
	 * Catch the Amazon Client errors such as unable to connect to S3.
	 */
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
	
	/**
	 * Catch the Security errors such as unable to match the MD5 checksum.
	 */
	@Provider
	public static class SecurityErrorMapper implements ExceptionMapper<SecurityError>
	{
		@Override
		public Response toResponse(SecurityError e) {
			ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), Response.Status.PRECONDITION_FAILED.getStatusCode(), "http://info.drillinginfo.com");
			return Response.status(errorMessage.getErrorCode()).entity(errorMessage).build();
		}
	}
	
	/**
	 * Catch the Server errors such as unable to create the server.
	 */
	@Provider
	public static class ServerErrorMapper implements ExceptionMapper<ServerError>
	{
		@Override
		public Response toResponse(ServerError e) {
			ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), 421, "http://info.drillinginfo.com");
			return Response.status(errorMessage.getErrorCode()).entity(errorMessage).build();
		}
	}
}
