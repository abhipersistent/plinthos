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

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.plinthos.core.model.RegisteredTask;

/**
 * TODO: Add comment
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 * @see org.plinthos.core.model.RegisteredTask
 */
@Entity
@Table(name = "task_registry")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "service_provider",
		discriminatorType = DiscriminatorType.STRING)
@NamedQueries(
		@NamedQuery(name="RegisteredTask.byTaskType", query="from RegisteredTaskImpl t where t.taskType = ? ") //findByType
)
public class RegisteredTaskImpl implements RegisteredTask {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3733001148708646766L;
	
	private String taskType;	
	private String executorClass;

	private boolean etcSupported;
	
	/**
	 * Getter for the taskType.
	 *
	 * @return the taskType
	 */
	@Id
	@Column(name = "task_type", unique = true, nullable = false)
	public String getTaskType() {
		return taskType;
	}

	/**
	 * Setter for the taskType.
	 *
	 * @param taskType the taskType to set
	 */
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
	@Column(name = "executor_class", nullable = true)
	public String getExecutorClass() {
		return executorClass;
	}

	public void setExecutorClass(String exClass) {
		this.executorClass = exClass;
	}

	/**
	 * Getter for the etcSupported.
	 *
	 * @return the etcSupported
	 */
	@Column(name = "etc_supported")
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
