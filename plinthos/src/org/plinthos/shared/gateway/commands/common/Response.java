package org.plinthos.shared.gateway.commands.common;

import java.util.Date;

public class Response implements java.io.Serializable {

	private static final long serialVersionUID = -792171095322965205L;
	
	public static final String STATUS_ERROR = "ERROR";
	public static final String STATUS_SUCCESS = "SUCCESS";
	
	private Date responseTimestamp;
	private String responseStatus;
	private String responseStatusMessage;
	
	public Response() {
		responseTimestamp = new Date(); 
		responseStatus = STATUS_SUCCESS;
	}

	public Date getResponseTimestamp() {
		return responseTimestamp;
	}

	public void setResponseTimestamp(Date responseTimestamp) {
		this.responseTimestamp = responseTimestamp;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getResponseStatusMessage() {
		return responseStatusMessage;
	}

	public void setResponseStatusMessage(String responseStatusMessage) {
		this.responseStatusMessage = responseStatusMessage;
	}
	
	
}
