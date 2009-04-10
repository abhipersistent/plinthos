package org.plinthos.shared.gateway.commands.request;

import org.plinthos.shared.gateway.commands.common.Response;

public class SubmitResponse extends Response {

	private static final long serialVersionUID = -1127631672766341462L;
	
	private String requestId;
	
	public SubmitResponse() {
		
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	
}
