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
package org.plinthos.core.service;

import java.util.Properties;

import org.plinthos.core.framework.Constants;
import org.plinthos.core.model.SystemConfigurationProperty;


/**
 * This interface loads the system configuration from
 * persistent store and makes it available for application.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public interface ConstantsManager {
	
	/**
	 * Loads the system configuration from the persistent store 
	 * 
	 * The static fields in Constants class are updated by this class.
	 * 
	 * @see Constants
	 * @throws PlinthosException
	 */
	public void loadSystemProperties();
	
	public void initSystemProperties(Properties props);
	
	public SystemConfigurationProperty findSystemConfiguration(
			String systemConfigurationPropertyName);
	
}
