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
package org.plinthos.core.process;

import org.plinthos.core.PlinthosRuntimeException;
import org.plinthos.core.process.impl.RequestManagerImpl;
import org.plinthos.util.ServiceLocator;


/**
 * Provides utility methods to get session beans home and handler objects.
 *
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class ProcessUtil {

	/**
	 * Gets the QueueRequest Manager session bean remote object
	 *
	 * @return RequestManager Handler reference of RequestManager bean
	 * @throws PlinthosRuntimeException  if the specified name doesn't exist.
	 */
	public static RequestManager getRequestManager() throws PlinthosRuntimeException {
		String jndiName = "PlinthOS/"+ RequestManagerImpl.class.getSimpleName() + "/local";
		return (RequestManager) ServiceLocator.getInstance().getFromJNDI(jndiName);
	}
}
