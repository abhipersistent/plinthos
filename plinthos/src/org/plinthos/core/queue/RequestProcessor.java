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
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.PlinthosRequestStatus;
import org.plinthos.core.process.ProcessUtil;
import org.plinthos.core.process.RequestManager;
import org.plinthos.queue.Processor;
import org.plinthos.util.Constants;

/**
 * The <CODE>RequestProcessor</CODE> is responsible for running the perpetual
 * thread that looks for new requests in the queue and assigns them to
 * corresponding workers.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class RequestProcessor extends Thread implements Processor {

	private long keepAliveTime = 20;

	private static Processor processor;

	private ThreadPoolExecutor executor;

	private QueueService requestQueue;

	private static final Logger log = Logger.getLogger(RequestProcessor.class);

	/**
	 * Constructor RequestProcessor
	 */
	public RequestProcessor() {

		this.setDaemon(true);
		this.setName("RequestProcessorMainThread");

		executor = new PlithosThreadPoolExecutor(
				Constants.MIN_THREAD_POOL_SIZE,
				Constants.MAX_THREAD_POOL_SIZE, keepAliveTime,
				TimeUnit.MINUTES, 
				new ArrayBlockingQueue<Runnable>(Constants.MAX_THREAD_POOL_SIZE));

		log.info("Thread Pool is created with " + executor.getMaximumPoolSize()
				+ "  size and minimum size is " + executor.getCorePoolSize());

		requestQueue = (QueueService) QueueFactory.getQueue();
	}

	/**
	 * This method contains the main loop of the PlinthOS infrastructure.
	 */
	public void processRequest() {

		log.debug("Entering into processRequest");
		try {
			while (true) {

				QueueRequest queueRequest = getNextRequest();

				if (queueRequest != null) {

					try {

						createWorker(queueRequest);

					} catch (Throwable t) {
						log.error("Error occured while processing the request", t);
					}
				} else {
					Thread.sleep(5000);
				}
			}
		} catch (InterruptedException exception) {
			log.error("InterruptedException occured while processing the request", exception);
		}
	}

	/**
	 * Method run
	 */
	public void run() {
		log.info("Started the reporting engine processor");
		processRequest();
	}

	/**
	 * Method getNextRequest
	 * 
	 * @return QueueRequest
	 * @throws InterruptedException
	 */
	private QueueRequest getNextRequest() throws InterruptedException {

		log.debug("Entering into getNextRequest method");

		QueueRequest q = null;
//		try {
//            synchronized ( requestQueue ) {            	
				
				if (requestQueue.isEmpty()) {
	
					log.info("Queue is empty, waiting for request");
	
//					requestQueue.wait(5000);
	
				} else {
		            log.debug ( "Leaving from getNextRequest method" );
		            q = (QueueRequest) requestQueue.dequeue();
				}
//			}                        
//		} catch (InterruptedException ex) {
//			log.error("Interrupted Exception in getNextRequest method", ex);
//			throw ex;
//		}
		
		return q;
	}

	/**
	 * Method createWorker
	 * 
	 * @param queueRequest
	 * @throws Exception
	 */
	private void createWorker(QueueRequest queueRequest) throws Exception {

		log.debug("Entering into createWorker method ...");
		log.debug("Maximum number of threads = " + executor.getMaximumPoolSize());

		log.debug("---------- Processing Request ----------");
		log.debug("        ID: " + queueRequest.getRequestId());
		log.debug("Expires On: " + queueRequest.getExpirationTime());
		log.debug("Waited for: " + queueRequest.getWaitTime());
		log.debug("  Priority: " + queueRequest.getPriority());
		log.debug("      Size: " + queueRequest.getSize());
		log.debug("    Weight: " + queueRequest.getWeight());
		log.debug("----------------------------------------");

		log.info("Assigning the request ...");

		// Remove the request from queued requests and add it to
		// processing requests.
		if (requestQueue.updateInProgressRequests(queueRequest)) {

			RequestManager requestManager = ProcessUtil
					.getRequestManager();

			PlinthosRequest request = requestManager
					.getRequest(queueRequest.getRequestId());

			requestManager.updateRequestStatus(queueRequest
					.getRequestId(), PlinthosRequestStatus.IN_PROGRESS);

			// Command task = new DefaultCommandImpl();
			Command task = CommandFactory.getCommand(request.getTask());

			task.setRequest(queueRequest);
	
			executor.execute(task);
		}

		log.debug("Leaving from createWorker method");
	}

	/**
	 * Creates the RequestProcessor instance only once
	 * 
	 * @return Processor
	 */
	public static Processor getInstance() {

		if (processor == null) {

			log.debug("Creating QueueRequest Processor object instance");

			synchronized (RequestProcessor.class) {
				if (processor == null) {
					processor = new RequestProcessor();
				}
			}
		}
		return processor;
	}

	/**
	 * @return the keepAliveTime
	 */
	public long getKeepAliveTime() {
		return keepAliveTime;
	}

	/**
	 * @param keepAliveTime
	 *            the keepAliveTime to set
	 */
	public void setKeepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}
}
