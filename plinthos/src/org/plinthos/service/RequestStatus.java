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


import java.io.Serializable;


/**
 * This class encapsulates status of requests managed by the PlinthOS platform.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class RequestStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6347108333622237862L;

	/**
	 * The unique id assigned to this request.
	 */
	private String requestId;
	
	/**
	 * The status.
	 * 
	 */
	private String status;

	/**
	 * The estimated time to complete in seconds.
	 */
	private long estimatedTimeToComplete;
	
	/**
	 * Error code. 0 indicates that request was processed successfully.
	 */
	private int errorCode;
	
	/**
	 * Time of submission.
	 */
	private long dateSubmitted;
	
	/**
	 * Time of completion.
	 */
	private long dateCompleted;
	
	/**
	 * A descriptive error message is available in case of error.
	 */
	private String errorMessage;
	
	/**
	 * Getter for the requestId.
	 *
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * Setter for the requestId.
	 *
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * Getter for the status.
	 * Possible values - SUBMITTED, RUNNING, COMPLETED, EXPIRED, FAILED, CANCELLED, REJECTED.
	 * 		SUBMITTED - QueueRequest has been submitted and waiting for its turn for execution.
	 * 		RUNNING - QueueRequest being executed.
	 * 		COMPLETED - QueueRequest has completed successfully.
	 * 		EXPIRED - QueueRequest could not be completed within expiration time specified and hence terminated.
	 * 		FAILED - QueueRequest has terminated abnormally.
	 * 		CANCELLED - QueueRequest has been cancelled.
	 * 		REJECTED - QueueRequest submission failed.
	 * 
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Setter for the status.
	 *
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Getter for the estimatedTimeToComplete.
	 * 		-1 indicates that ETC is not available yet.
	 * 
	 * @return the estimatedTimeToComplete.
	 */
	public long getEstimatedTimeToComplete() {
		return estimatedTimeToComplete;
	}

	/**
	 * Setter for the estimatedTimeToComplete.
	 *
	 * @param estimatedTimeToComplete the estimatedTimeToComplete to set
	 */
	public void setEstimatedTimeToComplete(long estimatedTimeToComplete) {
		this.estimatedTimeToComplete = estimatedTimeToComplete;
	}

	/**
	 * Getter for the dateSubmitted.
	 *
	 * @return the dateSubmitted
	 */
	public long getDateSubmitted() {
		return dateSubmitted;
	}

	/**
	 * Setter for the dateSubmitted.
	 *
	 * @param dateSubmitted the dateSubmitted to set
	 */
	public void setDateSubmitted(long dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}

	/**
	 * Getter for the dateCompleted.
	 *
	 * @return the dateCompleted
	 */
	public long getDateCompleted() {
		return dateCompleted;
	}

	/**
	 * Setter for the dateCompleted.
	 *
	 * @param dateCompleted the dateCompleted to set
	 */
	public void setDateCompleted(long dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	/**
	 * Getter for the errorCode.
	 *
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * Setter for the errorCode.
	 *
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Getter for the errorMessage.
	 *
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Setter for the errorMessage.
	 *
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	

}
