/*
 * PlinthOS, Open Source Multi-Core and Distributed Computing.
 * Copyright 2003-2009, Emptoris Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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
	
	// @Override
	public boolean isTaskCancelled() {
		PlinthosRequest r = requestManager.getRequest(requestId);
		return r.isCancelRequested();  
	}

	// @Override
	public void setResults(String data) {
		requestManager.saveResults(requestId, data);
	}


	// @Override
	public void setProgressMessage(String msg) {
		requestManager.updateProgressMessage(requestId, msg);
	}

	// @Override
	public void setStatusMessage(String msg) {
		this.statusMessage = msg;
	}

	// @Override
	public String getStatusMessage() {
		return statusMessage;
	}

	// @Override
	public void setEtc(double etc) {
		requestManager.updateEtc(requestId, etc);
	}

	@Override
	public String getRequestTemplateParameters() {
		return requestManager.getRequest(requestId).getRequestParams();
	}
}
