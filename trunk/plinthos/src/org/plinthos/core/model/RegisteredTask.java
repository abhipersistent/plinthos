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


/**
 * An abstraction of a registered task.
 * 
 * This entity allows configuration of independent tasks deployed on the PlinthOS platform.
 * 
 * When PlinthOS application receives a request, it looks up for registered task in its repository
 * matching the client supplied request type.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class RegisteredTask  implements Serializable {
	private static final long serialVersionUID = -3733001148708646766L;
	
	private String taskType;	
	private String executorClass;
	private boolean etcSupported;

	@SuppressWarnings("unused")
	private int version;
	
	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
	public String getExecutorClass() {
		return executorClass;
	}

	public void setExecutorClass(String exClass) {
		this.executorClass = exClass;
	}

	public boolean isEtcSupported() {
		return etcSupported;
	}

	/**
	 * Setter for the etcSupported.
	 *
	 * @param etcSupported the etcSupported to set
	 */
	public void setEtcSupported(boolean etcSupported) {
		this.etcSupported = etcSupported;
	}

	@Override
	public String toString() {
		return "[" +
		"taskType=" + taskType +
		", executorClass=" + executorClass +
		", etcSupported=" + etcSupported +
		"]";
		
	}
	
	/**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        
    	final int prime = 11;
        
        int hash = 7;
        
        hash = (prime * hash) + (null == taskType      ? 0 : taskType.hashCode());
        hash = (prime * hash) + (null == executorClass ? 0 : executorClass.hashCode());
        
        return hash;
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

        final RegisteredTask other = (RegisteredTask) obj;
        if ( this.getTaskType() == null || other.getTaskType() == null ) {
        	return false;
        }
        return this.getTaskType().equals(other.getTaskType());
    }
	
}
