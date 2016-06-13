package exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage {
	
	@JsonProperty("ErrorMessage")
	private String errorMessage;
	@JsonProperty("ErrorCode")
	private int errorCode;
	@JsonProperty("Documentation")
	private String documntation;
	
	/**
	 * Default constructor. Does nothing really.
	 */
	public ErrorMessage() {
		
	}
	
	/**
	 * Constructor to create the message and fill all it's properties.
	 * 
	 * @param errorMessage The message to display to the user.
	 * @param errorCode The HTTP error code.
	 * @param documentation The location for the user to go to get help.
	 */
	public ErrorMessage(@JsonProperty("ErrorMessage") String errorMessage, @JsonProperty("ErrorCode") int errorCode, @JsonProperty("Documentation") String documentation) {
		super();
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.documntation = documentation;
	}
	
	/**
	 * Get the message to display to the user.
	 * 
	 * @return The message to display to the user.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	/**
	 * Set the message to display to the user.
	 * 
	 * @param errorMessage The message to display to the user.
	 */
	public void setErrorMessage(@JsonProperty("ErrorMessage") String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	/**
	 * Get the HTTP error code.
	 * 
	 * @return The HTTP error code.
	 */
	public int getErrorCode() {
		return errorCode;
	}
	
	/**
	 * Set the HTTP error code.
	 * 
	 * @param errorCode The HTTP error code.
	 */
	public void setErrorCode(@JsonProperty("ErrorCode") int errorCode) {
		this.errorCode = errorCode;
	}
	
	/**
	 * Get the location for the user to go to get help.
	 * 
	 * @return The location for the user to go to get help.
	 */
	public String getDocumntation() {
		return documntation;
	}
	
	/**
	 * Set the location for the user to go to get help.
	 * 
	 * @param documntation The location for the user to go to get help.
	 */
	public void setDocumntation(@JsonProperty("Documentation") String documntation) {
		this.documntation = documntation;
	}
}
