package exception;

import javax.xml.ws.WebServiceException;

public class LocalFileError extends WebServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8889782594382417995L;
	
	public LocalFileError(String message){
		super(message);
	}

}
