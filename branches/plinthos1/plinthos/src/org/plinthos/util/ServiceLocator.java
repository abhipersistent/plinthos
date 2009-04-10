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

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * TODO: Add documentation
 *  
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class ServiceLocator {
	private static ServiceLocator theInstance = null;
	
	private InitialContext context = null;
	
	private ServiceLocator() {
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			throw new RuntimeException("Naming exception while initializing JNDI context", e);
		}
	}
	
	public static ServiceLocator getInstance() {
		if ( theInstance == null ) {
			synchronized(ServiceLocator.class) {
				if ( theInstance == null ) {
					theInstance = new ServiceLocator();
				}
			}
		}
		return theInstance;
	}
	
	public Object getFromJNDI(String jndiName) {
		try {
			return context.lookup(jndiName);
		} catch (NamingException e) {
			throw new RuntimeException("Failed to lookup object with name " + jndiName, e);
		}
	}
	
	public void list() throws NamingException {
		System.out.println(context.getEnvironment().toString());
	}

}
