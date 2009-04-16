package org.plinthos.core.bootstrap.environment;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

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

		
		String nodePropertiesFile = System.getProperty("node.properties");
		
		if( nodePropertiesFile != null ) {
			
			Properties nodeProps = new Properties();
			
			try {
				InputStream in = new FileInputStream(nodePropertiesFile);
				
				nodeProps.load(in);
			}
			catch(Exception e) {
				throw new RuntimeException(
						"Failed to load node properties from: " + nodePropertiesFile, e);
			}
			
			@SuppressWarnings("unchecked")
			Enumeration<String> names = (Enumeration<String>)nodeProps.propertyNames();
			while( names.hasMoreElements() ) {
				String name = names.nextElement();
				String value = nodeProps.getProperty(name);

				String existingValue = System.getProperty(name); 
				if( existingValue == null ) {
					System.setProperty(name, value);
					logger.info("set node property: " + name + "=" + value);
				}
				else {
					logger.info("skipped node property: " + name + "=" + value + 
							" because it is already set to: " + existingValue);
				}
			}
		};
		
		
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
			dbType = System.getProperty(PlinthosNodePropertyNames.DB_TYPE);
			c.setDbType(dbType);
		}
		
		//TODO: consider simplification by explicitly defining hibernate file instead of db type
		String hbrConfigurationFile = c.getHbrConfigurationFile();
		if( hbrConfigurationFile == null ) {
			
			if( dbType == null || dbType.trim().length() == 0 ) {
				logger.info("Database type is not specified. " + 
						"Using default: " + DBTYPE_HSQLDB + 
						". Use '" + PlinthosNodePropertyNames.DB_TYPE 
						+ "' property to specify database type.");
				dbType = DBTYPE_HSQLDB;
			}

			hbrConfigurationFile = getDefaultHibernateConfigLocation(plinthosDir) + 
					"hibernate-" + dbType + ".cfg.xml";
			
			c.setHbrConfigurationFile(hbrConfigurationFile);
		}
		logger.info("hibernate configuration file: " + c.getHbrConfigurationFile());
		
		// If HSQLDB then set flag indicating that we need to use Embedded DB Server
		//c.setUseEmbeddedDatabaseServer( DBTYPE_HSQLDB.equalsIgnoreCase(dbType) );
		
		int httpPort = c.getHttpPort();
		
		if( httpPort <= 0) {
			String port = System.getProperty(PlinthosNodePropertyNames.HTTP_PORT);
			if( port != null ) {
				httpPort = Integer.parseInt(port);
				c.setHttpPort(httpPort);
			}
		}
		logger.info("http port for internal HTTP server: " + c.getHttpPort());
		
		if( c.getUseEmbeddedDatabaseServer() == null ) {
			String value = System.getProperty(PlinthosNodePropertyNames.USE_EMBEDDED_DB_SERVER);
			c.setUseEmbeddedDatabaseServer(Boolean.valueOf(value));
		}
		logger.info("using internal DB Server: " + c.getUseEmbeddedDatabaseServer());		
		
		if( c.getUseEmbeddedHttpServer() == null ) {
			String value = System.getProperty(PlinthosNodePropertyNames.USE_EMBEDDED_HTTP_SERVER);
			c.setUseEmbeddedHttpServer(Boolean.valueOf(value));
		}
		logger.info("using internal HTTP server: " + c.getUseEmbeddedHttpServer());		
		
		if( c.getTaskClasspath() == null ) {
			c.setTaskClasspath(System.getProperty(PlinthosNodePropertyNames.TASK_CLASSPATH));
		}
		logger.info("common classpath for tasks: " + c.getTaskClasspath());		
	}
}
