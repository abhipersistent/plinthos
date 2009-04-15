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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.plinthos.core.framework.Constants;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.service.RequestManager;
import org.plinthos.core.service.ServiceFactory;
import org.plinthos.core.taskinvoker.Command;
import org.plinthos.core.taskinvoker.CommandFactory;

/**
 * The <CODE>RequestProcessor</CODE> is responsible for running the perpetual
 * thread that looks for new requests in the queue and assigns them to
 * corresponding workers.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class ThreadPoolQueueProcessor implements QueueProcessor {

	private ThreadPoolExecutor executor;

	private Queue requestQueue;

	private RequestManager requestManager;
	
	private CommandFactory cmdFactory;
	
	private static final Logger log = Logger.getLogger(ThreadPoolQueueProcessor.class);

	/**
	 * Constructor RequestProcessor
	 */
	public ThreadPoolQueueProcessor(Queue q) {

		requestManager = ServiceFactory.getInstance().getRequestManager();
		
		//TODO: externalize for configuration
		long keepAliveTime = 20;
		
		executor = new PlithosThreadPoolExecutor(
				Constants.MIN_THREAD_POOL_SIZE,
				Constants.MAX_THREAD_POOL_SIZE, 
				keepAliveTime,
				TimeUnit.SECONDS, 
				new ArrayBlockingQueue<Runnable>(Constants.MAX_THREAD_POOL_SIZE));

		log.info("Thread Pool is created with " + executor.getMaximumPoolSize()
				+ "  size and minimum size is " + executor.getCorePoolSize());

		requestQueue = q;
		
		cmdFactory = new CommandFactory();
		cmdFactory.setQueueProcessor(this);
		cmdFactory.setRequestMgr(requestManager);
	}

	/**
	 * This method contains the main loop of the PlinthOS infrastructure.
	 */
	public int processRequests() {

		int nRequests = 0;
		while (true) {

			if( executor.getActiveCount() == Constants.MAX_THREAD_POOL_SIZE ) {
				// all threads are busy.
				break;
			}
			
			QueueRequest queueRequest = getNextRequest();
			
			if( queueRequest == null ) {
				break;
			}
			
			nRequests++;
			
			try {
				createWorker(queueRequest);
			} catch (Throwable t) {
				log.error("Error occured while processing the request:" + 
						queueRequest.getRequestId(), t);
			}
		}
		
		return nRequests;
	}

	/**
	 * Method getNextRequest
	 * 
	 * @return QueueRequest
	 * @throws InterruptedException
	 */
	private QueueRequest getNextRequest() {

		log.debug("Entering into getNextRequest method");

		QueueRequest q = null;
		synchronized ( requestQueue ) {            	
			
			if (requestQueue.isEmpty()) {
				log.debug("Queue is empty, waiting for request");
			} else {
				log.info("processing request");
	            // log.debug ( "Leaving from getNextRequest method" );
	            q = (QueueRequest) requestQueue.dequeue();
	            
			}
		}                        
		return q;
	}

	/**
	 * Method createWorker
	 * 
	 * @param queueRequest
	 * @throws Exception
	 */
	private void createWorker(QueueRequest queueRequest) throws Exception {

		log.debug("---------- Processing Request ----------");
		log.debug(queueRequest.toString());
		log.debug("----------------------------------------");

		// Remove the request from queued requests and add it to processing requests.
		requestQueue.notifyQueueAboutRequestStart(queueRequest);

		RequestManager requestManager = getRequestManager();

		PlinthosRequest request = requestManager.getRequest(queueRequest.getRequestId());

		Command task = cmdFactory.getCommand(request.getTaskInfo());
		task.setRequest(queueRequest);

		executor.execute(task);

		log.debug("Leaving from createWorker method");
	}

//	void executeRequest(QueueRequest r) {
//		// Remove the request from queued requests and add it to processing requests.
//		PlinthosRequest request = requestManager.getRequest(r.getRequestId());
//		Command task = CommandFactory.getCommand(request.getTaskInfo());
//		task.setRequest(r);
//		executor.execute(task);
//	}
	
	protected RequestManager getRequestManager() {
		return requestManager;
	}

	// @Override
	public void onRequestComplete(QueueRequest qR) {
		requestQueue.notifyQueueAboutRequestCompletion(qR);
	}

	// @Override
	public void onRequestStart(QueueRequest qR) {
		requestQueue.notifyQueueAboutRequestStart(qR);
	}
}
