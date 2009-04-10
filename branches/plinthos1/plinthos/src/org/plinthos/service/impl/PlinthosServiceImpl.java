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
package org.plinthos.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.plinthos.core.PlinthosErrorCodes;
import org.plinthos.core.PlinthosException;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.PlinthosRequestStatus;
import org.plinthos.core.process.ProcessUtil;
import org.plinthos.core.process.RequestManager;
import org.plinthos.core.queue.QueueRequest;
import org.plinthos.core.queue.RequestPlacer;
import org.plinthos.queue.Placer;
import org.plinthos.service.PlinthosService;
import org.plinthos.service.RequestStatus;


/**
 * TODO: Add documentation
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class PlinthosServiceImpl implements PlinthosService {

	private static final Logger log = Logger.getLogger(PlinthosServiceImpl.class);
	
	/* (non-Javadoc)
	 * @see org.plinthos.service.PlinthosService#cancelRequest(java.lang.String)
	 */
	public RequestStatus cancelRequest(List<String> requestIds) {
		log.info("cancelRequest() : " + requestIds);
		RequestStatus status = new RequestStatus();
		status.setRequestId("");
		status.setStatus("CANCELLED");
		status.setEstimatedTimeToComplete(-1);
		RequestManager mgr = ProcessUtil.getRequestManager();
		for (String x : requestIds ) {
			try {
				mgr.cancelRequest(Integer.parseInt(x));
			} catch (PlinthosException e) {
				status.setStatus("FAILED");
			}
		}
		return status;
	}

	/* (non-Javadoc)
	 * @see org.plinthos.service.PlinthosService#queryStatus(java.util.List)
	 */
	public List<RequestStatus> queryStatus(List<String> requestIds) {
		log.info("queryStatus() : " + requestIds);
		List<RequestStatus> statusList = new ArrayList<RequestStatus>(requestIds.size());
		RequestManager mgr = ProcessUtil.getRequestManager();
		for (String x : requestIds ) {
			RequestStatus status = new RequestStatus();
			int requestId = Integer.parseInt(x);
			status.setRequestId(x);
			PlinthosRequest request = mgr.getRequest(requestId);
			if ( request == null ) {
				status.setStatus("MISSING");
			} else {
				if ( request.getStatus().equals(PlinthosRequestStatus.SUBMITTED) ||
						request.getStatus().equals(PlinthosRequestStatus.IN_PROGRESS) ) {
					status.setEstimatedTimeToComplete(mgr.getETC(request));
				} else {
					status.setEstimatedTimeToComplete(-1);
				}
				status.setDateSubmitted(request.getSubmissionTime().getTime());
				if ( request.getCompletionTime() != null ) {
					status.setDateCompleted(request.getCompletionTime().getTime());
				}
				status.setStatus(request.getStatus());
			}
			log.debug("QueueRequest Status [" + x + ", " + status.getStatus() + "]");
			statusList.add(status);
		}
		return statusList;
	}
	
	/* (non-Javadoc)
	 * @see org.plinthos.service.PlinthosService#submitRequest(java.lang.String)
	 */
	public RequestStatus submitRequest(String requestType, String userId, int priority, int expiresAfterMinutes, String xmlData) {
		
		StringBuilder msg = new StringBuilder("QueueRequest received:\n");
		msg.append("   QueueRequest Type         : ").append(requestType);
		msg.append("   User ID              : ").append(userId);
		msg.append("   Priority Level       : ").append(priority);
		msg.append("   Expiration Time (min): ").append(expiresAfterMinutes);
		msg.append("   QueueRequest XML          :\n").append(xmlData).append("\n");
		log.debug(msg.toString());
		
		RequestStatus status = new RequestStatus();
		status.setEstimatedTimeToComplete(-1);
		RequestManager mgr = ProcessUtil.getRequestManager();
		
		try {
			// Persist the incoming request.
			PlinthosRequest request = mgr.scheduleRequest(requestType, userId, priority, expiresAfterMinutes, xmlData);
			
			// Place it in the queue
			QueueRequest queueRequest = new QueueRequest(request.getId());
	        queueRequest.setPriority(request.getPriority());
	        queueRequest.setSize(1000);
	        queueRequest.setExpirationTime(request.getExpiration().getTime());

	        // Get the reference to place the request in the queue
	        Placer placer = RequestPlacer.getInstance();
	        if ( placer.placeRequest(queueRequest) == false ) {
	        	mgr.updateRequestStatus(request.getId(), PlinthosRequestStatus.REJECTED);
	        	request.setStatus(PlinthosRequestStatus.REJECTED);
	        	throw new PlinthosException(PlinthosErrorCodes.QUEUE_NOT_EMPTY_EXCEPTION);
	        }
			status.setRequestId(String.valueOf(request.getId()));
			status.setStatus(request.getStatus());
			log.info("request processed [id " + request.getId() + ", status " + request.getStatus() + "\n");
		} catch (PlinthosException e) {
			log.error("request failed ", e);
			status.setStatus("REJECTED");
			status.setErrorCode(e.getMessageCode());
			status.setErrorMessage(e.getMessage());
		} catch (Exception e) {
			log.error("request failed ", e);
			status.setStatus("FAILED");
			status.setErrorCode(501);
			status.setErrorMessage(e.getMessage());
		}
		
		return status;
	}
}
