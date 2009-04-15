package org.plinthos.core.queue.fifo;

import org.plinthos.core.queue.Queue;
import org.plinthos.core.queue.QueueFactory;
import org.plinthos.core.queue.QueuePlacer;
import org.plinthos.core.queue.QueueProcessor;
import org.plinthos.core.queue.ThreadPoolQueueProcessor;
import org.plinthos.core.service.RequestManager;

public class FIFOQueueFactory implements QueueFactory {

	private static final int DEFAULT_QUEUE_SIZE = 10;
	
	private int queueSize = DEFAULT_QUEUE_SIZE;
	private FIFOQueue queue;
	private QueuePlacer queuePlacer;	
	private QueueProcessor queueProcessor;
	
	public FIFOQueueFactory() {
		queue = new FIFOQueue(queueSize);
		queueProcessor = new ThreadPoolQueueProcessor(queue);
		queuePlacer = new FIFOQueuePlacer(queue);
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
