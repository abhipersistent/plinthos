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

//Log4J API
import org.apache.log4j.Logger;
import org.plinthos.queue.Placer;
import org.plinthos.queue.Queue;


/**
 * Provides implementation to place requests in the Queue. This class is
 * singleton.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class RequestPlacer implements Placer {

	private static final Logger log = Logger.getLogger(RequestPlacer.class);

	private static Placer placer;
	private Queue requestQueue;

	private RequestPlacer() {
		requestQueue = QueueFactory.getQueue();
	}


	/**
	 * Places the specified request object in the queue.
	 * 
	 * 
	 * @param request
	 *            object to be placed in the queue
	 * 
	 */
	public synchronized boolean placeRequest(Object request) {

		log.debug("Entering into placeRequest method");
		boolean status = false;
		synchronized (requestQueue) {
			status = requestQueue.enqueue(request);
			if (status) {
				log.info("Placed the request in the queue with id " + ((QueueRequest) request).getRequestId());
				requestQueue.notifyAll();
			}
		}
		log.debug("Leaving from placeRequest method");
		return status;
	}

	/**
	 * Creates the RequestPlacer instance only once
	 * 
	 * 
	 * @return Placer
	 * 
	 */
	public static Placer getInstance() {

		if (placer == null) {
			log.debug("Creating QueueRequest Placer object instance");

			synchronized (RequestPlacer.class) {
				if (placer == null) {
					placer = new RequestPlacer();
				}
			}
			log.debug("QueueRequest Placer object instance is created");
		}

		return placer;
	}
}
