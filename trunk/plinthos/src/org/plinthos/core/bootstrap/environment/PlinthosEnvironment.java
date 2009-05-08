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
package org.plinthos.core.bootstrap.environment;

public class PlinthosEnvironment {

	private String plinthosDir = null;
	
	private String initialAppPropertiesFile = null; 
	
	private String registeredTasksFile = null;

	private String dbType = null;
	
	private String hbrConfigurationFile = null;
	
	private Boolean useEmbeddedDatabaseServer = null;
	
	private Boolean useEmbeddedHttpServer = null;
	
	private String taskClasspath = null;
	
	private int httpPort = -1;
	
	public PlinthosEnvironment() {
	}
	
	public String getPlinthosDir() {
		return plinthosDir;
	}
	
	public void setPlinthosDir(String dir) {
		this.plinthosDir = dir;
	}
	
	public String getInitialAppPropertiesFile() {
		return this.initialAppPropertiesFile;
	}
	
	public String getRegisteredTasksFile() {
		return this.registeredTasksFile;
	}

	public void setInitialAppPropertiesFile(String initialAppPropertiesFile) {
		this.initialAppPropertiesFile = initialAppPropertiesFile;
	}

	public void setRegisteredTasksFile(String registeredTasksFile) {
		this.registeredTasksFile = registeredTasksFile;
	}

	public String getHbrConfigurationFile() {
		return hbrConfigurationFile;
	}

	public void setHbrConfigurationFile(String hbrConfigurationFile) {
		this.hbrConfigurationFile = hbrConfigurationFile;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	public Boolean getUseEmbeddedDatabaseServer() {
		return useEmbeddedDatabaseServer;
	}

	public void setUseEmbeddedDatabaseServer(Boolean useEmbeddedDatabaseServer) {
		this.useEmbeddedDatabaseServer = useEmbeddedDatabaseServer;
	}

	public Boolean getUseEmbeddedHttpServer() {
		return useEmbeddedHttpServer;
	}

	public void setUseEmbeddedHttpServer(Boolean useEmbeddedHttpServer) {
		this.useEmbeddedHttpServer = useEmbeddedHttpServer;
	}

	public String getTaskClasspath() {
		return taskClasspath;
	}

	public void setTaskClasspath(String taskClasspath) {
		this.taskClasspath = taskClasspath;
	}
	
	
}
