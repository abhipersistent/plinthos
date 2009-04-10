package org.plinthos.shared.gateway.commands.request;

import org.plinthos.shared.gateway.commands.common.Request;

public class FindRequest extends Request {

	private static final long serialVersionUID = -8897924799053177317L;
	
	private String requestId;
	private String correlationId;
	private boolean includeRequestData;
	private boolean includeResponseData;
	
	public FindRequest() {
		
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public boolean isIncludeRequestData() {
		return includeRequestData;
	}

	public void setIncludeRequestData(boolean includeRequestData) {
		this.includeRequestData = includeRequestData;
	}

	public boolean isIncludeResponseData() {
		return includeResponseData;
	}

	public void setIncludeResponseData(boolean includeResponseData) {
		this.includeResponseData = includeResponseData;
	}
	
	
}
