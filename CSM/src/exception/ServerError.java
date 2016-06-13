package exception;

import javax.xml.ws.WebServiceException;

public class ServerError extends WebServiceException {

	private static final long serialVersionUID = 7892099914106598673L;

	/**
	 * Constructor to set the error message.
	 * 
	 * @param message The error message you want to set.
	 */
	public ServerError(String message){
		super(message);
	}
}
