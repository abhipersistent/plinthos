package org.plinthos.shared.gateway.commands.admin;

import org.plinthos.shared.gateway.commands.common.Response;

public class PingResponse extends Response {

	private static final long serialVersionUID = 8169885548656093980L;
	
	private String message;
	
	public PingResponse() {
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
