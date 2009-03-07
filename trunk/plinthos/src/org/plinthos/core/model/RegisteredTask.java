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
package org.plinthos.core.model;

import java.io.Serializable;


/**
 * An abstraction of a registered task.
 * 
 * This entity allows configuration of independent tasks deployed on the PlinthOS platform.
 * 
 * When PlinthOS application receives a request, it looks up for registered task in its repository
 * matching the client supplied request type.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 * @see org.plinthos.core.model.impl.RegisteredTaskImpl
 */
public interface RegisteredTask  extends Serializable {

	/**
	 * Returns task type.
	 * @return
	 */
	public String getTaskType();

	/**
	 * Sets the task type.
	 * @param taskType
	 */
	public void setTaskType(String taskType);

	public void setExecutorClass(String exClass);
	
	public String getExecutorClass();

	/**
	 * Returns true if this task supports calculation of ETC.
	 * @return
	 */
	public boolean isEtcSupported();
	
	/**
	 * Setter for etcSupported.
	 * @param etcSupported
	 */
	public void setEtcSupported(boolean etcSupported);
}
