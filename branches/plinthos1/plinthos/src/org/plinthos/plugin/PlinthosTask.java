/*
 * PlinthOS, Home of Open Source Multi-Core Computing.
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
package org.plinthos.plugin;

/**
 * An abstraction of independent jobs that may be executed using PlinthOS platform. 
 * This interface declares notion of PlinthosTask.  
 * 
 * 
 * PlinthOS platform can accept requests which may be mapped to web-services
 * implementing this interface.
 * 
 * In order to develop plug-able services that can be deployed on PlinthOS platform,
 * you need to implement web-services providing this API.
 * 
 * While developing independent services only {@link PlinthosTask#execute(String, String)} is mandatory.
 * {@link PlinthosTask#cancelRequest(String)} and {@link PlinthosTask#getTimeToComplete(String, String)} are required
 * to be implemented if you wish to provide functionality such as cancellation of requests and query for
 * ETC (Estimated Time to Complete)
 * 
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public abstract class PlinthosTask {

	/**
	 * Executes the job and returns the status.
	 * The information needed for completing the task may be passed in any format (such as XML)
	 * as an argument (xmlData) to execute method. Implementors have the responsibility of defining
	 * the contract (implementation specific format and semantics) while developing the pluggable
	 * services. 
	 * 
	 * Status may be COMPLETED, FAILED, CANCELLED or EXPIRED.
	 * @param requestId - QueueRequest Id used by PlinthOS platform for status tracking.
	 * @return
	 */
	public String execute(String requestId, String xmlData) {
		//DEFAULT
		return "Executed PlinthosTask";
	}
	
	/**
	 * Cancels the specified request.
	 * 
	 * @param requestId
	 */
	public void cancelRequest(String requestId) {
		//EMPTY
	}
	
	/**
	 * Returns estimated time to complete in seconds. 
	 * @param requestId
	 * @param xmlData
	 * @return
	 */
	public long getTimeToComplete(String requestId, String xmlData) {
		return -1;
	}
	
}
