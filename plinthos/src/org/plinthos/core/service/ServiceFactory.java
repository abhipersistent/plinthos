package org.plinthos.core.service;

public class ServiceFactory {

	private static final ServiceFactory instance = new ServiceFactory();

	public static ServiceFactory getInstance() {
		return instance;
	}

	private ServiceFactory() {
		
	}
	
	public RequestManager getRequestManager() {
		return new RequestManagerImpl();
	}
	
	public ConstantsManager getConstantsManager() {
		return new ConstantsManagerImpl();
	}
	
	public TaskRegistry getTaskRegistry() {
		return new TaskRegistryImpl();
	}
}
