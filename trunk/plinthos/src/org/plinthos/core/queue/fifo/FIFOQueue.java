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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.plinthos.core.queue.Queue;


public class FIFOQueue implements Queue {

	private BlockingQueue<Object> queue = null;

	private int processedRequestsCount = 0;
	private int runningRequestsCount = 0;
	
	public FIFOQueue(int capacity) {
		queue = new LinkedBlockingQueue<Object>(capacity);
	}

	@Override	
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override	
	public int getCurrentSize() {
		return queue.size();
	}

	@Override	
	public int getCapacity() {
		return queue.size() + queue.remainingCapacity();
	}
	
	@Override
	public Object dequeue() {
		return queue.poll();
	}

	@Override	
	public boolean enqueue(Object item) {
		return queue.offer(item);
	}

	@Override
	public int getExpiredRequestsCount() {
		return 0;
	}

	@Override
	public int getInProgressRequestsCount() {
		return runningRequestsCount;
	}

	@Override
	public int getProcessedRequestsCount() {
		return processedRequestsCount;
	}

	@Override
	public void notifyQueueAboutRequestCompletion(Object r) {
		processedRequestsCount++;
	}

	@Override
	public void notifyQueueAboutRequestStart(Object r) {
		runningRequestsCount++;
	}

}
