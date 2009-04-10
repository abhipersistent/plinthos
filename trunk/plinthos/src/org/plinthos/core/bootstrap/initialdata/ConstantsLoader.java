package org.plinthos.core.bootstrap.initialdata;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.plinthos.core.bootstrap.environment.PlinthosEnvironment;
import org.plinthos.core.bootstrap.environment.PlinthosEnvironmentHolder;
import org.plinthos.core.service.ConstantsManager;
import org.plinthos.core.service.ServiceFactory;

public class ConstantsLoader {

	private static final Logger logger = Logger.getLogger(ConstantsLoader.class);
	
	private ConstantsManager m;
	
	public ConstantsLoader() {
		this.m = ServiceFactory.getInstance().getConstantsManager();
	}
	
	public void setConfigurationService(ConstantsManager m) {
		this.m = m;
	}
	
	public ConstantsManager getSystemConfigurationManager() {
		return m;
	}
	
	public Properties run() {
		
    	PlinthosEnvironment plinthosConfig = 
    		PlinthosEnvironmentHolder.getInstance().getConfig();
    	
    	if( plinthosConfig == null ) {
    		String msg = "Plinthos configuration is not available - can't load default system properties.";
    		logger.error(msg);
    		throw new RuntimeException(msg);
    	}
    	
		String propertiesFile = plinthosConfig.getInitialAppPropertiesFile();

		Properties props = loadInitialProperties(propertiesFile);

		m.initSystemProperties(props);
		
		m.loadSystemProperties();
		
		return props;
	}

	private Properties loadInitialProperties(String propertiesFile) {
		
		Properties props = new Properties();
		
		if( propertiesFile != null ) {
			
			try {
				File f = new File(propertiesFile);
				URL url = f.toURI().toURL();
				InputStream inStream = url.openStream();
				props.load(inStream);
			}
			catch(Exception e) {
				logger.error("Failed to load configuration from file: '" + propertiesFile + "', Error: ", e);
				throw new RuntimeException(e);
			}
			
		}
		
		return props;
	}
	
}
