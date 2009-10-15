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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.plinthos.core.bootstrap.environment.PlinthosEnvironment;
import org.plinthos.core.bootstrap.environment.PlinthosEnvironmentHolder;
import org.plinthos.core.framework.Constants;


/**
 * Utility class to obtain a list of URL for task resources.
 *
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class TaskResourceUrlsFinder {

	private static final Logger logger = Logger.getLogger(TaskResourceUrlsFinder.class);
	
	private static final String PATH_SEPARATOR = System.getProperty("file.separator");
	
	private String PLINTHOS_DIRECTORY = null; 

	private PlinthosEnvironment config = null;
	
	public TaskResourceUrlsFinder() {
		config = PlinthosEnvironmentHolder.getInstance().getConfig();
		PLINTHOS_DIRECTORY = config.getPlinthosDir();
	}
	
	public URL[] getUrls(String serviceName) {
		
		// Since the application runs within JBoss
		// this is going to be JBOSS_HOME\bin (on Windows)
		File plinthosDir = new File(PLINTHOS_DIRECTORY);
		
		StringBuilder b = new StringBuilder(plinthosDir.getPath());
		b.append(PATH_SEPARATOR).append(Constants.SERVICES_LIBRARY);
		b.append(PATH_SEPARATOR).append(serviceName);
		
		File plinthosLibDir = new File(b.toString());
		
		String[] fNames = plinthosLibDir.list();
		
		List<URL> urls = new ArrayList<URL>();
		
		int i=0;
		StringBuilder buffer = new StringBuilder();
		for (String fName : fNames) {
			if (fName.endsWith("jar")) {
				
				buffer.delete(0, buffer.length());
				buffer.append(plinthosLibDir.getPath()).append(PATH_SEPARATOR);
				buffer.append(fName).append("!/");
				String fPath = buffer.toString();
		        logger.debug("adding jar: " + fPath);		
		        String urlText = "jar:file:" + fPath;
				try {
					urls.add( new URL(urlText) );
					i++;
				} catch (MalformedURLException logOnly) {
					logger.error("Error while parsing url: '" + urlText + "', error: ", logOnly);
				}
			}
		}
		

		urls.addAll( getDefaultClasspathURLs( config.getTaskClasspath() ) );
		
    	return urls.toArray(new URL[urls.size()]);
	}
	
	//TODO: refactor this code to avoid duplication
	public List<URL> getDefaultClasspathURLs( String classpath) {
		List<URL> urlList = new ArrayList<URL>();
		if( classpath != null && classpath.length() > 0 ) {
			String[] parts = classpath.split(";");
			for(String path : parts) {
				try {
					URL url = null;

					if( path.endsWith("jar") ) {
						path = path + "!/";
						url = new URL("jar:file:" + path);
					}
					else {
						File f = new File(path);
						url = f.toURI().toURL();
					}

					logger.info("adding classpath url: '" + url.toExternalForm() + "'");
					urlList.add( url );
				}
				catch(Exception logOnly) {
					logger.error("Error while parsing task classpath: claspath=" + 
							classpath + ", path=" + path + ", error: ", logOnly);
				}
			}
		}
		
		return urlList;
	}
	
}
