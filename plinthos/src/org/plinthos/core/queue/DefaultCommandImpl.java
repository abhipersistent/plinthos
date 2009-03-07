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

import java.lang.reflect.Method;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.PlinthosRequestStatus;
import org.plinthos.core.model.RegisteredTask;
import org.plinthos.core.process.ProcessUtil;
import org.plinthos.core.process.RequestManager;
import org.plinthos.util.PlinthosUtil;


/**
 * Default implementation for Command.
 * It invokes SOAP based web-service using XFire.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 * @see org.plinthos.core.queue.Command
 */
public class DefaultCommandImpl implements Command {

	private static final Logger log = Logger.getLogger(DefaultCommandImpl.class);

	private QueueRequest queueRequest;
	
	/* (non-Javadoc)
	 * @see org.plinthos.core.queue.Task#process()
	 */
	public void execute() {
		log.debug("Entering the process method");
		
		invokeService();
        
		QueueService requestQueue = (QueueService) QueueFactory.getQueue();
        
        //Update the processed queueRequest
        requestQueue.updateProcessedRequests(queueRequest);
        
        log.debug("Leaving from process method");		
	}

	private void invokeService() {
		
		RequestManager requestMgr = ProcessUtil.getRequestManager();
		
		PlinthosRequest plinthosRequest = requestMgr.getRequest(queueRequest.getRequestId());

		RegisteredTask task = plinthosRequest.getTask();

	    try { 
	    	String executorClassName = task.getExecutorClass();

	    	URLClassLoader classLoader = URLClassLoader.newInstance(PlinthosUtil.getUrls(executorClassName));

//	    	log.info(Arrays.toString(classLoader.getURLs()));
	    	
	    	Class<?> c = classLoader.loadClass(executorClassName);
	    	Method m = c.getMethod("execute", String.class, String.class);

	    	Object obj = c.newInstance(); 

	    	String status = (String) m.invoke(obj, String.valueOf(queueRequest.getRequestId()), plinthosRequest.getRequestParams());
			
	    	log.info(" Queue Request         : " + queueRequest.getRequestId());
	    	log.info(" Completed with status : " + status);
	    	
	    	requestMgr.updateRequestStatus(queueRequest.getRequestId(), status);
	    	
	    } catch (Exception eX) {
	    	
	    	log.error("Unable to invoke service: " + task.getTaskType());
	    	log.error("Exception:\n" + eX.getMessage());
	    	eX.printStackTrace();
	    	
	    	requestMgr.updateRequestStatus(queueRequest.getRequestId(), PlinthosRequestStatus.FAILED);
	    }
	}


	/* (non-Javadoc)
	 * @see org.plinthos.core.queue.Task#setRequest(org.plinthos.core.queue.QueueRequest)
	 */
	public void setRequest(QueueRequest queueRequest) {
		this.queueRequest = queueRequest;
	}

	public void run() {
		execute();
	}	
}
