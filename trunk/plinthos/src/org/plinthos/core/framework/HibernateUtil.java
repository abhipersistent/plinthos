/*
 * PlinthOS, Open Source Multi-Core and Distributed Computing.
 * Copyright 2003-2009, Emptoris Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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
