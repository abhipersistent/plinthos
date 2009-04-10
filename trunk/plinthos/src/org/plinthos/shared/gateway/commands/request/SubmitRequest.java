package org.plinthos.shared.gateway.commands.request;

import java.util.Date;

import org.plinthos.shared.gateway.commands.common.Request;

public class SubmitRequest extends Request {

	private static final long serialVersionUID = -6185795714151287841L;
	
	private String correlationId;
	 private String userId;
	 private int priority;
	 private Date expirationDate;
	 private String type;
	 private String requestData;
	 
	 public SubmitRequest() {
		 
	 }

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}
	 
	 
	 
}
