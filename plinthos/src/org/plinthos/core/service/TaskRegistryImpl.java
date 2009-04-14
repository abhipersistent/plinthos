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
	
	@Override
	public RegisteredTask findTask(final String taskType) {
		
		TxTemplate txTemplate = new TxTemplate();
		
		TxAction<RegisteredTask> txAction = new TxAction<RegisteredTask>() {

			@Override
			public RegisteredTask run() {
				RegisteredTaskDAO dao = getRegisteredTaskDAO();
				return dao.findById(taskType, false);
			}
			
		};
 
		return txTemplate.execute(txAction);
	}

	@Override
	public RegisteredTask registerTask(final RegisteredTask task) {
		
		TxTemplate txTemplate = new TxTemplate();
		TxAction<RegisteredTask> txAction = new TxAction<RegisteredTask>() {
			@Override
			public RegisteredTask run() {
				RegisteredTaskDAO dao = getRegisteredTaskDAO();
				return dao.makePersistent(task);
			}
		};
		
		return txTemplate.execute(txAction);
	}

	@Override
	public List<RegisteredTask> findAll() {
		
		TxTemplate txTemplate = new TxTemplate();
		TxAction<List<RegisteredTask>> txAction = new TxAction<List<RegisteredTask>>() {

			@Override
			public List<RegisteredTask> run() {
				RegisteredTaskDAO dao = getRegisteredTaskDAO();
				return dao.findAll();
			}
			
		};

		return txTemplate.execute(txAction);
	}
	
	
	@Override
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
