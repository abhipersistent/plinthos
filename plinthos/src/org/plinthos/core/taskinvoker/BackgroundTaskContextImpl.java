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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.service.RequestManager;
import org.plinthos.shared.gateway.commands.request.RequestDetails;
import org.plinthos.shared.plugin.BackgroundTaskContext;

public class BackgroundTaskContextImpl implements BackgroundTaskContext {

	private RequestManager requestManager;
	
	public BackgroundTaskContextImpl(){
		
	}

	@Override
	public String getRequestParameters(int requestId) {
		return requestManager.getRequest(requestId).getRequestParams();
	}

	@Override
	public List<RequestDetails> findRequestsByTaskTypeAndStatusAndCompletionTime(
			String taskType, String status, Date completionTime) {
		List<PlinthosRequest> requests = requestManager.findRequestsByTaskTypeAndStatusAndCompletionTime(
				taskType,status, completionTime);
		RequestDetails vo;
		List<RequestDetails> requestDetails = new ArrayList<RequestDetails>();

		for (PlinthosRequest request : requests) {
			vo = new RequestDetails();
			loadRequestDetails(vo, request);
			requestDetails.add(vo);
		}

		return requestDetails;
	}

	private void loadRequestDetails(RequestDetails vo, PlinthosRequest r) {

		vo.setCancelRequested(r.isCancelRequested());
		vo.setCompletionDate(r.getCompletionTime());
		vo.setEtc(r.getEtc());
		vo.setCorrelationId(r.getCorrelationId());
		vo.setExpirationDate(r.getExpiration());
		vo.setPriority(r.getPriority());
		vo.setProgressMessage(r.getProgressMessage());
		vo.setRequestData(r.getRequestParams());
		vo.setRequestId(String.valueOf(r.getId()));
		vo.setResponseData(r.getRequestResults());
		vo.setStatus(r.getStatus());
		vo.setStatusMessage(r.getStatusMessage());
		vo.setSubmitDate(r.getSubmissionTime());
		vo.setType(r.getTaskInfo().getTaskType());
		vo.setUserId(r.getUserId());

	}

	@Override
	public void setRequestManager(RequestManager requestManager) {
		this.requestManager=requestManager;
	}
}
