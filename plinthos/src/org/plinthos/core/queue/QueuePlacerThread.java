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
package org.plinthos.core.queue;

import java.util.List;

import org.apache.log4j.Logger;
import org.plinthos.core.framework.Constants;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.PlinthosRequestStatus;
import org.plinthos.core.service.RequestManager;
import org.plinthos.core.service.ServiceFactory;


public class QueuePlacerThread extends Thread {

	private static final Logger log = Logger.getLogger(QueuePlacerThread.class);

	private QueuePlacer placer;

	private RequestManager requestManager;
	
	public QueuePlacerThread(QueuePlacer requestPlacer) {
		this.setName(getClass().getSimpleName() + "@" + hashCode());
		this.placer = requestPlacer;
		this.requestManager = ServiceFactory.getInstance().getRequestManager();		
	}

	public void setRequestManager(RequestManager m) {
		this.requestManager = m;
	}
	
	public RequestManager getRequestManager() {
		return requestManager;
	}
	
	public void run() {

		//TODO: move into external configuration
		final long REQUEST_PLACER_PAUSE_FOR_FULL_QUEUE = 1 * 1000;
		final long REQUEST_PLACER_PAUSE_FOR_NO_REQUESTS = 1 * 1000;
		final int MAX_REQUESTS_FETCH_SIZE = Constants.QUEUE_CAPACITY * 2;
		try {
			while( true ) {
				
				try {
					List<PlinthosRequest> pRequests = 
						requestManager.findNewRequests(placer.getMaxQueuedRequestId(), MAX_REQUESTS_FETCH_SIZE);
	
					// clean up requests that expired within queue 
					Queue queue = placer.getQueue();
					for( QueueRequest qR : queue.getExpiredRequests() ) {
						requestManager.updateRequestStatus(
								qR.getRequestId(), 
								PlinthosRequestStatus.EXPIRED, 
								"expired");
						queue.remove(qR);
					}
					
					boolean queueIsFull = false;
					boolean noNewRequests = pRequests.size() == 0;
					
					for(PlinthosRequest pR : pRequests) {
						
						// do not schedule request if it has request for cancellation
						if( pR.isCancelRequested() ) {
							pR.setStatus(PlinthosRequestStatus.CANCELED);
							requestManager.updateRequestStatus(
									pR.getId(), 
									PlinthosRequestStatus.CANCELED, 
									null);
							continue;
						}
						
						if( pR.getExpiration() != null ) {
							if( System.currentTimeMillis() >= pR.getExpiration().getTime() ) {
								pR.setStatus(PlinthosRequestStatus.EXPIRED);
								requestManager.updateRequestStatus(pR.getId(), 
										PlinthosRequestStatus.EXPIRED, 
										"expired");
								continue;
							}
						}
						
						if( placer.placeRequest(pR) == false ) {
							queueIsFull = true;
							break;
						}
					}
					
					if( queueIsFull ) {
						Thread.sleep(REQUEST_PLACER_PAUSE_FOR_FULL_QUEUE);
					}
					
					if( noNewRequests ) {
						Thread.sleep(REQUEST_PLACER_PAUSE_FOR_NO_REQUESTS);
					}
					
					Thread.yield();
				}
				catch(Throwable e) {
					log.error("Encounter exception and Ignored:" + e.getMessage(), e);
				}
				
			}
		}
		finally {
			log.info("Thread was stopped.");
		}
	}
}
