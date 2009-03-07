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

/**
 * This is a fundamental class that encapsulates the request of the system's clients.
 * Since, PlinthOS was originally created to generate reports the first part of the
 * name was introduced. 
 * 
 * We do not intend to change that; besides the term <I>QueueRequest</I> 
 * is so overloaded that our historical prefix is actually good for disambiguation.
 * 
 * PlinthosRequest objects are persisted and its status is tracked by the status.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 * @see org.plinthos.core.model.impl.PlinthosRequestImpl
 */
public interface PlinthosRequest extends Serializable {

	/**
	 * Returns the unique id.
	 * @return
	 */
	public int getId();

	/**
	 * Sets the id.
	 * @param id
	 */
	public void setId(int id);

	/**
	 * Sets the user id.
	 * @param username
	 */
	public void setUserId(String username);

	/**
	 * Returns the user id.
	 * @return
	 */
	public String getUserId();

	/**
	 * Sets the request params.
	 * The content of this field encapsulates everything needed by
	 * the request handler to process the request.
	 * @param requestParams
	 */
	public void setRequestParams(String requestParams);

	/**
	 * Returns the request params.
	 * @return
	 */
	public String getRequestParams();

	/**
	 * Sets the status.
	 * @param status
	 */
	public void setStatus(String status);

	/**
	 * Returns the status.
	 * @return
	 */
	public String getStatus();

	/**
	 * Sets the priority for this request.
	 * @param priority
	 */
	public void setPriority(int priority);

	/**
	 * Returns the priority for this request.
	 * @return
	 */
	public int getPriority();

	/**
	 * Sets the expiration time.
	 * @param expiration
	 */
	public void setExpiration(Date expiration);

	/**
	 * Gets the expiration time.
	 * @return
	 */
	public Date getExpiration();

	/**
	 * Sets the submission time.
	 * @param expiration
	 */
	public void setSubmissionTime(Date submissionTime);

	/**
	 * Gets the submission time.
	 * @return
	 */
	public Date getSubmissionTime();

	/**
	 * Sets the completion time.
	 * @param expiration
	 */
	public void setCompletionTime(Date completion);

	/**
	 * Gets the completion time.
	 * @return
	 */
	public Date getCompletionTime();

	public RegisteredTask getTask();
	
	public void setTask(RegisteredTask task);
}
