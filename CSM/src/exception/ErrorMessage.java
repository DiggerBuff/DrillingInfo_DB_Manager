package exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage {
	
	@JsonProperty("ErrorMessage")
	private String errorMessage;
	@JsonProperty("ErrorCode")
	private int errorCode;
	@JsonProperty("Documentation")
	private String documntation;
	
	public ErrorMessage() {
		
	}
	
	public ErrorMessage(@JsonProperty("ErrorMessage") String mes, @JsonProperty("ErrorCode") int code, @JsonProperty("Documentation") String doc) {
		super();
		errorMessage = mes;
		errorCode = code;
		documntation = doc;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(@JsonProperty("ErrorMessage") String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(@JsonProperty("ErrorCode") int errorCode) {
		this.errorCode = errorCode;
	}

	public String getDocumntation() {
		return documntation;
	}

	public void setDocumntation(@JsonProperty("Documentation") String documntation) {
		this.documntation = documntation;
	}
}
