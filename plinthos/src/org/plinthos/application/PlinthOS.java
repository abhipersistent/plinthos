package org.plinthos.application;

import org.apache.log4j.Logger;
import org.plinthos.connector.http.HttpServer;
import org.plinthos.core.bootstrap.environment.DefaultPlinthosEnvironmentBuilder;
import org.plinthos.core.bootstrap.environment.PlinthosEnvironment;
import org.plinthos.core.bootstrap.environment.PlinthosEnvironmentHolder;
import org.plinthos.core.bootstrap.initialdata.ConstantsLoader;
import org.plinthos.core.bootstrap.initialdata.RegisteredTasksLoader;
import org.plinthos.core.queue.QueueFactory;
import org.plinthos.core.queue.QueueFactoryProvider;
import org.plinthos.core.queue.QueuePlacer;
import org.plinthos.core.queue.QueuePlacerThread;
import org.plinthos.core.queue.QueueProcessor;
import org.plinthos.core.queue.QueueProcessorThread;
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
		
		if( env.isUseEmbeddedDatabaseServer() ) {
			EmbeddedHSQLDBServer dbServer = EmbeddedHSQLDBServer.getInstance();
			dbServer.start();
		}

		/*
		 * Useful when we use embedded database that is recreated on startup. 
		 */
		loadInitialDataIfMissing();
		
		/*
		 * Create request processing threads.
		 */
		initRequestProcessing();
	
		
		// HTTP Listener/connector
		if( this.env.isUseEmbeddedHttpServer() ) {
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
		
		if( env != null && env.isUseEmbeddedDatabaseServer() ) {
			dbServer.stop();
		}
		
		if( env != null && env.isUseEmbeddedHttpServer() ) {
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
	
}
