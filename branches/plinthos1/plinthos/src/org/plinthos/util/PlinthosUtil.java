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
package org.plinthos.util;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * This class contains utility methods those can be used accross the ZPort application.
 *
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class PlinthosUtil {

	public static final String PATH_SEPARATOR = System.getProperty("file.separator");
	public static final String USER_DIRECTORY = System.getProperty("user.dir"); 


	/**
	 * Returns the Wrapper Object based on the data type
	 *
	 *
	 * @param type data type
	 * @param value initial value for wrapper object
	 *
	 * @return Object
	 *
	 */
	public static Object getWrapperObject(final String type, final String value) {

		if (type.equals("int")) {
			return new Integer(value);
		} else if (type.equals("double")) {
			return new Double(value);
		} else if (type.equals("long")) {
			return new Long(value);
		} else if (type.equals("float")) {
			return new Float(value);
		} else if (type.equals("boolean")) {
			return new Boolean(value);
		} else if (type.equals("java.lang.String")) {
			return value;
		} else if (type.equals("java.lang.Integer")) {
			return new Integer(value);
		} else if (type.equals("java.awt.Image")) {
			return getImage(value);
		}

		return null;
	}

	/**
	 * Method getImage $description$
	 *
	 *
	 * @param fileName $description$
	 *
	 * @return Image
	 *
	 */
	private static Image getImage(String fileName) {

		Image image = Toolkit.getDefaultToolkit().createImage(fileName);
		MediaTracker traker = new MediaTracker(new Panel());

		traker.addImage(image, 0);

		try {
			traker.waitForID(0);
			return image;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	public static URL[] getUrls(String serviceName) {
		
		// Since the application runs within JBoss
		// this is going to be JBOSS_HOME\bin (on Windows)
		File userDir = new File(USER_DIRECTORY);
		
		StringBuilder b = new StringBuilder(userDir.getParent());
		b.append(PATH_SEPARATOR).append(Constants.SERVICES_LIBRARY);
		b.append(PATH_SEPARATOR).append(serviceName);
		
		File plinthosLibDir = new File(b.toString());
		
		String[] fNames = plinthosLibDir.list();
		
		URL[] urls = new URL[fNames.length];
		
		int i=0;
		StringBuilder buffer = new StringBuilder();
		for (String fName : fNames) {
			if (fName.endsWith("jar")) {
				
				buffer.delete(0, buffer.length());
				buffer.append(plinthosLibDir.getPath()).append(PATH_SEPARATOR);
				buffer.append(fName).append("!/");
				
				String fPath = buffer.toString();
				
				try {
					urls[i] = new URL("jar:file:" + fPath);
					i++;
				} catch (MalformedURLException eX) {
					eX.printStackTrace();
				}
			}
		}
		
    	return urls;
	}
}
