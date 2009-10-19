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

import org.plinthos.core.framework.Constants;
import org.plinthos.core.queue.Queue;
import org.plinthos.core.queue.QueueFactory;
import org.plinthos.core.queue.QueuePlacer;
import org.plinthos.core.queue.QueueProcessor;
import org.plinthos.core.queue.ThreadPoolQueueProcessor;

public class PriorityWeightedQueueFactory implements QueueFactory {

	private int queueSize = Constants.QUEUE_CAPACITY;
	
	private PriorityWeightedQueue queue;
	private QueuePlacer queuePlacer;	
	private QueueProcessor queueProcessor;
	
	public PriorityWeightedQueueFactory() {
		
		queue = new PriorityWeightedQueue(queueSize);
		queueProcessor = new ThreadPoolQueueProcessor(queue);
		queuePlacer = new PriorityWeightedQueuePlacer(queue);
 	}
	
	// @Override
	public Queue getQueue() {
		return queue;
	}

	// @Override
	public QueuePlacer getQueuePlacer() {
		return queuePlacer;
	}

	// @Override
	public QueueProcessor getQueueProcessor() {
		return queueProcessor;
	}


}
