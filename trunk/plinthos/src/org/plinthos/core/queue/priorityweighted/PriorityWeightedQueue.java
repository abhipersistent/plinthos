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

// JAVA 2 API
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.plinthos.core.framework.Constants;
import org.plinthos.core.queue.Queue;
import org.plinthos.core.queue.QueueRequest;



/**
 * This class provides the implementation for <b> Priority Weighted Queue
 * Algorithm <b>. The item can be placed in the queue,if the item finds the
 * space in the queue. Items are removed from the queue based on the <b>
 * Priority Weighted Queue Algorithm <b>. Each time a task has been completed
 * the priority weighting queue algorithm will be executed and based on
 * the calculated weights , the next items for execution will be selected 
 * and removed from the queue. 
 * 
 * In particular, when a job finishes,and the worker responds that is ready 
 * to handle a new request, we select a new request to be serviced as follows:
 * 
 * <ol>
 * <li> We evaluate the Time-to-Live (TL) for each request. If (TL < 0) then 
 * we subtract the request from the queue according to whatever business logic 
 * is desirable.</li>
 * 
 * <li> We evaluate the weight Wi for the i-th request as the sum of three
 * contributions:
 * <ul>
 * <li> The priority contribution given as Pi</li>
 * <li> The time to live contribution given as TLi</li>
 * <li> The size contribution given as Si</li>
 * </ul>
 * the coefficients alpha, beta, and gamma are configurable and accessible on real-time.
 * These coefficients are manipulated by the system as well.</li>
 * 
 * <li> Based on the weights, we order the requests in the queue and select the
 * request with the highest weight.</li>
 * 
 * <li>We reevaluate based on the new data and solve for a phase space that is ten times 
 * larger in degrees of freedom (dof) than the current state cardinality. 
 * If the probability to be in the same state is larger than the value that is determined 
 * in a configurable state weight list then we will make gamma negative, which implies that 
 * the smaller the job is the higher its weight will be.</li> 
 * 
 * We can also increase the priority of small size requests based on a weighted or uniform
 * distribution. Typically the simulation should run for as long as E [s].</li>
 * </ol>
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class PriorityWeightedQueue implements Queue {

	private static final Logger log = Logger.getLogger(PriorityWeightedQueue.class);

	private double[] stateProbabilities;
	private double[] stateWeightList;

	private StackFIFO lambda;
	private StackFIFO mu;

	private ArrayList<PriorityWeightedQueueRequest> queuedRequests;
	private ArrayList<PriorityWeightedQueueRequest> inProgressRequests;
	private LinkedList<PriorityWeightedQueueRequest> processedRequests;
	private ArrayList<PriorityWeightedQueueRequest> expiredRequests;

	private int maxProcessedRequests = 1000;
	
	/*
	 * This queue implementation allows to resize queue at runtime.
	 */
	private int queueCapacity;

	/**
	 * Constructor PriorityWeightedQueue $description$
	 */
	public PriorityWeightedQueue(int capacity) {

		log.debug("Entering the PriorityWeightedQueue constructor");

		queuedRequests     = new ArrayList<PriorityWeightedQueueRequest>();
		inProgressRequests = new ArrayList<PriorityWeightedQueueRequest>();
		processedRequests  = new LinkedList<PriorityWeightedQueueRequest>();
		expiredRequests    = new ArrayList<PriorityWeightedQueueRequest>();
		
		lambda = new StackFIFO(Constants.FIFO_STACK_SIZE);
		mu     = new StackFIFO(Constants.FIFO_STACK_SIZE);

		this.queueCapacity = capacity;
		
		// Initialize the probabilities and the ideal weights
		stateProbabilities = new double[getCapacity()];
		stateWeightList = new double[getCapacity()];
		stateProbabilities[0] = 1.0d;

		if (Constants.QUEUE_WEIGHTS_TYPE.equalsIgnoreCase(Constants.HARDCODED)) {
			stateWeightList[1] = 0.50d;
			stateWeightList[2] = 0.25d;
			stateWeightList[3] = 0.15d;
			stateWeightList[4] = 0.05d;
			stateWeightList[5] = 0.03d;
			stateWeightList[6] = 0.02d;
		} else {
			configureWeightListValues(stateWeightList);
		}

		log.debug("Leaving the PriorityWeightedQueue constructor");
	}

	/**
	 * This method will be invoked from the scheduler (or an administrative MBean) 
	 * to set the alpha value. Here alpha is the coefficient for the time to live
	 * attribute and it will be used in the calculation of request's total weight.
	 * 
	 * @param alpha
	 *            weight associated with the "time to live" of a request
	 * 
	 */
	public synchronized void setAlpha(double alpha) {

		if (Constants.ALPHA != alpha) {
			Constants.ALPHA = alpha;
		}
	}

	/**
	 * This method will be invoked from the scheduler (or an administrative MBean) 
	 * to set the beta value. Here beta is the coefficient for the priority attribute
	 * and it will be used in the calculation of request's total weight.
	 * 
	 * @param beta
	 *            weight associated with the "priority" of a request
	 * 
	 */
	public synchronized void setBeta(double beta) {

		if (Constants.BETA != beta) {
			Constants.BETA = beta;
		}
	}

	/**
	 * This method will be invoked from the scheduler (or a MBean) to set
	 * the gamma value. Here gamma is the coefficient for the "size" attribute and
	 * it will be used in the calculation of the request's total weight.
	 * 
	 * @param gamma
	 *            weight associated with the "size" of a request
	 */
	public synchronized void setGamma(double gamma) {

		if (Constants.GAMMA != gamma) {
			Constants.GAMMA = gamma;
		}
	}

	/**
	 * Returns the number of processed requests in the queue.
	 * Queue will only keep up to specified number of processed requests. 
	 * 
	 * @return int total number of processed requests.
	 */
	public synchronized int getProcessedRequestsCount() {
		return processedRequests.size();
	}

	/**
	 * Returns the total number of requests from the queue that are currently executed.
	 * 
	 * @return int total number of requests with status <tt>PlinthosRequestStatus.IN_PROGRESS</tt>.
	 */
	public synchronized int getInProgressRequestsCount() {
		return inProgressRequests.size(); 
	}
	
	/**
	 * Returns the total number expired requests count.
	 * 
	 * @return int total number of expired requests.
	 */
	public synchronized int getExpiredRequestsCount() {
		return expiredRequests.size();
	}

	/**
	 * Returns the processed requests references in the queue.
	 * 
	 * @return ArrayList contains the processing requests references
	 */
	public synchronized ArrayList<? extends QueueRequest> getInProgressRequests() {
		return new ArrayList<PriorityWeightedQueueRequest>(inProgressRequests);
	}

	/**
	 * Returns the expired requests references.
	 * 
	 * @return ArrayList contains the expired requests references
	 */
	public synchronized ArrayList<? extends QueueRequest> getExpiredRequests() {
		return new ArrayList<PriorityWeightedQueueRequest>(expiredRequests);		
	}

	/**
	 * This method returns the mean arrival rate of the requests.
	 * 
	 * @return mean arrival rate
	 */
	public synchronized double getMeanWaitingRate() {

		log.debug("Entering the getMeanWaitingRate method");

		double meanWaitingRate = 0.0;

		Iterator<PriorityWeightedQueueRequest> processingRequestsIterator = inProgressRequests.iterator();
		
		int size = inProgressRequests.size();
		
		if (size > 0) {

			log.debug("Calculating mean waiting rate.");
			
			while (processingRequestsIterator.hasNext()) {
				
				PriorityWeightedQueueRequest queueRequest = processingRequestsIterator.next();
				
				meanWaitingRate += queueRequest.getWaitTime();
			}
			
			meanWaitingRate /= size;
			
			log.debug("Mean Waiting Rate  meanWaitingRate = " + meanWaitingRate);
		}
		
		log.debug("Leaving the getMeanWaitingRate method");
		
		return meanWaitingRate;
	}

	/**
	 * This method returns the mean service rate of the requests.
	 * 
	 * @return mean service rate
	 */
	public synchronized double getMeanServiceRate() {
		log.debug("Entering the getMeanServiceRate method");

		double meanServiceRate = 0.0;
		
		// make sure that, nobody can update the processed requests
		synchronized (processedRequests) {
			
			Iterator<PriorityWeightedQueueRequest> processedRequestsIterator = processedRequests.iterator();
			int size = processedRequests.size();
			
			if (size > 0) {
				log.debug("Calculating mean Service rate.");
				
				while (processedRequestsIterator.hasNext()) {
					PriorityWeightedQueueRequest queueRequest = processedRequestsIterator.next();
					meanServiceRate += queueRequest.getServiceTime();
				}
				
				meanServiceRate /= size;
				log.debug("Mean Service Rate meanServiceRate = " + meanServiceRate);
			}
		}
		log.debug("Leaving the getMeanServiceRate method");
		return meanServiceRate;
	}

	/**
	 * This method evaluates the weight w(i) for the i-th request as the sum of
	 * three contributions: The priority contribution, the time to live contribution,
	 * and the size contribution.
	 * 
	 *    W(i) = p(i) - T(i) + S(i)
	 * 
	 * @param queueRequest
	 * @return double request weight
	 */
	private double getRequestWeight(PriorityWeightedQueueRequest queueRequest) {

		log.debug("Entering the getRequestWeight method - ID: " +queueRequest.getRequestId());

		double weight = Constants.ALPHA * queueRequest.getPriority() 
		              - Constants.BETA * queueRequest.getTimeToLive()
		              + Constants.GAMMA * queueRequest.getSize();

		log.debug(" Weight = " + weight);
		
		log.debug("Leaving the getRequestWeight method");

		return weight;
	}
	
	/**
	 * Method getCurrentSize $description$
	 * 
	 * @return int
	 */
	public synchronized int getCurrentSize() {
		return queuedRequests.size();
	}

	/**
	 * Method getCapacity $description$
	 * 
	 * @return int
	 */
	public synchronized int getCapacity() {
		return queueCapacity;
	}

	/**
	 * It follows the Priority Weighted Queue Algorithm to return request object
	 * with highest weight.
	 * 
	 * @return Object request object with highest weight.
	 * @throws QueueEmptyException
	 */
	public synchronized Object dequeue() {

		log.debug("Entering into dequeue method   " + System.currentTimeMillis());

		if (queuedRequests.isEmpty()) {
			
			log.debug("Queue is empty");
			return null;
			
		} else {
			
			// ----- Implementing PWQA step 1 --------------------------
			PriorityWeightedQueueRequest[] queuedRequestsArray = 
				queuedRequests.toArray(new PriorityWeightedQueueRequest[queuedRequests.size()]);

			// Sort all queued requests based on Time To Live factor
			Arrays.sort(queuedRequestsArray, new RequestTimeToLiveComparator());
			
			// log.debug("Sorted request objects based on time to live attribute "+ Arrays.asList(queuedRequestsArray).toString());

			// Get the collection of valid requests (Current Time < Expiration Time)
			queuedRequestsArray = removeExpiredRequests(queuedRequestsArray);
			
			// Remove all existing requests from the queue and put non expired
			// requests in the queue
			queuedRequests.clear();
			queuedRequests.addAll(Arrays.asList(queuedRequestsArray));
			
			// can happen if all requests expired
			if( queuedRequestsArray.length == 0 ) {
				return null;
			}
			
			// ----- Implementing PWQA step 2 ---------------------------
			// Assign the weight for all requests
			for (int index = queuedRequestsArray.length - 1; index >= 0; index--) {
				PriorityWeightedQueueRequest queueRequest = queuedRequestsArray[index];
				double weight = getRequestWeight(queueRequest);
				queueRequest.setWeight(weight);
			}

			// ----- Implementing PWQA step 3 --------------------------
			// Sort the requests based on weight attribute
			Arrays.sort(queuedRequestsArray, new RequestWeightComparator());

			// log.debug("Sorted request objects based on weight attribute "+ Arrays.asList(queuedRequestsArray).toString());

			
			int queueState = queuedRequestsArray.length - 1;

			// ----- Implementing PWQA step 4 --------------------------
			// Process the expired requests

			// ----- Implementing PWQA step 5 --------------------------
			int dof = getDof();
			
			log.debug("Queued request length  " + queuedRequestsArray.length);
			log.debug("BirthDeathRK4 contstructor paramaeters\n dof = " + dof
					+ "\n Constants.RK4_SCHEME_GRID_POINTS =" + Constants.RK4_SCHEME_GRID_POINTS
					+ "\n Constants.RK4_SCHEME_INTEGRATION_TIME = " + Constants.RK4_SCHEME_INTEGRATION_TIME);

			BirthDeathRK4 bdrk4 = new BirthDeathRK4(dof, 
					                                Constants.RK4_SCHEME_GRID_POINTS,
					                                Constants.RK4_SCHEME_INTEGRATION_TIME);

			// Set the Initial Conditions (IC)
			double[] ic = new double[dof];

			for (int i = 0; i < dof; i++) {
				
				ic[i] = stateProbabilities[i];
				
				//TODO make this a DEBUG statement
				if (stateProbabilities[i] > 0) {
					log.info("stateProbabilities  ic[" + i + "] =" + stateProbabilities[i]);
				}
			}

			bdrk4.setInitialConditions(ic);

			// DEBUG
			// printParams();

			// Solve the system
			ic = bdrk4.solve(lambda.toConstantArray(dof), mu.toConstantArray(dof));

			// Assign the probabilities
			for (int i = 0; i < dof; i++) {
				stateProbabilities[i] = ic[i];
			}

			for (int i = dof + 1; i < getCapacity(); i++) {
				stateProbabilities[i] = 0.0;
			}

			// First rule			
			if (stateProbabilities[queueState] - stateWeightList[queueState] > Constants.QUEUE_WEIGHT_TOLERANCE) {
				// DO SOMETHING ABOUT IT
				// look at the documentation for gamma
			}

			// Second rule
			if (Constants.QUEUE_TOLERANCE_INDEX < getCapacity()) {
				if (stateProbabilities[Constants.QUEUE_TOLERANCE_INDEX] > Constants.QUEUE_TOLERANCE) {
					// Notify the administrator by email
				}
			} else {
				// throw an Exception that the configurable variable
				// QUEUE_TOLERANCE_INDEX is larger
				// than the capacity and set the QUEUE_TOLERANCE_INDEX
				// equal to the capacity.
			}
			
			log.debug("Leaving from dequeue method   " + System.currentTimeMillis());
			
			if (queuedRequests.isEmpty()) {
				log.debug("Queue is empty");
				return null;
			}
			
			return queuedRequestsArray[queuedRequestsArray.length - 1];
		}
	}
	
	@SuppressWarnings("unused")
	private void printParams() {
		
		int dof = getDof();
		double temp[] = lambda.toConstantArray(dof);

		log.debug("Lambda values  ");
		for (int i = 0; i < temp.length; i++) {
			log.debug("Lambda[" + i + "] = " + temp[i]);
		}
		temp = mu.toConstantArray(dof);
		log.debug("mu values  ");
		for (int i = 0; i < temp.length; i++) {
			log.debug("mu[" + i + "] = " + temp[i]);
		}		
	}

	private int getDof() {
		
		int dof = queuedRequests.size();
		
		if (dof < Constants.RK4_DEFAULT_INITIAL_DOF) {
			dof = Constants.RK4_DEFAULT_INITIAL_DOF;
		}
		
		return dof;
	}


	/**
	 * Adds the specified element to the queue
	 * 
	 * @param item
	 *            Object to be placed in the queue
	 * 
	 * @throws QueueOverflowException
	 *             if the queue is fullS
	 */
	public synchronized boolean enqueue(Object item) {

		log.debug("Entering into enqueue method   " + System.currentTimeMillis());
		
		boolean status = false;
		PriorityWeightedQueueRequest qR = (PriorityWeightedQueueRequest) item;
		
		if (queuedRequests.contains(qR)) {
			log.debug("Request already exists in the queue  -->  ID = "+qR.getRequestId());
		} else {
			if ( getCurrentSize() < getCapacity() ) {
				long currentTime = System.currentTimeMillis();
				lambda.push(currentTime);
				qR.setQueueRegistrationTime(currentTime);
				queuedRequests.add(qR);
				status = true;
			}
		}
		
		log.debug("Leaving from enqueue method   " + System.currentTimeMillis());
		return status;
	}

	/**
	 * This method returns an array of request objects, sorted based on the 
	 * time-to-live attribute. This
	 * method returns the set of request objects, which are having Current Time <
	 * Expire Time Factor
	 * 
	 * @param list
	 *            request objects in sorted order
	 * 
	 * @return Collection set or subset of request, which has Current Time <
	 *         Expiration Time
	 */
	private PriorityWeightedQueueRequest[] removeExpiredRequests(PriorityWeightedQueueRequest[] list) {

		log.debug("Entering into removeExpiredRequests method   ");

		List<PriorityWeightedQueueRequest> requestList = 
			new ArrayList<PriorityWeightedQueueRequest>();

		for (int index = list.length - 1; index >= 0; index--) {
			PriorityWeightedQueueRequest queueRequest = list[index];

			// Set the time to live
			//queueRequest.setTimeToLive(queueRequest.getExpirationTime() - System.currentTimeMillis());
			log.debug("QueueRequest Expiretime " + queueRequest.getTimeToLive() + " EXPIRE_TIME_FACTOR"
					+ Constants.EXPIRE_TIME_FACTOR);
			
			// If the following conditions are true, then the request expired,
			// so add it to the expired request list and remove it from the original list
			if (queueRequest.getTimeToLive() < Constants.EXPIRE_TIME_FACTOR) {
				expiredRequests.add(queueRequest);
			} else {
				requestList.add(queueRequest);
			}
		}
		log.debug("Leaving from removeExpiredRequests method");
		return requestList.toArray(new PriorityWeightedQueueRequest[0]);
	}

	/**
	 * This method reads the weight list values from the constantns file
	 * 
	 * @param stateWeightList
	 *            reference to state weight list values
	 */
	private void configureWeightListValues(double[] stateWeightList) {
		
		log.debug("Entering into configureWeightListValues method   ");
		try {
			StringTokenizer strTokenize = new StringTokenizer(Constants.WEIGHT_VALUES,
					Constants.WEIGHT_VALUE_SEPERATOR);
			int counter = 0;
			
			while (strTokenize.hasMoreTokens()) {
				
				stateWeightList[Constants.WEIGHT_LIST_START_INDEX + counter] = 
					Double.parseDouble(strTokenize.nextToken());
				
				counter++;
				
				if (Constants.WEIGHT_LIST_START_INDEX + counter > getCapacity()) {
					log.error("Trying to add  elements to weight list more its capacity");
					throw new IllegalArgumentException("Trying to add  elements to weight list more its capacity");
				}
			}
			
		} catch (Throwable exception) {
			log.error("Invalid configuration. Please check the Weight List configuration values");
			throw new IllegalArgumentException("Invalid configuration for weight list");
		}
		
		log.debug("Leaving from configureWeightListValues method   ");
	}

	@Override
	public synchronized void notifyQueueAboutRequestCompletion(Object r) {
		PriorityWeightedQueueRequest qR = (PriorityWeightedQueueRequest)r;

		// if everything went fine the request should not exist in queuedRequests
		queuedRequests.remove(qR);
		
		if ( inProgressRequests.contains(qR) ) {
			long currentTime = System.currentTimeMillis();
			mu.push(currentTime);
			inProgressRequests.remove(qR);
			qR.setServiceCompletionTime(currentTime);

			if( processedRequests.size() >= maxProcessedRequests) {
				processedRequests.remove();
			}
			processedRequests.add(qR);
		}		
		
	}

	@Override
	public synchronized void notifyQueueAboutRequestStart(Object r) {
		PriorityWeightedQueueRequest qR = (PriorityWeightedQueueRequest)r;
		if (queuedRequests.contains(qR)) {
			queuedRequests.remove(qR);
		}
		qR.setServiceBeginningTime(System.currentTimeMillis());
		inProgressRequests.add(qR);
		log.info("QueueRequest " + qR.getRequestId() + " assigned for processing");
	}

	@Override
	public synchronized boolean isEmpty() {
		return getCurrentSize() == 0;
	}

	@Override
	public synchronized boolean remove(QueueRequest qR) {
		
		boolean removed = false;
		
		removed = expiredRequests.remove(qR) ||
				  queuedRequests.remove(qR);

		return removed;
	}

	@Override
	public synchronized List<? extends QueueRequest> getProcessedRequests() {
		return new ArrayList<PriorityWeightedQueueRequest>(processedRequests);
	}
}
