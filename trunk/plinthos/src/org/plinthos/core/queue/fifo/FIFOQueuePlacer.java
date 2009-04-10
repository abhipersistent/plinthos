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
package org.plinthos.core.queue.fifo;

//Log4J API
import org.apache.log4j.Logger;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.queue.QueuePlacer;
import org.plinthos.core.queue.QueueRequest;


/**
 * Provides implementation to place requests in the Queue. This class is
 * singleton.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class FIFOQueuePlacer implements QueuePlacer {

	private static final Logger log = Logger.getLogger(FIFOQueuePlacer.class);

	private FIFOQueue requestQueue;

	private int maxQueuedRequestId;

	public FIFOQueuePlacer(FIFOQueue q) {
		requestQueue = q;
		maxQueuedRequestId = -1;		
	}


	@Override
	public int getMaxQueuedRequestId() {
		return maxQueuedRequestId;
	}


	@Override
	public boolean placeRequest(QueueRequest r) {
		
		boolean status = false;
		
		synchronized (requestQueue) {
			status = requestQueue.enqueue(r);
			if (status) {
				maxQueuedRequestId = Math.max(maxQueuedRequestId, r.getRequestId());				
				log.info("Placed the request in the queue with id " + r.getRequestId());
				requestQueue.notifyAll();
			}
		}
		
		log.debug("Leaving from placeRequest method");
		return status;
	}


	@Override
	public boolean placeRequest(PlinthosRequest r) {
		FIFOQueueRequest qR = new FIFOQueueRequest(r.getId());
		
		return placeRequest(qR);
	}
}
