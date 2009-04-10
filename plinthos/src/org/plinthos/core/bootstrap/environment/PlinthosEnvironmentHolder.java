package org.plinthos.core.bootstrap.environment;

public class PlinthosEnvironmentHolder {

	private static PlinthosEnvironmentHolder instance = 
		new PlinthosEnvironmentHolder();
	
	public static PlinthosEnvironmentHolder getInstance() {
		return instance;
	}

	private PlinthosEnvironment config;
	
	private PlinthosEnvironmentHolder() {
	}

	public PlinthosEnvironment getConfig() {
		return config;
	}

	public void setConfig(PlinthosEnvironment config) {
		this.config = config;
	}
	
	
	

	
}
