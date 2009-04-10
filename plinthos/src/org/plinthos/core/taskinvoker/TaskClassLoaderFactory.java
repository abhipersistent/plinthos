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
	
	public synchronized ClassLoader getClassLoaderForTask(String taskId) {
		ClassLoader classLoaderForTask = cachedClassLoaders.get(taskId);
		
		if( classLoaderForTask == null ) {
			logger.info("creating new ClassLoader for taskId: " + taskId);
			classLoaderForTask = createNewClassLoader(taskId);
			cachedClassLoaders.put(taskId, classLoaderForTask);
		}
		
		return classLoaderForTask;
	}
	
	private ClassLoader createNewClassLoader(String taskId) {
		TaskResourceUrlsFinder plinthosUtil = new TaskResourceUrlsFinder();
		URLClassLoader classLoaderForTask = URLClassLoader.newInstance(
				plinthosUtil.getUrls(taskId) ); 
		return classLoaderForTask;
	}
}
