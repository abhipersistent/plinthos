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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.plinthos.core.framework.Constants;
import org.plinthos.core.queue.Queue;
import org.plinthos.core.queue.QueueRequest;


public class FIFOQueue implements Queue {

	private static final Logger logger = Logger.getLogger(FIFOQueue.class);
	
	private int maxProcessedRequests = 1000;
	
	private List<FIFOQueueRequest> expiredRequests = 
		new LinkedList<FIFOQueueRequest>();
	private LinkedList<FIFOQueueRequest> processedRequests = 
		new LinkedList<FIFOQueueRequest>();
	private List<FIFOQueueRequest> inProgressRequests = 
		new LinkedList<FIFOQueueRequest>();

	private BlockingQueue<FIFOQueueRequest> queue = null;
	
	
	public FIFOQueue(int capacity) {
		queue = new LinkedBlockingQueue<FIFOQueueRequest>(capacity);
	}

	// @Override	
	public synchronized boolean isEmpty() {
		return queue.isEmpty();
	}

	// @Override	
	public synchronized int getCurrentSize() {
		return queue.size();
	}

	// @Override	
	public synchronized int getCapacity() {
		return queue.size() + queue.remainingCapacity();
	}
	
	// @Override
	public synchronized Object dequeue() {

		FIFOQueueRequest qR = null;

		while( qR == null ) {
			qR = queue.poll();

			if( qR == null ) {
				// there are no requests in the queue.
				break;
			}
			
			if( qR.getTimeToLive() < Constants.EXPIRE_TIME_FACTOR ) {
				expiredRequests.add(qR);
				logger.info("Request expired: " + qR);
				qR = null; // read next request;
			}
		}
		
		return qR;
	}

	// @Override	
	public synchronized boolean enqueue(Object item) {
		FIFOQueueRequest qR = (FIFOQueueRequest)item;
		// returns false if there is no more space in the queue
		return queue.offer(qR);
	}

	// @Override
	public synchronized int getExpiredRequestsCount() {
		return expiredRequests.size();
	}

	// @Override
	public synchronized int getInProgressRequestsCount() {
		return inProgressRequests.size();
	}

	// @Override
	public synchronized int getProcessedRequestsCount() {
		return processedRequests.size();
	}

	// @Override
	public  synchronized void notifyQueueAboutRequestCompletion(Object r) {
		FIFOQueueRequest qR = (FIFOQueueRequest)r;
		inProgressRequests.remove(r);
		// remove first element in the list if we've reached the capacity
		if( processedRequests.size() >= maxProcessedRequests ) {
			processedRequests.remove();
		}
		processedRequests.add(qR);
	}

	// @Override
	public synchronized void notifyQueueAboutRequestStart(Object r) {
		FIFOQueueRequest qR = (FIFOQueueRequest)r;
		inProgressRequests.add(qR);
		// dequeue method has already removed the request but just for completeness 
		// attempt to remove it here again.
		queue.remove(qR);
	}

	@Override
	public synchronized List<? extends QueueRequest> getExpiredRequests() {
		return new ArrayList<FIFOQueueRequest>(expiredRequests);		
	}

	@Override
	public synchronized List<? extends QueueRequest> getInProgressRequests() {
		return new ArrayList<FIFOQueueRequest>(inProgressRequests);		
	}

	@Override
	public synchronized List<? extends QueueRequest> getProcessedRequests() {
		return new ArrayList<FIFOQueueRequest>(processedRequests);
	}

	@Override
	public synchronized boolean remove(QueueRequest queueRequest) {
		return expiredRequests.remove(queueRequest) || queue.remove(queueRequest);
	}

}
