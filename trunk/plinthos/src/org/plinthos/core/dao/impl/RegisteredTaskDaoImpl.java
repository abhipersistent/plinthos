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
package org.plinthos.core.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.plinthos.core.dao.RegisteredTaskDao;
import org.plinthos.core.model.RegisteredTask;
import org.plinthos.core.model.impl.RegisteredTaskImpl;


/**
 * Implementation of RegisteredTaskDao.
 * 
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class RegisteredTaskDaoImpl extends GenericEntityManagerDao<RegisteredTask> implements RegisteredTaskDao {

	public RegisteredTaskDaoImpl(EntityManager em) {
		super(RegisteredTaskImpl.class, em);
	}

	/* (non-Javadoc)
	 * @see org.plinthos.core.dao.RegisteredTaskDao#findByType(java.lang.String)
	 */
	public RegisteredTask findByType(String taskType) {
		List<RegisteredTask> tasks = super.executeQuery("RegisteredTask.byTaskType", taskType);
		if ( ! tasks.isEmpty() ) {
			return tasks.get(0);
		}
		return null;
	}


}
