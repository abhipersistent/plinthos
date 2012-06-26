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
package org.plinthos.core.taskinvoker;

import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class TaskClassLoaderFactory {

	private static final Logger logger = Logger.getLogger(TaskClassLoaderFactory.class);
	
	private static TaskClassLoaderFactory instance = new TaskClassLoaderFactory();
	
	private Map<String, ClassLoader> cachedClassLoaders = new HashMap<String, ClassLoader>();
	
	private TaskClassLoaderFactory() {
		
	}
	
	public static TaskClassLoaderFactory getInstance() {
		return instance;
	}
	
	public synchronized ClassLoader getClassLoaderForTask(String taskLibLocation) {
		ClassLoader classLoaderForTask = cachedClassLoaders.get(taskLibLocation);
		
		if( classLoaderForTask == null ) {
			logger.info("creating new ClassLoader for taskId: " + taskLibLocation);
			classLoaderForTask = createNewClassLoader(taskLibLocation);
			cachedClassLoaders.put(taskLibLocation, classLoaderForTask);
		}
		
		return classLoaderForTask;
	}
	
	private ClassLoader createNewClassLoader(String taskId) {
		TaskResourceUrlsFinder plinthosUtil = new TaskResourceUrlsFinder();
		URLClassLoader classLoaderForTask = URLClassLoader.newInstance(
				plinthosUtil.getUrls(taskId), Thread.currentThread().getContextClassLoader()); 
		return classLoaderForTask;
	}
}
