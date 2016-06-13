package exception;

import javax.xml.ws.WebServiceException;

public class ServerError extends WebServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7892099914106598673L;

	public ServerError(String message){
		super(message);
	}
}
