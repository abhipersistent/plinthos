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

import org.apache.log4j.Logger;

public class QueueProcessorThread extends Thread {

	private static final Logger log = Logger.getLogger(QueueProcessorThread.class);

	private QueueProcessor processor;

	
	/**
	 * Constructor RequestProcessor
	 */
	public QueueProcessorThread(QueueProcessor processor) {
		this.setName(this.getClass().getSimpleName() + "@" + this.hashCode());
		this.processor = processor;
	}

	public void run() {

		//TODO: move into external configuration
		final long QUEUE_PROCESSOR_PAUSE = 1 * 1000;
		
		//TODO: add support for service shutdown
		try {
			while( true ) {
				
				try {
					
					int processedRequests = processor.processRequests();
					
					if( processedRequests == 0 ) {
						Thread.sleep(QUEUE_PROCESSOR_PAUSE);
					}
					
					Thread.yield();
				}
				catch(InterruptedException e) {
					
				}
				
			}
		}
		finally {
			log.info("thread is stopped.");
		}
	}

}
