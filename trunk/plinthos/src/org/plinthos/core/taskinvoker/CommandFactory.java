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

import org.plinthos.core.model.RegisteredTask;
import org.plinthos.core.queue.QueueProcessor;
import org.plinthos.core.service.RequestManager;

/**
 * @author kkirdat
 *
 */
public class CommandFactory {
	
	private RequestManager requestMgr;
	private QueueProcessor queueProcessor;
	
	public CommandFactory() {
		
	}

	
	
	public RequestManager getRequestMgr() {
		return requestMgr;
	}



	public void setRequestMgr(RequestManager requestMgr) {
		this.requestMgr = requestMgr;
	}



	public QueueProcessor getQueueProcessor() {
		return queueProcessor;
	}



	public void setQueueProcessor(QueueProcessor queueProcessor) {
		this.queueProcessor = queueProcessor;
	}



	public Command getCommand(RegisteredTask task) {
		DefaultCommandImpl cmd = new DefaultCommandImpl();
		cmd.setQueueProcessor(queueProcessor);
		cmd.setRequestManager(requestMgr);
		return cmd;	
	}
}
