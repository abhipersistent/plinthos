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
package org.plinthos.core.process.impl;

import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;
import org.plinthos.core.PlinthosErrorCodes;
import org.plinthos.core.PlinthosException;
import org.plinthos.core.PlinthosRuntimeException;
import org.plinthos.core.dao.BaseDao;
import org.plinthos.core.dao.PlinthosRequestDao;
import org.plinthos.core.dao.impl.GenericEntityManagerDao;
import org.plinthos.core.dao.impl.PlinthosRequestDaoImpl;
import org.plinthos.core.dao.impl.RegisteredTaskDaoImpl;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.PlinthosRequestStatus;
import org.plinthos.core.model.RegisteredTask;
import org.plinthos.core.model.impl.PlinthosRequestImpl;
import org.plinthos.core.process.RequestManager;
import org.plinthos.core.queue.QueueFactory;
import org.plinthos.core.queue.QueueRequest;
import org.plinthos.core.queue.QueueService;
import org.plinthos.core.queue.RequestPlacer;
import org.plinthos.core.queue.RequestProcessor;
import org.plinthos.queue.Placer;
import org.plinthos.util.ServiceLocator;


/**
 * SLSB implementation of RequestManager.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 * @see org.plinthos.core.process.RequestManager
 */
@Stateless
@LocalBinding(jndiBinding="RequestManagerLocal")
public class RequestManagerImpl implements RequestManager {

	private static final Logger log = Logger.getLogger(RequestManagerImpl.class);
	
	static {
		/*
		 * Create the request processor object to process the queued requests
		 */
		RequestProcessor requestProcessor = new RequestProcessor();
		requestProcessor.start();
	}
	
	@PersistenceContext(unitName="PLINTHOS_DB")
	private EntityManager em;

	/* (non-Javadoc)
	 * @see org.plinthos.core.process.RequestManager#createRequest(java.lang.String, int, int, java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PlinthosRequest scheduleRequest(String requestType, 
			                               String userId, 
			                               int priority, 
			                               int expiresAfterMinutes, 
			                               String xmlData) 
	throws PlinthosException {

		log.info("Entering into createRequest method");
		
		RegisteredTask task = new RegisteredTaskDaoImpl(em).findByType(requestType);
		
		if ( task == null ) {
			throw new PlinthosException(PlinthosErrorCodes.UNKNOWN_REQUEST_TYPE, new Object[] {requestType});
		}
		
		String jndiName = "PlinthOS/"+ PlinthosRequestDaoImpl.class.getSimpleName() + "/local";
		
		PlinthosRequestDao requestDao = 
			(PlinthosRequestDao) ServiceLocator.getInstance().getFromJNDI(jndiName);
		
		PlinthosRequest plinthosRequest = 
			requestDao.createRequest(task, userId, priority, expiresAfterMinutes, xmlData);
        
        
		// Place it in the queue
		QueueRequest queueRequest = new QueueRequest(plinthosRequest.getId());
        queueRequest.setPriority(plinthosRequest.getPriority());
        queueRequest.setSize(1000);
        queueRequest.setExpirationTime(plinthosRequest.getExpiration().getTime());

        // Get the reference to place the request in the queue
        Placer placer = RequestPlacer.getInstance();
        if ( placer.placeRequest(queueRequest) == false ) {
        	requestDao.delete(plinthosRequest);
        	throw new PlinthosException(PlinthosErrorCodes.QUEUE_NOT_EMPTY_EXCEPTION);
        }
        log.debug("Leaving from createReportRequest method");
        return plinthosRequest;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateRequestStatus(int requestId, String status) {
		PlinthosRequest request = getRequest(requestId);
		if ( request == null ) {
			throw new PlinthosRuntimeException("Failed to get PlinthosRequest with id " + requestId);
		}
		request.setStatus(status);
		//set completion date for request.
		if (status.equalsIgnoreCase(PlinthosRequestStatus.COMPLETED)) {
			request.setCompletionTime(new Date());
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public PlinthosRequest getRequest(int requestId) {
		BaseDao<PlinthosRequest> dao = new GenericEntityManagerDao<PlinthosRequest>(PlinthosRequestImpl.class, em);
		return dao.findByPrimaryKey(requestId);
	}

	/* (non-Javadoc)
	 * @see org.plinthos.core.process.RequestManager#cancelRequest(int)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void cancelRequest(int requestId) throws PlinthosException {
		
		QueueService queue = (QueueService) QueueFactory.getQueue();
		
		if ( queue.removeRequest(requestId) ) {
			log.info("QueueRequest successfully removed from queue: " + requestId);
			updateRequestStatus(requestId, PlinthosRequestStatus.CANCELED);
		} else {
			throw new PlinthosException(PlinthosErrorCodes.REQUEST_CANCELLATION_FAILED,
										"Unable to remove request [" + requestId +"] from the QueueService array");
		}

		// the request has been successfully removed from the queue
		
		// TODO: Cancel the job if it is running

		log.info("QueueRequest successfully canceled : " + requestId);
	}

	/* (non-Javadoc)
	 * @see org.plinthos.core.process.RequestManager#getETC(org.plinthos.core.model.PlinthosRequest)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long getETC(PlinthosRequest request) {

		long estimatedTime = -1;
		
		RegisteredTask task = request.getTask();

		if (task.isEtcSupported()) {
			
			// TODO: Get the reference of the running thread
			estimatedTime = 0;
		}
		
		return estimatedTime;
	}

	
}
