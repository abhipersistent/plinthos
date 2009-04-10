package org.plinthos.shared.gateway.commands.request;

import java.util.List;

import org.plinthos.shared.gateway.commands.common.Response;

public class FindResponse extends Response {
	
	private static final long serialVersionUID = 189344717782002288L;
	
	List<RequestDetails> requests;
	
	public FindResponse() {
		
	}

	public List<RequestDetails> getRequests() {
		return requests;
	}

	public void setRequests(List<RequestDetails> requests) {
		this.requests = requests;
	}
	
	
	
}
