package org.plinthos.shared.gateway.commands.admin;

import org.plinthos.shared.gateway.commands.common.Request;

public class PingRequest extends Request {

	private static final long serialVersionUID = -5277539794615543960L;
	
	private String message;
	
	public PingRequest() {
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
