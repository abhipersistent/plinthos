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
package org.plinthos.core.queue.priorityweighted;

//Log4J API
import org.apache.log4j.Logger;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.queue.Queue;
import org.plinthos.core.queue.QueuePlacer;
import org.plinthos.core.queue.QueueRequest;


public class PriorityWeightedQueuePlacer implements QueuePlacer {

	private static final Logger log = Logger.getLogger(PriorityWeightedQueuePlacer.class);

	private PriorityWeightedQueue queue;

	private int maxQueuedRequestId;

	public PriorityWeightedQueuePlacer(PriorityWeightedQueue q) {
		queue = q;
		maxQueuedRequestId = -1;		
	}


	// @Override
	public int getMaxQueuedRequestId() {
		return maxQueuedRequestId;
	}


	// @Override
	public boolean placeRequest(QueueRequest r) {
		
		boolean status = false;
		
		synchronized (queue) {
			status = queue.enqueue(r);
			if (status) {
				maxQueuedRequestId = Math.max(maxQueuedRequestId, r.getRequestId());				
				log.info("Placed the request in the queue with id " + r.getRequestId());
				queue.notifyAll();
			}
		}
		
		log.debug("Leaving from placeRequest method");
		return status;
	}


	// @Override
	public boolean placeRequest(PlinthosRequest r) {
		PriorityWeightedQueueRequest qR = new PriorityWeightedQueueRequest(r.getId());
		if( r.getExpiration() != null ) {
			qR.setExpirationTime(r.getExpiration().getTime());
		}
		else {
			qR.setExpirationTime(PriorityWeightedQueueRequest.EXPIRATION_TIME_DEFAULT);
		}
		qR.setPriority(r.getPriority());
		qR.setSize(PriorityWeightedQueueRequest.SIZE_DEFAULT); 
		
		return placeRequest(qR);
	}


	@Override
	public Queue getQueue() {
		return queue;
	}
}
