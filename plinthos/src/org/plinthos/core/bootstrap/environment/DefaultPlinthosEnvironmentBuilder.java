package org.plinthos.core.bootstrap.environment;

import org.apache.log4j.Logger;

public class DefaultPlinthosEnvironmentBuilder {


	private static final Logger logger = Logger.getLogger(DefaultPlinthosEnvironmentBuilder.class);
	
	
	public static final String INITIAL_APP_PROPERTIES_FILE = "plinthos-configuration.properties";
	public static final String REGISTERED_TASKS_FILE = "task-registration.csv";
	
	@SuppressWarnings("unused")
	private static final String DBTYPE_MYSQL = "mysql";
	private static final String DBTYPE_HSQLDB = "hsqldb";
	
	
	public DefaultPlinthosEnvironmentBuilder() {
		
	}
	
	public PlinthosEnvironment loadEnvironment() {
		return loadEnvironment( new PlinthosEnvironment() );
	}

	
	public PlinthosEnvironment loadEnvironment(PlinthosEnvironment c) {
		PlinthosEnvironmentHolder configHolder = 
			PlinthosEnvironmentHolder.getInstance();
		load(c);
		configHolder.setConfig(c);
		return c;
	}

	private static String PATH_SEPARATOR = System.getProperty("file.separator");
	
	private String getDefaultInitialDataLocation(String plinthosDir) {
		return plinthosDir + PATH_SEPARATOR + "config/initialdata" + PATH_SEPARATOR; 
	}
	
	private String getDefaultHibernateConfigLocation(String plinthosDir) {
		return plinthosDir + PATH_SEPARATOR + "config/hibernate" + PATH_SEPARATOR; 
	}
	
	private void load(PlinthosEnvironment c) {

		
		String plinthosDir = c.getPlinthosDir(); 
		
		if( plinthosDir == null ) {
			plinthosDir = System.getProperty("plinthos.dir");
			c.setPlinthosDir(plinthosDir);
		}
		
		if( plinthosDir == null ) {
			String msg = "plinthos.dir property is not set.";
			logger.error(msg);
			throw new RuntimeException(msg);
		}

		String initialAppPropertiesFile = c.getInitialAppPropertiesFile();
		
		if( initialAppPropertiesFile == null ) {
			initialAppPropertiesFile = getDefaultInitialDataLocation(plinthosDir) +
				INITIAL_APP_PROPERTIES_FILE;
			
			c.setInitialAppPropertiesFile(initialAppPropertiesFile);
		}
		
		String registeredTasksFile = c.getRegisteredTasksFile();
		
		if( registeredTasksFile == null ) {
			registeredTasksFile = getDefaultInitialDataLocation(plinthosDir) + 
				REGISTERED_TASKS_FILE;
			
			c.setRegisteredTasksFile(registeredTasksFile);
		}
		
		String dbType = c.getDbType();
		if( dbType == null ) {
			dbType = System.getProperty("db.type");
			c.setDbType(dbType);
		}
		
		String hbrConfigurationFile = c.getHbrConfigurationFile();
		if( hbrConfigurationFile == null ) {
			
			if( dbType == null || dbType.trim().length() == 0 ) {
				logger.info("Database type is not specified. " + 
						"Using default: " + DBTYPE_HSQLDB + 
						". Use 'db.type' property to specify database type.");
				dbType = DBTYPE_HSQLDB;
			}

			hbrConfigurationFile = getDefaultHibernateConfigLocation(plinthosDir) + 
					"hibernate-" + dbType + ".cfg.xml";
			
			c.setHbrConfigurationFile(hbrConfigurationFile);
		}
		
		// If HSQLDB then set flag indicating that we need to use Embedded DB Server
		c.setUseEmbeddedDatabaseServer( DBTYPE_HSQLDB.equalsIgnoreCase(dbType) );
		
		int httpPort = c.getHttpPort();
		
		if( httpPort <= 0) {
			String port = System.getProperty("http.server.port");
			if( port != null ) {
				httpPort = Integer.parseInt(port);
				c.setHttpPort(httpPort);
			}
		}
		
		c.setUseEmbeddedHttpServer( true );
	}
	
	
}
