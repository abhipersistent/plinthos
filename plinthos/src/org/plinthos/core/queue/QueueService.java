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

import org.plinthos.queue.Queue;

/**
 * Helpful interface for monitoring the queuing system
 *  
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */ 
public interface QueueService extends Queue  {

    int getProcessedRequestsCount();

    int getInProgressRequestsCount();

    int getExpiredRequestsCount();

    void updateQueuedRequests(QueueRequest queueRequest);

    void updateProcessedRequests(QueueRequest queueRequest);


    /**
     * Transitions the request from SUBMITTED state to IN_PROGRESS.
     * Returns true if the transition is successful and false otherwise.
     * 
     * Please note that transition may fail, when we try to retrive a request
     * from the SUBMITTED requests queue which has already been removed from the
     * queue (may be when user cancelled the request).
     * 
     * @param queueRequest
     * @return
     */
    boolean updateInProgressRequests(QueueRequest queueRequest);
    
    /**
     * Removes and returns a request matching requestId if
     * the request is waiting in the queue for its turn.
     * Returns NULL if no matching request found.
     * 
     * @param requestId
     * @return true, if successful; false otherwise
     */
    boolean removeRequest(int requestId);
}
