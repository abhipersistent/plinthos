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

// JAVA 2 API
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.plinthos.core.PlinthosException;
import org.plinthos.core.dao.PlinthosRequestDao;
import org.plinthos.core.dao.impl.PlinthosRequestDaoImpl;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.PlinthosRequestStatus;
import org.plinthos.core.process.ProcessUtil;
import org.plinthos.core.process.RequestManager;
import org.plinthos.queue.Placer;
import org.plinthos.queue.QueueEmptyException;
import org.plinthos.queue.QueueOverflowException;
import org.plinthos.queue.routing.BirthDeathRK4;
import org.plinthos.util.Constants;
import org.plinthos.util.PlinthosDBUtil;
import org.plinthos.util.ServiceLocator;
import org.plinthos.util.StackFIFO;


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
public class PriorityWeightedQueueImpl implements QueueService {

	/**
	 * The queue's state represents how many elements exist in the queue, at any given time
	 */
	private int queueState;

	private double[] stateProbabilities;
	private double[] stateWeightList;

	private StackFIFO lambda;
	private StackFIFO mu;

	private ArrayList<QueueRequest> queuedRequests;
	private ArrayList<QueueRequest> inProgressRequests;
	private ArrayList<QueueRequest> processedRequests;
	private ArrayList<QueueRequest> expiredRequests;

	private int queueCapacity;
	private final Logger log = Logger.getLogger(PriorityWeightedQueueImpl.class);

	/**
	 * Constructor PriorityWeightedQueueImpl $description$
	 */
	public PriorityWeightedQueueImpl() {

		log.debug("Entering the PriorityWeightedQueueImpl constructor");

		queuedRequests     = new ArrayList<QueueRequest>();
		inProgressRequests = new ArrayList<QueueRequest>();
		processedRequests  = new ArrayList<QueueRequest>();
		expiredRequests    = new ArrayList<QueueRequest>();
		
		lambda = new StackFIFO(Constants.FIFO_STACK_SIZE);
		mu     = new StackFIFO(Constants.FIFO_STACK_SIZE);
		
		setCapacity(Constants.QUEUE_CAPACITY);
		
		// Initialize the probabilities and the ideal weights
		stateProbabilities = new double[getCapacity()];
		stateWeightList = new double[getCapacity()];
		stateProbabilities[0] = 1.0d;

		// TODO: write a function for the ideal weights.
		if (Constants.QUEUE_WEIGHTS_TYPE.equalsIgnoreCase(Constants.HARDCODED)) {
			stateWeightList[1] = 0.50d;
			stateWeightList[2] = 0.25d;
			stateWeightList[3] = 0.15d;
			stateWeightList[4] = 0.05d;
			stateWeightList[5] = 0.03d;
			stateWeightList[6] = 0.02d;
		} else {
			// TODO: call the function with some configurable parameters
			configureWeightListValues(stateWeightList);
		}

		log.debug("Leaving the PriorityWeightedQueueImpl constructor");
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
	 * This method return the number of requests in the system, it includes the
	 * requests in the queue as well as requests in the service.
	 * 
	 * @return int queue length
	 */
	public int getQueueLength() {
		return getInProgressRequestsCount() + queuedRequests.size();
	}

	/**
	 * Returns the total number of processed requests in the queue.
	 * 
	 * @return int total number of processed requests.
	 */
	public int getProcessedRequestsCount() {
		return processedRequests.size();
	}

	/**
	 * Returns the total number of requests from the queue that are currently executed.
	 * 
	 * @return int total number of requests with status <tt>PlinthosRequestStatus.IN_PROGRESS</tt>.
	 */
	public int getInProgressRequestsCount() {
		return inProgressRequests.size(); 
	}
	
	/**
	 * Returns the total number expired requests count.
	 * 
	 * @return int total number of expired requests.
	 */
	public int getExpiredRequestsCount() {
		return expiredRequests.size();
	}

	/**
	 * Returns the processed requests references in the queue.
	 * 
	 * @return ArrayList contains the processing requests references
	 */
	public ArrayList<QueueRequest> getInProgressRequests() {
		return inProgressRequests;
	}

	/**
	 * Returns the expired requests references.
	 * 
	 * @return ArrayList contains the expired requests references
	 */
	public ArrayList<QueueRequest> getExpiredRequests() {
		return expiredRequests;
	}

	/**
	 * This method returns the mean arrival rate of the requests.
	 * 
	 * @return mean arrival rate
	 */
	public double getMeanWaitingRate() {

		log.debug("Entering the getMeanWaitingRate method");

		double meanWaitingRate = 0.0;

		// make sure that, nobody can update the processing requests
		synchronized (inProgressRequests) {
			Iterator<QueueRequest> processingRequestsIterator = inProgressRequests.iterator();
			
			int size = inProgressRequests.size();
			
			if (size > 0) {

				log.debug("Calculating mean waiting rate.");
				
				while (processingRequestsIterator.hasNext()) {
					
					QueueRequest queueRequest = processingRequestsIterator.next();
					
					meanWaitingRate += queueRequest.getWaitTime();
				}
				
				meanWaitingRate /= size;
				
				log.debug("Mean Waiting Rate  meanWaitingRate = " + meanWaitingRate);
			}
		}
		
		log.debug("Leaving the getMeanWaitingRate method");
		
		return meanWaitingRate;
	}

	/**
	 * This method returns the mean service rate of the requests.
	 * 
	 * @return mean service rate
	 */
	public double getMeanServiceRate() {
		log.debug("Entering the getMeanServiceRate method");

		double meanServiceRate = 0.0;
		
		// make sure that, nobody can update the processed requests
		synchronized (processedRequests) {
			
			Iterator<QueueRequest> processedRequestsIterator = processedRequests.iterator();
			int size = processedRequests.size();
			
			if (size > 0) {
				log.debug("Calculating mean Service rate.");
				
				while (processedRequestsIterator.hasNext()) {
					QueueRequest queueRequest = processedRequestsIterator.next();
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
	 * Gets the Lambda values
	 * 
	 * @return StackFIFO
	 */
	public StackFIFO getLambda() {
		return lambda;
	}

	/**
	 * Method getMu $description$
	 * 
	 * @return StackFIFO
	 */
	public StackFIFO getMu() {
		return mu;
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
	public double getRequestWeight(QueueRequest queueRequest) {

		log.debug("Entering the getRequestWeight method - ID: " +queueRequest.getRequestId());

		double weight = Constants.ALPHA * queueRequest.getPriority() 
		              - Constants.BETA * queueRequest.getTimeToLive()
		              + Constants.GAMMA * queueRequest.getSize();

		log.debug(" Weight = " + weight);
		
		log.debug("Leaving the getRequestWeight method");

		return weight;
	}

	/**
	 * Method getUtilizationFactor $description$
	 * @return double
	 */
	public double getUtilizationFactor() {
		return 10.0;
	}

	/**
	 * Method isEmpty $description$
	 * @return boolean
	 */
	public synchronized boolean isEmpty() {
		
		ArrayList<Integer> newReqs = hasSubmitted();
		
		if (newReqs.size()>0) {
			String jndiName = "PlinthOS/"+ PlinthosRequestDaoImpl.class.getSimpleName() + "/local";
			
			PlinthosRequestDao requestDao = (PlinthosRequestDao) ServiceLocator.getInstance().getFromJNDI(jndiName);
			
			PlinthosRequest plinthosRequest;
			for (Integer id: newReqs) {

				plinthosRequest = requestDao.findByPrimaryKey(id);
				
				// Place it in the queue
				QueueRequest queueRequest = new QueueRequest(plinthosRequest.getId());
		        
				queueRequest.setPriority(plinthosRequest.getPriority());
		        
		        //TODO: Remove the hardcoded value
		        queueRequest.setSize(1);
		        
		        queueRequest.setExpirationTime(plinthosRequest.getExpiration().getTime());

		        // Get the reference to place the request in the queue
		        Placer placer = RequestPlacer.getInstance();
		        if ( placer.placeRequest(queueRequest) == false ) {
		        	// requestDao.delete(plinthosRequest);
		        	// log.error(">>>  FAILED TO LOAD TABLE RECORDS IN THE QUEUE  <<<");
		        }
			}
			
			return false;
			
		} else {
			
			return queuedRequests.isEmpty();
		}
	}

	private ArrayList<Integer> hasSubmitted() {
		ArrayList<Integer> reqs = new ArrayList<Integer>(5);
		ResultSet rs = null;
		Connection connection = null;
		try {
			// Grab a DB connection from the pool for the real work below
			connection = PlinthosDBUtil.getDBConnection();
			
			// check if there are any requests in the table
			// that were not added in the Queue
			rs = PlinthosDBUtil.executeQuery ( connection, "SELECT * FROM request WHERE status='SUBMITTED'" );
			
			try {
				
				while (rs.next()) {
					reqs.add(rs.getInt(1));
				}
				
			} catch (SQLException sqlX) {
				log.error(sqlX.getMessage());
			}
			
		} catch ( PlinthosException plinthosEx ) {
			
			log.error ( "Could not find if there are SUBMITTED requests" );
			
		} finally {
			PlinthosDBUtil.closeResultSet ( rs );
			PlinthosDBUtil.closeConnection ( connection );
		}
		
		return reqs;
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
	 * Method setCapacity $description$
	 * 
	 * @param size
	 */
	public synchronized void setCapacity(int size) {
		queueCapacity = size;
	}

	/**
	 * It follows the Priority Weighted Queue Algorithm to return request object
	 * with highest weight.
	 * 
	 * @return Object request object with highest weight.
	 * @throws QueueEmptyException
	 */
	// Babis suggestion required
	public synchronized Object dequeue() {

		log.debug("Entering into dequeue method   " + System.currentTimeMillis());

		//TODO: Do we even need this if-else branching? (!)
		if (queuedRequests.isEmpty()) {
			log.debug("Queue is empty");
			return null;
		} else {
			// ----- Implementing PWQA step 1 --------------------------
			QueueRequest[] queuedRequestsArray = queuedRequests.toArray(new QueueRequest[0]);

			// Sort all queued requests based on Time To Live factor
			Arrays.sort(queuedRequestsArray, new RequestTimeToLiveComparator());
			
			// log.debug("Sorted request objects based on time to live attribute "+ Arrays.asList(queuedRequestsArray).toString());

			// Get the collection of valid requests (Current Time < Expiration Time)
			queuedRequestsArray = removeExpiredRequests(queuedRequestsArray);

			// ----- Implementing PWQA step 2 ---------------------------
			// Assign the weight for all requests
			for (int index = queuedRequestsArray.length - 1; index >= 0; index--) {
				QueueRequest queueRequest = (QueueRequest) queuedRequestsArray[index];
				double weight = getRequestWeight(queueRequest);
				queueRequest.setWeight(weight);
			}

			// ----- Implementing PWQA step 3 --------------------------
			// Sort the requests based on weight attribute
			Arrays.sort(queuedRequestsArray, new RequestWeightComparator());

			// log.debug("Sorted request objects based on weight attribute "+ Arrays.asList(queuedRequestsArray).toString());

			queueState = queuedRequestsArray.length;

			// ----- Implementing PWQA step 4 --------------------------
			// Process the expired requests
			// TODO: This may need to be done asynchronously later. [Babis - July 10, 2003]
			processExpiredRequests();

			// ----- Implementing PWQA step 5 --------------------------
			int dof = queuedRequestsArray.length;
			if (dof < Constants.RK4_DEFAULT_INITIAL_DOF) {
				dof = Constants.RK4_DEFAULT_INITIAL_DOF;
			}
			
			log.debug("Queued request length  " + queuedRequestsArray.length);
			log.debug("BirthDeathRK4 contstructor paramaeters  dof = " + dof
					+ " Constants.RK4_SCHEME_GRID_POINTS =" + Constants.RK4_SCHEME_GRID_POINTS
					+ "   Constants.RK4_SCHEME_INTEGRATION_TIME = " + Constants.RK4_SCHEME_INTEGRATION_TIME);

			BirthDeathRK4 bdrk4 = new BirthDeathRK4(dof, Constants.RK4_SCHEME_GRID_POINTS,
					Constants.RK4_SCHEME_INTEGRATION_TIME);

			// Set the Initial Conditions (IC)
			double[] ic = new double[dof];

			for (int i = 0; i < dof; i++) {
				
				ic[i] = stateProbabilities[i];
				
				log.debug("stateProbabilities  ic[" + i + "] =" + stateProbabilities[i]);
			}

			bdrk4.setInitialConditions(ic);

			// DEBUG -- START
//			double temp[] = lambda.toConstantArray(dof);
//
//			log.debug("Lambda values  ");
//			for (int i = 0; i < temp.length; i++) {
//				log.debug("Lambda[" + i + "] = " + temp[i]);
//			}
//			temp = mu.toConstantArray(dof);
//			log.debug("mu values  ");
//			for (int i = 0; i < temp.length; i++) {
//				log.debug("mu[" + i + "] = " + temp[i]);
//			}
			// DEBUG -- END

			// Solve the system
			ic = bdrk4.solve(lambda.toConstantArray(dof), mu.toConstantArray(dof));

			// Assign the probabilities
			for (int i = 0; i < dof; i++) {
				stateProbabilities[i] = ic[i];
			}

			for (int i = dof + 1; i < getCapacity(); i++) {
				stateProbabilities[i] = Constants.ZERO_DOUBLE;
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
			
			// Remove all existing requests from the queue and put non expired
			// requests in the queue
			queuedRequests.clear();
			queuedRequests.addAll(Arrays.asList(queuedRequestsArray));
			
			// Remove the item from the
			// queuedRequests.remove(queuedRequestsArray
			// [queuedRequestsArray.length - 1]);
			// -----------------------------------------------------------
			log.debug("Leaving from dequeue method   " + System.currentTimeMillis());
			
			if (queuedRequests.isEmpty()) {
				log.debug("Queue is empty");
				return null;
			}
			
			return queuedRequestsArray[queuedRequestsArray.length - 1];
		}
	}
	
	/* (non-Javadoc)
	 * @see org.plinthos.core.queue.QueueService#removeRequest(int)
	 */
	public synchronized boolean removeRequest(int requestId) {
		
		// We want to remove a request from the queue.
		if (isEmpty()) {
			log.warn("The Queue is empty! There is nothing to remove ...");
			return true;
		}

		Iterator<QueueRequest> iter = queuedRequests.iterator();
		while ( iter.hasNext() ) {
			QueueRequest queueRequest = iter.next();
			if ( queueRequest.getRequestId() == requestId ) {
				iter.remove();
			}
		}
		
		return true;
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
		QueueRequest qR = (QueueRequest) item;
		
		if (queuedRequests.contains(qR)) {
			
			// log.warn("Request already exists in the queue  -->  ID = "+qR.getRequestId());
		
		} else {

			if (getCapacity() - 1 > getCurrentSize()) {
				updateQueuedRequests(qR);
				status = true;
			}
		}
		
		log.debug("Leaving from enqueue method   " + System.currentTimeMillis());
		return status;
	}

	/**
	 * Updates the processing request references. It will remove the request
	 * from the queued state and puts in the processing state.
	 * 
	 * @param queueRequest
	 */
	public synchronized boolean updateInProgressRequests(QueueRequest queueRequest) {
		log.info("Entering into updateProcessingRequest method");

		try {
			if (queuedRequests.contains(queueRequest)) {

				queuedRequests.remove(queueRequest);

				queueRequest.setServiceBeginningTime(System.currentTimeMillis());

				inProgressRequests.add(queueRequest);
				
				log.info("QueueRequest " + queueRequest.getRequestId() + " assigned for processing");

				return true;
			}
			
			log.info("QueueRequest " + queueRequest.getRequestId() + " no longer in the queue");
			return false;
		} finally {
			log.info("Leaving the updateProcessingRequest method");
		}
	}

	/**
	 * Updates the processing request references. It will remove the request
	 * from the queued state and puts in the processing state.
	 * 
	 * @param queueRequest
	 *            request object
	 */
	public synchronized void updateQueuedRequests(QueueRequest queueRequest) {

		log.debug("Entering into updateQueuedRequests method   ");

		// Here the request is entering into WAITING state, so add it to queued
		// requests
		long currentTime = System.currentTimeMillis();

		lambda.push(currentTime);

		queueRequest.setQueueRegistrationTime(currentTime);

		queuedRequests.add(queueRequest);
		
		log.debug("Leaving from updateQueuedRequests method   ");
	}

	/**
	 * This method removes the request from the group of requests that are
	 * currently in progress and adds them in the group of requests that have 
	 * been processed, while keeping track of the time so that the dynamic coefficients 
	 * that determine the birth/death rate can be determined.
	 * 
	 * @param queueRequest
	 *            is the request that completed its work
	 */
	public synchronized void updateProcessedRequests(QueueRequest queueRequest) {
		log.debug("Entering into updateProcessedRequests method   ");
		
		if ( inProgressRequests.contains(queueRequest) ) {
			
			long currentTime = System.currentTimeMillis();
			mu.push(currentTime);
			
			inProgressRequests.remove(queueRequest);
			
			queueRequest.setServiceCompletionTime(currentTime);
			
			processedRequests.add(queueRequest);
		}		
		log.debug("Leaving from updateProcessedRequests method   ");
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
	private QueueRequest[] removeExpiredRequests(QueueRequest[] list) {

		log.debug("Entering into removeExpiredRequests method   ");

		List<QueueRequest> requestList = new ArrayList<QueueRequest>();

		RequestManager requestManager = ProcessUtil.getRequestManager();

		for (int index = list.length - 1; index >= 0; index--) {
			QueueRequest queueRequest = list[index];

			// Set the time to live
			queueRequest.setTimeToLive(queueRequest.getExpirationTime() - System.currentTimeMillis());
			log.debug("QueueRequest Expiretime " + queueRequest.getTimeToLive() + " EXPIRE_TIME_FACTOR"
					+ Constants.EXPIRE_TIME_FACTOR);
			
			// If the following conditions are true, then the request expired,
			// so add it to the expired request list and remove it from the original list
			if (queueRequest.getTimeToLive() < Constants.EXPIRE_TIME_FACTOR) {
				requestManager.updateRequestStatus(queueRequest.getRequestId(), PlinthosRequestStatus.EXPIRED);
				expiredRequests.add(queueRequest);
			} else {
				requestList.add(queueRequest);
			}
		}
		log.debug("Leaving from removeExpiredRequests method");
		return requestList.toArray(new QueueRequest[0]);
	}

	/**
	 * Process all expired requests
	 */
	private void processExpiredRequests() {
		//TODO
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
}
