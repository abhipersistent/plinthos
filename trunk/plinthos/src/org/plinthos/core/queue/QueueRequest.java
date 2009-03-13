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



import org.plinthos.util.Constants;


/**
 * This class contains the meta information for the request.
 *
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class QueueRequest {

    public int requestId;

    private long size;           // Size of the request

    private long timeToLive;    // time to live = ExpirationTime - CurrentTime

    private int priority;        // QueueRequest priority

    private double weight;       // Contains the weight of the request

    private long queueRegistrationTime = Constants.ZERO_LONG;    // contains request queue entering time

    private long serviceBeginningTime = Constants.ZERO_LONG;    // contains request service entering time

    private long serviceCompletionTime = Constants.ZERO_LONG;    // contains request service leaveing time

    public QueueRequest(int reqId) {
    	requestId = reqId;
    }


    /**
     * Method getRequestId $description$
     *
     *
     * @return Integer
     *
     */
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
     * This method records the time at which a request was registered with the Queue
     *
     * @param queueEnteringTime $description$
     *
     */
    public void setQueueRegistrationTime ( long time ) {
        queueRegistrationTime = time;
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

    /**
     * @param time refers to the instant of time (measured since the zero epoch in milliseconds)
     *        at which the execution of the requested service begins
     */
    public void setServiceBeginningTime ( long time ) {
        serviceBeginningTime = time;
    }

    /**
     * @param time refers to the instant of time (measured since the zero epoch in milliseconds) 
     *        at which the task for the requested service has been completed 
     */
    public void setServiceCompletionTime ( long time ) {
        serviceCompletionTime = time;
    }

    /**
     * Returns the size of the request
     *
     * @return long request size
     *
     */
    public long getSize () {
        return size;
    }

    /**
     * Sets the size of the request
     *
     * @param size request size
     *
     */
    public void setSize ( long size ) {
        this.size = size;
    }

    /**
     *     This method returns the request time to live in m.seconds . This time will be
     * calculated as follows
     *
     *   TimeToLive = ExpirationTime - CurrentTime.
     *
     * @return long time to live
     *
     */
    public long getTimeToLive () {
        return timeToLive;
    }

    /**
     * Sets the time to live
     *
     *
     * @param timeToLive request time to live
     *
     */
    public void setTimeToLive ( long timeToLive ) {
        this.timeToLive = timeToLive;
    }

    /**
     * Sets the priority for the request
     *
     *
     * @return int request priority
     *
     */
    public int getPriority () {
        return priority;
    }

    /**
     * Sets the request priority
     *
     *
     * @param priority request priority
     *
     */
    public void setPriority ( int priority ) {
        this.priority = priority;
    }

    /**
     * Method getWeight $description$
     *
     *
     * @return double
     *
     */
    public double getWeight () {
        return this.weight;
    }

    /**
     * Method setWeight $description$
     *
     *
     * @param weight $description$
     *
     */
    public void setWeight ( double weight ) {
        this.weight = weight;
    }

    /**
     * Method toString $description$
     *
     *
     * @return String
     *
     */
    long expireTime;

    /**
     * Method getExpirationTime $description$
     *
     *
     * @return long
     *
     */
    public long getExpirationTime () {
        return expireTime;
    }

    /**
     * Method setExpirationTime $description$
     *
     *
     * @param expireTime $description$
     *
     */
    public void setExpirationTime ( long expireTime ) {
        this.expireTime = expireTime;
    }

    /**
     * Method equals $description$
     *
     *
     * @param anotherRequest $description$
     *
     * @return boolean
     *
     */
    public boolean equals ( Object other ) {
    	
        if ( other == null || !( other instanceof QueueRequest ) ) {
            return false; 
        }
        
        //USE THIS ONE CAREFULLY :-)
        if ( !( other instanceof QueueRequest ) ) {
        	if ( (other instanceof Integer) && (requestId == ((Integer) other).intValue() ) ) {
        		return true;
        	} else {
        		return false;
        	}
        }

        QueueRequest qR = (QueueRequest) other;

        if (requestId == qR.getRequestId()) {
        	return true;
        } else {
        	return false;
        }
    }
}
