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
package org.plinthos.plugin;

/**
 * A rudimentary carrier of information. It encapsulates a basic PlinthOS request
 * for the purpose of submitting a request to a PlinthOS instance via (HTTP) XML-RPC.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class PlinthosXmlRequest {

	// TODO: Add validation. No more than 50 characters.
	 String userId;
	 
	 // TODO: Add validation based on the registered tasks (?)
	 String requestXml;
	 
	 // TODO: enum('SUBMITTED','RUNNING','EXPIRED','FAILED','COMPLETED','CANCELED','REJECTED')
	 //       Make it an enum and add validation.		 
	 /** The status of the request */
	 String status;
	 
	 // TODO: This admits currently any integer value. 
	 //       Examine the implications of negative values on the efficacy of the queue.
	 /** Priority of the request */
	 int priority=0;
	 
	 // TODO: Add validation. Expiration should occur after submission
	 //       We might want to make this configurable (or rather restrictive and controlled on the server)
	 /** Expiration time */
	 String expiration;

	 /** Submission time */
	 String submissionTime;

	 // TODO: Add validation. Only requests that correspond to registered tasks can be accepted.
	 /** The type of a request must be identical to one of the registered tasks of PlinthOS. */
	 String type;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the requestXml
	 */
	public String getRequestXml() {
		return requestXml;
	}

	/**
	 * @param requestXml the requestXml to set
	 */
	public void setRequestXml(String requestXml) {
		this.requestXml = requestXml;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the expiration
	 */
	public String getExpiration() {
		return expiration;
	}

	/**
	 * @param expiration the expiration to set
	 */
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the submissionTime
	 */
	public String getSubmissionTime() {
		return submissionTime;
	}

	/**
	 * @param submissionTime the submissionTime to set
	 */
	public void setSubmissionTime(String submissionTime) {
		this.submissionTime = submissionTime;
	}
}
