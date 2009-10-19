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

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.plinthos.core.bootstrap.environment.PlinthosEnvironmentHolder;
import org.plinthos.core.framework.Constants;
import org.plinthos.core.service.ServiceFactory;
import org.plinthos.shared.plugin.BackgroundTask;
import org.plinthos.shared.plugin.BackgroundTaskContext;

public class BackgroundTaskInvoker {

	private static final Logger log = Logger.getLogger(BackgroundTaskInvoker.class);
	
	private Set<BackgroundTask> tasksImpls = new HashSet<BackgroundTask>();
	
	private Properties properties = new Properties();  

	private static BackgroundTaskInvoker backgroundTaskInvoker;

	public static BackgroundTaskInvoker getInstance() {
		if (backgroundTaskInvoker == null) {
			backgroundTaskInvoker = new BackgroundTaskInvoker();
		}
		return backgroundTaskInvoker;
	}
	
	public void startBackgroundTasks() {

		String executorClassName = null;
		String executorLocation = null;
		BackgroundTaskContext ctx=null;
		ClassLoader originalContextClassLoader =  Thread.currentThread().getContextClassLoader();
		try {
			ClassLoader taskClassLoader = createNewClassLoader();
			Thread.currentThread().setContextClassLoader(taskClassLoader);	
			
			try {
				properties.load(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(Constants.BACKGROUND_PROPERTY_FILE_NAME));
			} catch (NullPointerException ex) {
				// if property file not available then return
				if (properties.isEmpty()) {
					log.info(Constants.BACKGROUND_PROPERTY_FILE_NAME + " not present");
					return;
				}
			}
	    	
	    	String classNames = properties.getProperty("executor_class");
	    	String locations = properties.getProperty("executor_location");
			
	    	if (classNames == null || locations == null
					|| "".equals(classNames.trim())
					|| "".equals(locations.trim())) {
				log.error(Constants.BACKGROUND_PROPERTY_FILE_NAME + " not contain proper values for properties" 
						+ " executor_class:" + classNames + ",executor_location:" + locations);
				return;
			}
			String[] executorClassNames = classNames.split(",");
			String[] executorLocations = locations.split(",");
    	
			for (int i = 0; i < executorClassNames.length; i++) {
				
				executorClassName=executorClassNames[i];
				executorLocation=executorLocations[i];
		    	ctx = new BackgroundTaskContextImpl();
		    	ctx.setRequestManager(ServiceFactory.getInstance().getRequestManager());
		    	
		    	if( executorLocation == null || executorLocation.trim().length() == 0 ) {
		    		String msg = "background task: " + executorClassName + ", executorLocation is null or empty string; please fix your configuration."; 
		    		log.error(msg);
		    		throw new RuntimeException(msg);
		    	}
		    	
		    	loadAndStartTask( executorClassName, executorLocation, ctx);
		    	log.info(" Start Background task: executor_class= "+ executorClassName + " executor_location=" + executorLocation);	    		
			}
			
	    } 
	    catch (Throwable t) {
	    	log.error("Unable to start backgorund tasks: ", t);
	    }finally {
			// restore original context class loader for this thread
			Thread.currentThread().setContextClassLoader(originalContextClassLoader);
		}
	}


	private void loadAndStartTask(String taskClassName,
			String executorLocation, BackgroundTaskContext ctx) 
		throws Exception {
		
		ClassLoader originalContextClassLoader = 
			Thread.currentThread().getContextClassLoader();

		TaskClassLoaderFactory tclFactory = TaskClassLoaderFactory.getInstance();
		
		try {
			/*
			 * Task implementation should be taking it into account. For example
			 * if it relies on XStream library to parse request data it should 
			 * configure XStream to use task classloader. Without it XStream will
			 * only be using PlinthOS classloader that is not aware of any 
			 * classes related to task implementation.
			 */
			ClassLoader taskClassLoader = tclFactory.getClassLoaderForTask(executorLocation);
			/*
			 * Some of the code that task is using may rely on context class loader instead 
			 * of class loader of the class.
			 */
			Thread.currentThread().setContextClassLoader(taskClassLoader);		
		
			@SuppressWarnings("unchecked")
	    	Class<? extends BackgroundTask> taskClass = 
	    		(Class<? extends BackgroundTask>) taskClassLoader.loadClass(taskClassName);
			BackgroundTask taskImpl = null;

			taskImpl = (BackgroundTask)taskClass.newInstance();
	    	taskImpl.setContext(ctx);
	    	taskImpl.init();
	    	taskImpl.start();
	    	
	    	tasksImpls.add(taskImpl);
		}
	    catch( Throwable t) {
	    	log.error("Failed to start bakgorund task: taskClassName=" + taskClassName + ". ", t);
	    }
		finally {
			// restore original context class loader for this thread
			Thread.currentThread().setContextClassLoader(originalContextClassLoader);
		}
		
	}
	
	public void stopBackgroundTasks() {
		for (BackgroundTask taskImpl : tasksImpls) {
			taskImpl.stop();
		}
	}
	
	private ClassLoader createNewClassLoader() {
		
		String config = PlinthosEnvironmentHolder.getInstance().getConfig().getTaskClasspath();
		TaskResourceUrlsFinder urlsFinder = new TaskResourceUrlsFinder();
		List<URL> urls = urlsFinder.getDefaultClasspathURLs(config);

		URLClassLoader classLoaderForTask = URLClassLoader.newInstance(urls
				.toArray(new URL[urls.size()]));
		return classLoaderForTask;
	}	
}
