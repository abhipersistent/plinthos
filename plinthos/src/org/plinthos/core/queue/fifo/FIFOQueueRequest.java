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
package org.plinthos.core.queue.fifo;

import org.plinthos.core.queue.QueueRequest;

public class FIFOQueueRequest implements QueueRequest {

    private int requestId;
    private int priority;
    private long expirationTime;
    private long size;
    

    public FIFOQueueRequest(int reqId) {
    	requestId = reqId;
    }

    public int getRequestId() {
        return requestId;
    }

    public long getTimeToLive() {
    	return expirationTime - System.currentTimeMillis();
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + requestId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FIFOQueueRequest))
			return false;
		FIFOQueueRequest other = (FIFOQueueRequest) obj;
		if (requestId != other.requestId)
			return false;
		return true;
	}

	@Override
	public long getExpirationTime() {
		return expirationTime;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public long getSize() {
		return size;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public void setSize(long size) {
		this.size = size;
	}

    
    
}
