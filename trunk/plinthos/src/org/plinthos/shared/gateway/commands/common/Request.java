package org.plinthos.shared.gateway.commands.common;

import java.util.Date;


public class Request implements java.io.Serializable {

	private static final long serialVersionUID = -6856681359586558111L;

	private Date requestTimestamp;
	
	protected Request() {
		requestTimestamp = new Date();
	}

	public Date getRequestTimestamp() {
		return requestTimestamp;
	}

	public void setRequestTimestamp(Date requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}
	
	
}
