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
package org.plinthos.core.queue.priorityweighted;

import org.plinthos.core.queue.QueueRequest;

public class PriorityWeightedQueueRequest implements QueueRequest {
	
    private int requestId;

    private long expirationTime;
    
    // Size of the request
    private long size;           

    // QueueRequest priority
    private int priority;        

    // Contains the weight of the request
    private double weight;       

    // contains request queue entering time
    private long queueRegistrationTime = 0L;    
    
    // contains request service entering time
    private long serviceBeginningTime = 0L;    

    // contains request service leaveing time
    private long serviceCompletionTime = 0L;    
    
    public PriorityWeightedQueueRequest(int reqId) {
    	requestId = reqId;
    }

    public int getRequestId() {
        return requestId;
    }
	
    
    
    /**
     * This method returns computational time that has been spent in executing 
     * the requested task. 
     *
     * @return the computational time that has been spent in executing the requested task
     *
     */
    public long getServiceTime() {
        if ( serviceCompletionTime > 0 ) {
            return ( serviceCompletionTime - serviceBeginningTime );
        } else {
            return ( System.currentTimeMillis () - serviceBeginningTime );
        }
    }

    /**
     * This method returns the request waiting time in the queue.
     *
     *
     * @return long waiting time
     *
     */
    public long getWaitTime () {
        if ( serviceBeginningTime > 0 ) {
            return ( serviceBeginningTime - queueRegistrationTime );
        } else {
            return ( System.currentTimeMillis() - queueRegistrationTime );
        }
    }
    
    
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	
	
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

    public long getTimeToLive() {
    	return expirationTime - System.currentTimeMillis();
    }

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public long getQueueRegistrationTime() {
		return queueRegistrationTime;
	}

	public void setQueueRegistrationTime(long queueRegistrationTime) {
		this.queueRegistrationTime = queueRegistrationTime;
	}

	public long getServiceBeginningTime() {
		return serviceBeginningTime;
	}

	public void setServiceBeginningTime(long serviceBeginningTime) {
		this.serviceBeginningTime = serviceBeginningTime;
	}

	public long getServiceCompletionTime() {
		return serviceCompletionTime;
	}

	public void setServiceCompletionTime(long serviceCompletionTime) {
		this.serviceCompletionTime = serviceCompletionTime;
	}

    public long getExpirationTime () {
        return expirationTime;
    }
	
	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
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
		if (!(obj instanceof PriorityWeightedQueueRequest))
			return false;
		PriorityWeightedQueueRequest other = (PriorityWeightedQueueRequest) obj;
		if (requestId != other.requestId)
			return false;
		return true;
	}

    
    
}
