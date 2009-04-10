package org.plinthos.core.service;

import java.util.List;

import org.hibernate.Session;
import org.plinthos.core.framework.HibernateUtil;
import org.plinthos.core.model.RegisteredTask;
import org.plinthos.core.persistence.DAOFactory;
import org.plinthos.core.persistence.RegisteredTaskDAO;

//TODO: add robust txn handling
class TaskRegistryImpl implements TaskRegistry {

	TaskRegistryImpl() {
		
	}
	
	@Override
	public RegisteredTask findTask(String taskType) {
		RegisteredTaskDAO dao = getRegisteredTaskDAO();
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        RegisteredTask task = dao.findById(taskType, false);
    	session.getTransaction().commit(); 
		return task;
	}

	@Override
	public RegisteredTask registerTask(RegisteredTask task) {
		RegisteredTaskDAO dao = getRegisteredTaskDAO();
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
   
        dao.makePersistent(task);
		
    	session.getTransaction().commit();
    	
    	return task;
	}

	@Override
	public List<RegisteredTask> findAll() {
		RegisteredTaskDAO dao = getRegisteredTaskDAO();
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
   
        List<RegisteredTask> allTasks = dao.findAll();
		
    	session.getTransaction().commit();
    	
    	return allTasks;
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
