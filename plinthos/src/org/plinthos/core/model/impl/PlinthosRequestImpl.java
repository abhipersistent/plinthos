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
package org.plinthos.core.model.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.RegisteredTask;


/**
 * This is a fundamental class that encapsulates the request of the system's clients.
 * 
 * TODO: Add more comments describing the individual elements
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 * @see org.plinthos.core.model.PlinthosRequest
 */
@Entity
@Table(name = "request")
public class PlinthosRequestImpl implements PlinthosRequest {

	private static final long serialVersionUID = -3998629321936126626L;
	
	private int id;
	private String userId;
	private String requestParams;
	private String status;
	private int priority;
	private Date expiration;
	private Date submissionTime;
	private Date completionTime;
	private RegisteredTask taskInfo;
	
	/**
	 * Getter for the id.
	 *
	 * @return the id
	 */
	@Id
	@Column(name="ID")
	public int getId() {
		return id;
	}
	
	/**
	 * Setter for the id.
	 *
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
	/**
	 * Getter for the userId.
	 *
	 * @return the userId
	 */
	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}
	
	/**
	 * Setter for the userId.
	 *
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	/* (non-Javadoc)
	 * @see org.plinthos.core.model.PlinthosRequest#getRequestParams()
	 */
	@Column(name="TEMPLATE_PARAMS")
	public String getRequestParams() {
		return requestParams;
	}
	
	
	/* (non-Javadoc)
	 * @see org.plinthos.core.model.PlinthosRequest#setRequestParams(java.lang.String)
	 */
	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}
	
	/**
	 * Getter for the status.
	 *
	 * @return the status
	 */
	@Column(name = "STATUS")
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
	 * Getter for the priority.
	 *
	 * @return the priority
	 */
	@Column(name = "PRIORITY")
	public int getPriority() {
		return priority;
	}
	
	/**
	 * Setter for the priority.
	 *
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * Getter for the expiration.
	 *
	 * @return the expiration
	 */
	@Column(name = "EXPIRATION")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getExpiration() {
		return expiration;
	}
	
	/**
	 * Setter for the expiration.
	 *
	 * @param expiration the expiration to set
	 */
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
	/**
	 * Getter for the submissionTime.
	 *
	 * @return the submissionTime
	 */
	@Column(name = "SUBMISSION_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getSubmissionTime() {
		return submissionTime;
	}
	
	/**
	 * Setter for the submissionTime.
	 *
	 * @param submissionTime the submissionTime to set
	 */
	public void setSubmissionTime(Date submissionTime) {
		this.submissionTime = submissionTime;
	}
	
	/**
	 * Getter for the completionTime.
	 *
	 * @return the completionTime
	 */
	@Column(name = "COMPLETION_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCompletionTime() {
		return completionTime;
	}
	
	/**
	 * Setter for the completionTime.
	 *
	 * @param completionTime the completionTime to set
	 */
	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}

	/**
	 * Getter for the taskInfo.
	 *
	 * @return the taskInfo
	 */
	@ManyToOne(cascade = {}, fetch = FetchType.EAGER, targetEntity = RegisteredTaskImpl.class)
	@JoinColumn(name = "REQUEST_TYPE", referencedColumnName = "TASK_TYPE")
	public RegisteredTask getTask() {
		return taskInfo;
	}

	/**
	 * Setter for the taskInfo.
	 *
	 * @param taskInfo the taskInfo to set
	 */
	public void setTask(RegisteredTask taskInfo) {
		this.taskInfo = taskInfo;
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
        return result;
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
