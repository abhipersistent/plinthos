package org.plinthos.shared.gateway.commands.request;

import org.plinthos.shared.gateway.commands.common.Request;

public class CancelRequest extends Request {

	private static final long serialVersionUID = 5974912721190932164L;
	
	private String requestId;
	
	public CancelRequest() {
		
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	
}
