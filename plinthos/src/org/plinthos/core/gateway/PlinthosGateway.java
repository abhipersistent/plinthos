package org.plinthos.core.gateway;

import java.util.ArrayList;
import java.util.List;

import org.plinthos.core.framework.Constants;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.RegisteredTask;
import org.plinthos.core.service.RequestManager;
import org.plinthos.core.service.ServiceFactory;
import org.plinthos.core.service.TaskRegistry;
import org.plinthos.shared.gateway.commands.admin.PingRequest;
import org.plinthos.shared.gateway.commands.admin.PingResponse;
import org.plinthos.shared.gateway.commands.common.Request;
import org.plinthos.shared.gateway.commands.common.Response;
import org.plinthos.shared.gateway.commands.request.CancelRequest;
import org.plinthos.shared.gateway.commands.request.CancelResponse;
import org.plinthos.shared.gateway.commands.request.FindRequest;
import org.plinthos.shared.gateway.commands.request.FindResponse;
import org.plinthos.shared.gateway.commands.request.RequestDetails;
import org.plinthos.shared.gateway.commands.request.SubmitRequest;
import org.plinthos.shared.gateway.commands.request.SubmitResponse;
import org.plinthos.shared.gateway.mapping.CommandXmlMapping;

public class PlinthosGateway {

	public String executeXml(String xmlRequest) {
		CommandXmlMapping mapper = new CommandXmlMapping();
		Request request = (Request)mapper.fromXML(xmlRequest);
		Response response = execute(request);
		return mapper.toXML(response);
	}
	
	public Response execute(Request r) {

		Response response = null;
		
		if( r instanceof SubmitRequest ) {
			response = execute((SubmitRequest) r);
		}
		else if( r instanceof CancelRequest ) {
			response = execute( (CancelRequest) r);
		}
		else if( r instanceof FindRequest ) {
			response = execute( (FindRequest) r);
		}
		else if( r instanceof PingRequest ) {
			response = execute( (PingRequest) r);
		}
		else {
			response = new Response();
			response.setResponseStatus(Response.STATUS_ERROR);
			response.setResponseStatusMessage("Unknown request: " + r.getClass().getCanonicalName());
		}
		
		return response;
	}
	
	
	public SubmitResponse execute(SubmitRequest r) {
		RequestManager requestManager = ServiceFactory.getInstance().getRequestManager();
		
		PlinthosRequest pR = new PlinthosRequest();
		pR.setCorrelationId(r.getCorrelationId());
		pR.setCompletionTime(null);
		pR.setExpiration(r.getExpirationDate());
		pR.setPriority(r.getPriority());
		pR.setRequestParams(r.getRequestData());
		pR.setStatus(null);
		pR.setSubmissionTime(null);
		
		TaskRegistry taskRegistry = ServiceFactory.getInstance().getTaskRegistry();
		RegisteredTask taskInfo = taskRegistry.findTask(r.getType());
		pR.setTaskInfo(taskInfo);
		
		pR.setTaskInfo(taskInfo);
		pR.setUserId(r.getUserId());

		SubmitResponse response = new SubmitResponse();
		boolean checkIncompleteRequestCount = false;
		if( Constants.MAX_INCOMPLETE_REQUESTS_ALLOWED_AT_A_TIME >= 0 ) {
			checkIncompleteRequestCount = true;
		}
		
		if( checkIncompleteRequestCount &&
			requestManager.findIncompleteRequestCount() > Constants.MAX_INCOMPLETE_REQUESTS_ALLOWED_AT_A_TIME) {

			response.setRequestId(null);
			response.setResponseStatus(Response.STATUS_ERROR);
			response.setResponseStatusMessage(
					"Request rejected (correlationId=" + pR.getCorrelationId() + ")" +
					" - max incomplete request count exceeds configured value. " +
					"MAX_INCOMPLETE_REQUESTS_ALLOWED_AT_A_TIME = " + 
					Constants.MAX_INCOMPLETE_REQUESTS_ALLOWED_AT_A_TIME 
					);
			
			
		}
		else {
			pR = requestManager.createRequest(pR);
			response.setRequestId(String.valueOf(pR.getId()));
		}
		return response;
	}
	
	public CancelResponse execute(CancelRequest r) {
		RequestManager requestManager = ServiceFactory.getInstance().getRequestManager();
		int requestId = Integer.parseInt(r.getRequestId());
		requestManager.cancelRequest(requestId);
		CancelResponse response = new CancelResponse();
		return response;
	}

	public PingResponse execute(PingRequest r) {
		PingResponse response = new PingResponse();
		response.setMessage(r.getMessage());
		return response;
	}
	
	public FindResponse execute(FindRequest r) {
		
		RequestManager requestManager = ServiceFactory.getInstance().getRequestManager();
		
		List<RequestDetails> requests = new ArrayList<RequestDetails>();
		
		if( r.getRequestId() != null ) {
			int requestId = Integer.parseInt(r.getRequestId());
			PlinthosRequest pR = requestManager.getRequest(requestId);
			RequestDetails vo = new RequestDetails();
			if( vo != null ) {
				loadRequestDetails(vo, pR, r.isIncludeRequestData(), r.isIncludeResponseData());
				requests.add(vo);
			}
		}

		// fall back on correlationId if requestId is not provided or 
		// we haven't found request with such id.
		if( requests.size() == 0 ) {
			if( r.getCorrelationId() != null ) {
				 
				List<PlinthosRequest> list = 
					requestManager.findRequestsByCorrelationId(r.getCorrelationId());
				
				for(PlinthosRequest pR : list) {
					RequestDetails vo = new RequestDetails();
					loadRequestDetails(vo, pR,  r.isIncludeRequestData(), r.isIncludeResponseData());
					requests.add(vo);
				}
			}
		}

		FindResponse response = new FindResponse();
		response.setRequests(requests);
		
		return response;
	}

	private void loadRequestDetails(RequestDetails vo, PlinthosRequest r, boolean includeRequestData, boolean includeResponseData) {

		vo.setCancelRequested(r.isCancelRequested());
		vo.setCompletionDate(r.getCompletionTime());
		vo.setCorrelationId(r.getCorrelationId());
		vo.setExpirationDate(r.getExpiration());
		vo.setPriority(r.getPriority());
		vo.setProgressMessage(r.getProgressMessage());
		if( includeRequestData ) {
			vo.setRequestData(r.getRequestParams());
		}
		vo.setRequestId(String.valueOf(r.getId()));
		
		if( includeResponseData ) {
			vo.setResponseData(r.getRequestResults());
		}
		vo.setStatus(r.getStatus());
		vo.setStatusMessage(r.getStatusMessage());
		vo.setSubmitDate(r.getSubmissionTime());
		vo.setType(r.getTaskInfo().getTaskType());
		vo.setUserId(r.getUserId());
		
	}
	
}
