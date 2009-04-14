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
package org.plinthos.core.taskinvoker;

import org.apache.log4j.Logger;
import org.plinthos.core.framework.ExceptionUtil;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.PlinthosRequestStatus;
import org.plinthos.core.model.RegisteredTask;
import org.plinthos.core.queue.QueueProcessor;
import org.plinthos.core.queue.QueueRequest;
import org.plinthos.core.service.RequestManager;
import org.plinthos.shared.plugin.PlinthosTask;
import org.plinthos.shared.plugin.PlinthosTaskContext;
import org.plinthos.shared.plugin.PlinthosTaskStatus;


/**
 * Default implementation for Command.
 * It invokes SOAP based web-service using XFire.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 * @see org.plinthos.core.taskinvoker.Command
 */
public class DefaultCommandImpl implements Command {

	private static final Logger log = Logger.getLogger(DefaultCommandImpl.class);

	private RequestManager requestMgr;
	private QueueProcessor queueProcessor;
	
	private int requestId;
	private QueueRequest queueRequest;
	
	public void setQueueProcessor(QueueProcessor queueProcessor) {
		this.queueProcessor = queueProcessor;
	}
	
	public void setRequestManager(RequestManager r) {
		this.requestMgr = r;
	}
	
	/* (non-Javadoc)
	 * @see org.plinthos.core.queue.Task#process()
	 */
	public void execute() {
		log.debug("Entering the process method");

		
		queueProcessor.onRequestStart(queueRequest);

		invokeService();
        
        //Update the processed queueRequest
		queueProcessor.onRequestComplete(queueRequest);
	}

	private void invokeService() {
		
    	requestMgr.updateRequestStatus(requestId, PlinthosRequestStatus.IN_PROGRESS, null);
    	
    	PlinthosRequest plinthosRequest = requestMgr.getRequest(requestId);

		RegisteredTask task = plinthosRequest.getTaskInfo();

    	String executorClassName = task.getExecutorClass();
    	
    	
		String status = null;
	    try { 
	    	PlinthosTaskContextImpl ctx = new PlinthosTaskContextImpl();
	    	ctx.setRequestManager(requestMgr);
	    	ctx.setRequestId(requestId);

    		status = loadAndExecuteTask( executorClassName, ctx,
    				String.valueOf(requestId), plinthosRequest.getRequestParams());
	    	
	    	log.info(" Queue Request         : " + queueRequest.getRequestId());
	    	log.info(" Completed with status : " + status);
	    	
	    	String requestStatus = toRequestCompletionStatus(status);
	    	requestMgr.updateRequestStatus(requestId, requestStatus, ctx.getStatusMessage());
	    	
	    } 
	    catch (Throwable t) {
	    	
	    	log.error("Unable to invoke service: " + task.getTaskType() + ". ", t);
	    	String statusMessage = ExceptionUtil.getCompactStackTrace(t);
	    	requestMgr.updateRequestStatus(requestId, PlinthosRequestStatus.FAILED, statusMessage);
	    }
	}


	private String loadAndExecuteTask(String taskClassName, 
			PlinthosTaskContext ctx,
			String requestId, String requestData) 
		throws Exception {
		
		ClassLoader originalContextClassLoader = 
			Thread.currentThread().getContextClassLoader();

		String status = null;
		
		TaskClassLoaderFactory tclFactory = TaskClassLoaderFactory.getInstance();
		
		try {
			/*
			 * Task implementation should be taking it into account. For example
			 * if it relies on XStream library to parse request data it should 
			 * configure XStream to use task classloader. Without it XStream will
			 * only be using PlinthOS classloader that is not aware of any 
			 * classes related to task implementation.
			 */
			ClassLoader taskClassLoader = tclFactory.getClassLoaderForTask(taskClassName);
			/*
			 * Some of the code that task is using may rely on context class loader instead 
			 * of class loader of the class.
			 */
			Thread.currentThread().setContextClassLoader(taskClassLoader);		
		
			@SuppressWarnings("unchecked")
	    	Class<? extends PlinthosTask> taskClass = 
	    		(Class<? extends PlinthosTask>) taskClassLoader.loadClass(taskClassName);
			PlinthosTask taskImpl = null;

			taskImpl = (PlinthosTask)taskClass.newInstance();
	    	taskImpl.setContext(ctx);
	    	status = taskImpl.execute( requestId, requestData ); 
		}
	    catch( Throwable t) {
	    	status = PlinthosTaskStatus.FAILED;
	    	log.error("Failed to execute task: requestId=" + requestId + ". ", t);
	    	String statusMessage = ExceptionUtil.getCompactStackTrace(t);
	    	ctx.setStatusMessage(statusMessage);
	    }
		finally {
			// restore original context class loader for this thread
			Thread.currentThread().setContextClassLoader(originalContextClassLoader);
		}
		
		return status;
	}
	
	/* (non-Javadoc)
	 * @see org.plinthos.core.queue.Task#setRequest(org.plinthos.core.queue.QueueRequest)
	 */
	public void setRequest(QueueRequest qR) {
		this.queueRequest = qR;
		this.requestId = qR.getRequestId();
	}

	public void run() {
		execute();
	}
	
    
    private String toRequestCompletionStatus(String taskStatus) {
    	if( PlinthosTaskStatus.CANCELLED.equals(taskStatus) ) {
    		return PlinthosRequestStatus.CANCELED;
    	}
    	else if( PlinthosTaskStatus.FAILED.equals(taskStatus) ) {
    		return PlinthosRequestStatus.FAILED;
    	}
    	else {
    		return PlinthosRequestStatus.COMPLETED;
    	}
    }
    
	
}
