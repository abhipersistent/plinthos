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

/**
 * A queue is a container that stores a list of items. Items can be inserted and
 * removed from the queue based on the <b> Priority Weighted Queue Algorithm <b>
 * Each time while taking the item in the queue, the priority weighting queue
 * algorithm will exceuted and based on the weight , the items will be
 * processed. <b> <center> Priority Weighted Queue Algorithm <br/> Babis
 * Marmanis </center></b>
 * 
 * When a job \uFB01nishes,and the worker responds that is ready to receive a
 * new request,we go in the queue and select a new request as follows:
 * 
 * <ol>
 * <li> We evaluate the T l for each request if T l <0 then we subtract the re-
 * quest from the queue according to whatever business logic is desirable.
 * <li> We evaluate the weight w i for the i-th request as the sum of three
 * contributions:
 * <ul>
 * <li> The priority contribution given as \u03B1p i
 * <li> The time to live contribution given as \u03B2 (T l )i
 * <li> The size contribution given as \u03B3 (S b )i
 * </ul>
 * the coe\uFB03cients \u03B1 ,\u03B2 ,and \u03B3 should be con \uFB01gurable
 * and accessible on real-time.These coe \uFB03cients will be manipulated by the
 * system as well.
 * <li> Based on the weight we order the requests in the queue and select the
 * request with the highest weight.
 * <li>We reevaluate \u03BB and �based on the new data and solve for a phase
 * space that is ten times larger in dof than the current state cardinality. If
 * the probability to be in the same state is larger than the value that is
 * determined in a con \uFB01gurable state weight list then we will make \u03B3
 * negative,in which case the smaller the job the higher its weight. We can also
 * increase the priority of small size requests based on weighted or uniform
 * distribution.Typically the simulation should run for as long as E [s ].
 * 
 * </ol>
 * 
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 * @stereotype control
 */
public interface Queue {

	/**
	 * Returns boolean value whether the queue is empty or not
	 * 
	 * 
	 * @return boolean queue empty status
	 * 
	 */
	boolean isEmpty();

	/**
	 * Return the count of the requests, which are in the queue.
	 * 
	 * 
	 * @return int waiting request count in the queue
	 * 
	 */
	int getCurrentSize();

	/**
	 * Returns the maximum number request can be placed in the queue
	 * 
	 * 
	 * @return int maximum number of requests can be placed in the queue.
	 * 
	 */
	int getCapacity();

	/**
	 * Remove and return an item from the queue. The item that is returned is
	 * the one with the highest weight.
	 * 
	 * W(r) = ALPHA * P(r) + BETA * Tl(r) + GAMMA * S(r)
	 * 
	 * 
	 * W = Weight P = Priority Tl = Time To Live S = Size
	 * 
	 * 
	 * @return Object Object with the highest weight.
	 * 
	 */
	Object dequeue();

	/**
	 * Adds the element to queue and returns true if the item is added to queue
	 * otherwise false.
	 * 
	 * 
	 * @param item
	 *            element
	 * 
	 * @return boolean item status
	 * 
	 */
	boolean enqueue(Object item);

	/* ------------------------------------------- */
	

	void notifyQueueAboutRequestCompletion(Object r);
	
	void notifyQueueAboutRequestStart(Object r);
	
    int getProcessedRequestsCount();

    int getInProgressRequestsCount();

    int getExpiredRequestsCount();

}
