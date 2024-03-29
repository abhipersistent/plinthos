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
package org.plinthos.application;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.plinthos.connector.http.HttpServer;
import org.plinthos.core.bootstrap.environment.DefaultPlinthosEnvironmentBuilder;
import org.plinthos.core.bootstrap.environment.PlinthosEnvironment;
import org.plinthos.core.bootstrap.environment.PlinthosEnvironmentHolder;
import org.plinthos.core.bootstrap.initialdata.ConstantsLoader;
import org.plinthos.core.bootstrap.initialdata.RegisteredTasksLoader;
import org.plinthos.core.framework.Constants;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.PlinthosRequestStatus;
import org.plinthos.core.model.RegisteredTask;
import org.plinthos.core.model.SystemConfigurationProperty;
import org.plinthos.core.queue.QueueFactory;
import org.plinthos.core.queue.QueueFactoryProvider;
import org.plinthos.core.queue.QueuePlacer;
import org.plinthos.core.queue.QueuePlacerThread;
import org.plinthos.core.queue.QueueProcessor;
import org.plinthos.core.queue.QueueProcessorThread;
import org.plinthos.core.service.ConstantsManager;
import org.plinthos.core.service.RequestManager;
import org.plinthos.core.service.ServiceFactory;
import org.plinthos.core.service.TaskRegistry;
import org.plinthos.core.taskinvoker.BackgroundTaskInvoker;
import org.plinthos.database.EmbeddedHSQLDBServer;

public class PlinthOS {

	private static final Logger log = Logger.getLogger(PlinthOS.class);
	
	private PlinthosEnvironment env = null;
	
	private HttpServer httpServer = null;
	
	private EmbeddedHSQLDBServer dbServer = null;
	
	public PlinthOS() {
		this.env = new DefaultPlinthosEnvironmentBuilder().loadEnvironment();
	}
	
	public PlinthOS(PlinthosEnvironment env) {
		this.env = env;
	}
	
	public void start() {
		
		if( env.getUseEmbeddedDatabaseServer() ) {
			dbServer = EmbeddedHSQLDBServer.getInstance();
			dbServer.start();
		}

		/*
		 * Useful when we use embedded database that is recreated on startup. 
		 */
		loadInitialDataIfMissing();
		
		/*
		 * Start the back ground tasks;
		 */
		BackgroundTaskInvoker.getInstance().startBackgroundTasks();
		
		/*
		 * Process incomplete requests as per task type configuration   
		 */
		processIncompleteRequests();
		
		/*
		 * Create request processing threads.
		 */
		initRequestProcessing();
	
		
		// HTTP Listener/connector
		if( this.env.getUseEmbeddedHttpServer() ) {
			int httpPort = getHttpServerPort();
			httpServer = new HttpServer();
			httpServer.setPort(httpPort);
			httpServer.start();
		}
	}

	private void loadInitialDataIfMissing() {
		/* 
		 * Load initial configuration if available. Only properties that exist in
		 * file and do not exist in the database will be added.  
		 */
		ConstantsLoader initialConfigLoader = new ConstantsLoader();
		initialConfigLoader.run();
		
		/*
		 * Load task information. It will not override any existing data.
		 * Only tasks that exist in file and do not exist in the database will be added.
		 */
		RegisteredTasksLoader tasksLoader = new RegisteredTasksLoader();
		tasksLoader.run();
	}

	private void initRequestProcessing() {
		// Queue placer and processor
		QueueFactory queueFactory = new QueueFactoryProvider().getQueueFactory();

		/*
		 * Starting Queue Processor thread.
		 * 
		 * This thread picks up requests from the queue and dispatches
		 * them to one of the available worker threads for processing.
		 */
		QueueProcessor queueProcessor = queueFactory.getQueueProcessor();
		QueueProcessorThread queueProcessorThread = new QueueProcessorThread( queueProcessor );
		queueProcessorThread.start();
		log.info("request queue processor is running");		

		/*
		 *  Starting Queue Placer thread. 
		 *  
		 *  This thread reads unprocessed requests from request table
		 *  and places them in queue for processing.
		 */
		QueuePlacer queuePlacer = queueFactory.getQueuePlacer();
		QueuePlacerThread queuePlacerThread = new QueuePlacerThread( queuePlacer );
		queuePlacerThread.start();
		
		log.info("request queue placer is running");
	}
	
	public void shutdown() {
		log.info("sutting down...");
		
		/*
		 * Stop the back ground tasks;
		 */
		BackgroundTaskInvoker.getInstance().stopBackgroundTasks();
		
		if( env != null && env.getUseEmbeddedDatabaseServer() ) {
			dbServer.stop();
		}
		
		if( env != null && env.getUseEmbeddedHttpServer() ) {
			httpServer.shutdown();
		}
	}
	
	private int getHttpServerPort() {

		int port = -1;
		
		PlinthosEnvironment plinthosConfig = 
    		PlinthosEnvironmentHolder.getInstance().getConfig();
    	
    	if( plinthosConfig == null ) {
    		String msg = "Plinthos configuration is not available - can't create embedded HTTP Server.";
    		log.error(msg);
    		throw new RuntimeException(msg);
    	}
		
    	port = plinthosConfig.getHttpPort();
    	
    	if( port <= 0 ) {
    		String msg = "HTTP Server port is not configured.";
    		log.error(msg);
    		throw new RuntimeException(msg);
    	}
		
    	return port;
	}

	private void processIncompleteRequests(){

    	TaskRegistry taskRegistry = ServiceFactory.getInstance().getTaskRegistry();
    	ConstantsManager constantsManager =	ServiceFactory.getInstance().getConstantsManager();
    	RequestManager requestManager = ServiceFactory.getInstance().getRequestManager();
    	
    	String propertyName;
    	SystemConfigurationProperty property;
    	List<String> reSubmitTaskTypes = new ArrayList<String>();
    	
    	for (RegisteredTask registeredTask : taskRegistry.findAll()) {
    		propertyName = registeredTask.getTaskType()
					+ Constants.TASK_RESUBMIT_ON_PLINTHOS_RESTART_SUFFIX;
			property = constantsManager.findSystemConfiguration(propertyName);
			
			if (property != null && "TRUE".equalsIgnoreCase(property.getValue())) {
				reSubmitTaskTypes.add(registeredTask.getTaskType());
			}
		}

        /*
         * Change request status to SUBMITTED/FAILED as per configuration. 
         */    	
        log.info("Start Changing RUNNING task status to  SUBMITTED/FAIL on plinthos restart.");
        
        List<PlinthosRequest> reportRequests = requestManager.
        	findRequestsByStatus(PlinthosRequestStatus.IN_PROGRESS);
		for (PlinthosRequest request : reportRequests) {
			if (reSubmitTaskTypes.contains(request.getTaskInfo().getTaskType())) {
				requestManager.updateRequestStatus(request.getId(),
						PlinthosRequestStatus.SUBMITTED, "PlinthOS Restarted");
				log.info("Change status to " + PlinthosRequestStatus.SUBMITTED
						+ " for task id:" + request.getId() + " of task type:"
						+ request.getTaskInfo().getTaskType());
			} else {
				requestManager.updateRequestStatus(request.getId(),
						PlinthosRequestStatus.FAILED, "PlinthOS Restarted");
				log.info("Change status to " + PlinthosRequestStatus.FAILED
						+ " for task id:" + request.getId() + " of task type:"
						+ request.getTaskInfo().getTaskType());
			}
		}

	    log.info("End Changing RUNNING task status to  SUBMITTED/FAIL on plinthos restart.");

    }	
}
