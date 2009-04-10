package org.plinthos.core.persistence;

public class DAOFactory {

	private static DAOFactory instance = new DAOFactory();
	
	public static DAOFactory getInstance() {
		return instance;
	}
	
	private DAOFactory() {
		
	}
	
	public PlinthosRequestDAO getRequestDAO() {
		return new PlinthosRequestDAOHibernate();
	}
	
	public RegisteredTaskDAO getTaskDAO() {
		return new RegisteredTaskDAOHibernate();
	}
	
	public SystemConfigurationDAO getConfigurationDAO() {
		return new SystemConfigurationDAOHibernate();
	}
	
	
	
}
