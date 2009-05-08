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
package org.plinthos.shared.gateway.commands.request;

import org.plinthos.shared.gateway.commands.common.Request;

public class FindRequest extends Request {

	private static final long serialVersionUID = -8897924799053177317L;
	
	private String requestId;
	private String correlationId;
	private boolean includeRequestData;
	private boolean includeResponseData;
	
	public FindRequest() {
		
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public boolean isIncludeRequestData() {
		return includeRequestData;
	}

	public void setIncludeRequestData(boolean includeRequestData) {
		this.includeRequestData = includeRequestData;
	}

	public boolean isIncludeResponseData() {
		return includeResponseData;
	}

	public void setIncludeResponseData(boolean includeResponseData) {
		this.includeResponseData = includeResponseData;
	}
	
	
}
