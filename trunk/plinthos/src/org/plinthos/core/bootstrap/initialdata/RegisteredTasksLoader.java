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
package org.plinthos.core.bootstrap.initialdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.plinthos.core.bootstrap.environment.PlinthosEnvironment;
import org.plinthos.core.bootstrap.environment.PlinthosEnvironmentHolder;
import org.plinthos.core.model.RegisteredTask;
import org.plinthos.core.service.ServiceFactory;
import org.plinthos.core.service.TaskRegistry;

public class RegisteredTasksLoader {

	private static final Logger logger = Logger.getLogger(RegisteredTasksLoader.class);
	
	private TaskRegistry taskRegistry;
	
	public RegisteredTasksLoader() {
		taskRegistry = ServiceFactory.getInstance().getTaskRegistry();
	}
	
	public List<RegisteredTask> run() {
		
    	PlinthosEnvironment plinthosConfig = 
    		PlinthosEnvironmentHolder.getInstance().getConfig();
    	
    	if( plinthosConfig == null ) {
    		String msg = "Plinthos configuration is not available - can't load default registered tasks.";
    		logger.error(msg);
    		throw new RuntimeException(msg);
    	}
		
		String tasksFile = plinthosConfig.getRegisteredTasksFile();

		if( tasksFile == null ) {
			//TODO: add a more informative message
			logger.info("skipping loading of registered tasks - file is not defined");
			return null;
		}
		List<RegisteredTask> tasks = loadTasksFromUrl(tasksFile);
		
		getTaskRegistry().initTasks(tasks);

		return tasks;
	}
	
	private List<RegisteredTask> loadTasksFromUrl(String fileName) {

		List<RegisteredTask> tasks = new ArrayList<RegisteredTask>();
		
		try {
			File f = new File(fileName);
			FileReader fReader = new FileReader(f);
			BufferedReader bReader = new BufferedReader(fReader); 

			String line = null;
			while( ( line = bReader.readLine() ) != null ) {
				// skip empty lines
				if( line.trim().length() == 0 ) {
					continue;
				}
				
				String[] tokens = line.split(",");
				if( tokens.length != 3 ) {
					logger.info("Invalid line in task registration file: [" + line + "]");
					continue;
				}
				
				RegisteredTask vo = new RegisteredTask();
				vo.setTaskType(tokens[0]);
				vo.setExecutorClass(tokens[1]);
				vo.setEtcSupported(Boolean.parseBoolean(tokens[2]));
				tasks.add(vo);
				
			}
		}
		catch(Exception e) {
			logger.error("Failed to load task registration from file: '" + fileName + "', Error: ", e);
			throw new RuntimeException(e);
		}
		
		return tasks;
	}
	
	private TaskRegistry getTaskRegistry() {
		return taskRegistry;
	}

	public void setTaskRegistry(TaskRegistry taskRegistry) {
		this.taskRegistry = taskRegistry;
	}
	
	
}
