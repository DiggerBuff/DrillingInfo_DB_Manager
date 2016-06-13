package exception;

import javax.xml.ws.WebServiceException;

public class SecurityError extends WebServiceException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3229434950761445737L;
	
	public SecurityError(String message) {
		super(message);
	}
}
