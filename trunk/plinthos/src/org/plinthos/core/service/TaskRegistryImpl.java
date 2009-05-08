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
package org.plinthos.core.service;

import java.util.List;

import org.plinthos.core.model.RegisteredTask;
import org.plinthos.core.persistence.DAOFactory;
import org.plinthos.core.persistence.RegisteredTaskDAO;
import org.plinthos.core.persistence.txn.TxAction;
import org.plinthos.core.persistence.txn.TxTemplate;

class TaskRegistryImpl implements TaskRegistry {

	TaskRegistryImpl() {
		
	}
	
	// @Override
	public RegisteredTask findTask(final String taskType) {
		
		TxTemplate txTemplate = new TxTemplate();
		
		TxAction<RegisteredTask> txAction = new TxAction<RegisteredTask>() {

			// @Override
			public RegisteredTask run() {
				RegisteredTaskDAO dao = getRegisteredTaskDAO();
				return dao.findById(taskType, false);
			}
			
		};
 
		return txTemplate.execute(txAction);
	}

	// @Override
	public RegisteredTask registerTask(final RegisteredTask task) {
		
		TxTemplate txTemplate = new TxTemplate();
		TxAction<RegisteredTask> txAction = new TxAction<RegisteredTask>() {
			// @Override
			public RegisteredTask run() {
				RegisteredTaskDAO dao = getRegisteredTaskDAO();
				return dao.makePersistent(task);
			}
		};
		
		return txTemplate.execute(txAction);
	}

	// @Override
	public List<RegisteredTask> findAll() {
		
		TxTemplate txTemplate = new TxTemplate();
		TxAction<List<RegisteredTask>> txAction = new TxAction<List<RegisteredTask>>() {

			// @Override
			public List<RegisteredTask> run() {
				RegisteredTaskDAO dao = getRegisteredTaskDAO();
				return dao.findAll();
			}
			
		};

		return txTemplate.execute(txAction);
	}
	
	
	// @Override
	public void initTasks(List<RegisteredTask> tasks) {
		for(RegisteredTask t : tasks) {
			// only register task if it is not defined yet
			if( findTask(t.getTaskType()) == null ) {
				registerTask(t);
			}
		}
	}
	
	private RegisteredTaskDAO getRegisteredTaskDAO() {
		return DAOFactory.getInstance().getTaskDAO();		
	}
}
