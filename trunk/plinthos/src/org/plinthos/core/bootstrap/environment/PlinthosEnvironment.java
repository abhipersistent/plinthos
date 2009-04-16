package org.plinthos.core.bootstrap.environment;



public class PlinthosEnvironment {

	private String plinthosDir = null;
	
	private String initialAppPropertiesFile = null; 
	
	private String registeredTasksFile = null;

	private String dbType = null;
	
	private String hbrConfigurationFile = null;
	
	private Boolean useEmbeddedDatabaseServer = null;
	
	private Boolean useEmbeddedHttpServer = null;
	
	private String taskClasspath = null;
	
	private int httpPort = -1;
	
	public PlinthosEnvironment() {
	}
	
	public String getPlinthosDir() {
		return plinthosDir;
	}
	
	public void setPlinthosDir(String dir) {
		this.plinthosDir = dir;
	}
	
	public String getInitialAppPropertiesFile() {
		return this.initialAppPropertiesFile;
	}
	
	public String getRegisteredTasksFile() {
		return this.registeredTasksFile;
	}

	public void setInitialAppPropertiesFile(String initialAppPropertiesFile) {
		this.initialAppPropertiesFile = initialAppPropertiesFile;
	}

	public void setRegisteredTasksFile(String registeredTasksFile) {
		this.registeredTasksFile = registeredTasksFile;
	}

	public String getHbrConfigurationFile() {
		return hbrConfigurationFile;
	}

	public void setHbrConfigurationFile(String hbrConfigurationFile) {
		this.hbrConfigurationFile = hbrConfigurationFile;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public Boolean getUseEmbeddedDatabaseServer() {
		return useEmbeddedDatabaseServer;
	}

	public void setUseEmbeddedDatabaseServer(Boolean useEmbeddedDatabaseServer) {
		this.useEmbeddedDatabaseServer = useEmbeddedDatabaseServer;
	}

	public Boolean getUseEmbeddedHttpServer() {
		return useEmbeddedHttpServer;
	}

	public void setUseEmbeddedHttpServer(Boolean useEmbeddedHttpServer) {
		this.useEmbeddedHttpServer = useEmbeddedHttpServer;
	}

	public String getTaskClasspath() {
		return taskClasspath;
	}

	public void setTaskClasspath(String taskClasspath) {
		this.taskClasspath = taskClasspath;
	}
	
	
}
