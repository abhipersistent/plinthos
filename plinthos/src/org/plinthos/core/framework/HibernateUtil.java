package org.plinthos.core.framework;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.plinthos.core.bootstrap.environment.PlinthosEnvironment;
import org.plinthos.core.bootstrap.environment.PlinthosEnvironmentHolder;

public class HibernateUtil {

	private static final Logger logger = Logger.getLogger(HibernateUtil.class);
	
	private static SessionFactory sessionFactory;

    static {
        try {
        	/* 
        	 * Discover PlinthOS Environment and use it to load Hibernate 
        	 * configuration. Environment should be configured before the first
        	 * use of this class.
        	 */
        	PlinthosEnvironment plinthosConfig = 
        		PlinthosEnvironmentHolder.getInstance().getConfig();
        	
        	if( plinthosConfig == null ) {
        		String msg = "Plinthos configuration is not available - can't create hibernate session factory.";
        		logger.error(msg);
        		throw new RuntimeException(msg);
        	}
        	
    		String configFile = plinthosConfig.getHbrConfigurationFile();
    		File f = new File(configFile);
    		URL configUrl = f.toURI().toURL();
            sessionFactory = new Configuration().configure(configUrl).buildSessionFactory();
        } catch (Throwable e) {
            logger.error("Initial SessionFactory creation failed.", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
