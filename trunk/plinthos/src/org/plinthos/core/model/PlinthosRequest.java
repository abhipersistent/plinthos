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
package org.plinthos.core.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * This is a fundamental class that encapsulates the request of the system's clients.
 * <code>PlinthosRequest</code> objects are persisted and their status is tracked by 
 * the <code>status</code> variable.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 * @see org.plinthos.core.model.impl.PlinthosRequestImpl
 */
public class PlinthosRequest implements Serializable {

	private static final long serialVersionUID = -3998629321936126626L;
	
	private static Logger logger = Logger.getLogger(PlinthosRequest.class);
	
	private static final int MAX_LENGTH_FOR_STATUS_MESSAGE = 1024;
	private static final int MAX_LENGTH_FOR_PROGRESS_MESSAGE = 1024;
	
	private int id = -1;
	
	private String correlationId;
	private String userId;
	private String requestParams;
	private String status;
	private int priority;
	private Date expiration;
	private Date submissionTime;
	private Date completionTime;
	private double etc;
	private String userEmail;
	private RegisteredTask taskInfo;
	private boolean cancelRequested;
	private String progressMessage;
	private String requestResults;
	private String statusMessage;

	@SuppressWarnings("unused")
	private int version = -1;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	public String getRequestParams() {
		return requestParams;
	}
	
	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public Date getExpiration() {
		return expiration;
	}
	
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
	public Date getSubmissionTime() {
		return submissionTime;
	}
	
	public void setSubmissionTime(Date submissionTime) {
		this.submissionTime = submissionTime;
	}

	public Date getCompletionTime() {
		return completionTime;
	}
	
	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}

	public double getEtc() {
		return etc;
	}

	public void setEtc(double etc) {
		this.etc = etc;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = checkLength(statusMessage, 
				MAX_LENGTH_FOR_STATUS_MESSAGE, "statusMessage");
	}

	private String checkLength(String value, int maxLength, String label) {
		String truncatedValue = value;
		if( value != null && value.length() > maxLength ) {
			logger.info(label + " value exceeds max length of " + maxLength + 
					". Truncated version will be stored. Original value: " + value);
			truncatedValue = value.substring(0, maxLength);
		}
		return truncatedValue;
	}
	
	/**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (int)id;
        return result;
    }

    public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public RegisteredTask getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(RegisteredTask taskInfo) {
		this.taskInfo = taskInfo;
	}
	
	public boolean isCancelRequested() {
		return cancelRequested;
	}

	public void setCancelRequested(boolean cancelRequested) {
		this.cancelRequested = cancelRequested;
	}

	public String getProgressMessage() {
		return progressMessage;
	}

	public void setProgressMessage(String progressMessage) {
		this.progressMessage = checkLength(progressMessage, 
				MAX_LENGTH_FOR_PROGRESS_MESSAGE, "progressMessage");

	}

	public String getRequestResults() {
		return requestResults;
	}

	public void setRequestResults(String requestResults) {
		this.requestResults = requestResults;
	}

	
	
	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ( obj == null ) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final PlinthosRequest other = (PlinthosRequest) obj;
        
        return this.getId() == other.getId();
    }
}
