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
package org.plinthos.core.process;

import org.plinthos.core.PlinthosException;
import org.plinthos.core.model.PlinthosRequest;


/**
 * The core interface to manage requests.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 * @see org.plinthos.core.process.impl.RequestManagerImpl
 */
public interface RequestManager {
	/**
	 * Persists and queues the incoming request.
	 * 
	 * @param type
	 * @param userId
	 * @param priority
	 * @param expiresAfterMinutes
	 * @param xmlData
	 * @return
	 * @throws PlinthosException
	 */
	public PlinthosRequest scheduleRequest(String requestType, String userId, int priority, int expiresAfterMinutes, String xmlData)
			throws PlinthosException;
	
	/**
	 * Returns the PlinthosRequest object matching the requestId.
	 * 
	 * @param requestId
	 * @return
	 */
	public PlinthosRequest getRequest(int requestId);
	
	/**
	 * Updates the status for specified request.
	 * 
	 * @param requestId
	 * @param status
	 */
	public void updateRequestStatus(int requestId, String status);
	
	/**
	 * Cancels the specified request.
	 * @param requestId
	 */
	public void cancelRequest(int requestId) throws PlinthosException;
	
	/**
	 * Calculates and returns ETC for specified request.
	 * @param request
	 * @return
	 */
	public long getETC(PlinthosRequest request);
	
}
