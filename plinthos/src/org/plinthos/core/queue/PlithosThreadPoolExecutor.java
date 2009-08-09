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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * The <CODE>PlithosThreadPoolExecutor</CODE> extends {@link ThreadPoolExecutor} to allow
 * {@link RequestProcessor } thread to wait when the pool has no capacity to accept
 * new requests.
 * 
 *
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class PlithosThreadPoolExecutor extends ThreadPoolExecutor {
	
	private static final Logger log = Logger.getLogger ( PlithosThreadPoolExecutor.class );
	
	/**
     * The queue used for holding tasks while they are being processed.
     * The capacity of this queue to set to max pool size of this executor
     * so that thread calling execute(Runnable) can wait when the capacity
     * is full.  
     * 
     */
	private BlockingQueue<Runnable> tasksQueue;
	
	/**
	 * The constructor.
	 * 
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 */
	public PlithosThreadPoolExecutor(int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue) {
		
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		
		// Initialize the queue to set capacity equal to max pool size.
		tasksQueue = new ArrayBlockingQueue<Runnable>(maximumPoolSize);
	}

	/**
	 * 
	 * This method overrides behavior of the superclass. The superclass execute method
	 * rejects the command when it reaches its capacity.
	 * 
	 * The overridden method doesn't reject the command but makes the caller thread to wait until pool
	 * worker is available for execution.
	 * 
	 * @param command
	 */
	public void execute(Runnable command) {

		// Check if the queue has free space to execute this command without reaching its capacity.
		if ( ! tasksQueue.offer(command) ) {
			log.info("*** Pool doesn't have free worker to handle the request ***");
			log.info("Waiting for the worker ...");
			try {
				tasksQueue.put(command);
			} catch (InterruptedException e) {
				log.error("InterruptedException occured while waiting for pooled thread", e);
			}
			log.info("Got available worker to handle the request" );
		}
		// Call the super-class execute.
		super.execute(command);
		
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
	 */
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		// The task has been completed. So remove one element from the queue
		// to make room for other tasks. It will automatically signal other thread waiting for
		// space to become available.
		tasksQueue.remove();
	}
}
