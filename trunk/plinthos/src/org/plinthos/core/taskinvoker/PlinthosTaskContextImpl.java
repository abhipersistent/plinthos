package org.plinthos.core.taskinvoker;

import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.service.RequestManager;
import org.plinthos.shared.plugin.PlinthosTaskContext;

public class PlinthosTaskContextImpl implements PlinthosTaskContext {

	private int requestId;
	private RequestManager requestManager;
	private String statusMessage;
	
	public PlinthosTaskContextImpl() {
		
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public void setRequestManager(RequestManager r) {
		this.requestManager = r;
	}
	
	public RequestManager getRequestManager() {
		return requestManager;
	}
	
	@Override
	public boolean isTaskCancelled() {
		PlinthosRequest r = requestManager.getRequest(requestId);
		return r.isCancelRequested();  
	}

	@Override
	public void setResults(String data) {
		requestManager.saveResults(requestId, data);
	}


	@Override
	public void setProgressMessage(String msg) {
		requestManager.updateProgressMessage(requestId, msg);
	}

	@Override
	public void setStatusMessage(String msg) {
		this.statusMessage = msg;
	}

	@Override
	public String getStatusMessage() {
		return statusMessage;
	}
}
