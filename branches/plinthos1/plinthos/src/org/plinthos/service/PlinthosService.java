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
package org.plinthos.service;



import java.util.List;

/**
 * This interface defines the core services offered by the PlinthOS platform.
 * 
 * This interface can be exposed as SOAP based web-service to the clients for
 * sumbiting requests, tracking status and cancelling requests.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public interface PlinthosService {

	/**
	 * Submits a request and returns an request id if the request is accepted.
	 * An incoming request is first validated against the type i.e. there is a
	 * registered task which can handle the request. Then PlinthOS will try to queue up
	 * the request and will return RequestStatus containing request id and status.
	 * 
	 * Callers should inspect RequestStatus ({@link RequestStatus#getStatus()}) for checking result of their operation.
	 * 
	 * @param type - Type of request.
	 * @param userId - the ID of the user who submits the request
	 * @param priority - priority for this request.
	 * @param expiresAfterMinutes - Time in minutes after which the request will expire.
	 * @param xmlData - input for this request in XML format.
	 *  
	 * @return
	 */
	public RequestStatus submitRequest(String requestType, String userId, int priority, int expiresAfterMinutes, String xmlData);
	
	/**
	 * Cancel the specified request.
	 * 
	 * @param requestId
	 * @return
	 */
	public RequestStatus cancelRequest(List<String> requestIds);
	
	/**
	 * Returns status for specified request ids. If there is no matching request found for a request id,
	 * the respective RequestStatus will be marked as "MISSING".
	 * 
	 * @param requestIds
	 * @return
	 */
	public List<RequestStatus> queryStatus(List<String> requestIds);
	
}
