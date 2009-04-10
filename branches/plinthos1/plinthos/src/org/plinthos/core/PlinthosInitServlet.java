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
package org.plinthos.core;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.plinthos.config.process.ProcessUtil;
import org.plinthos.core.queue.RequestProcessor;


/**
 * Initialization of the system configuration
 * 
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class PlinthosInitServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1182464033432321487L;

	private static final Logger log = Logger.getLogger(PlinthosInitServlet.class);

	// Initialize global variables

	/**
	 * Method init $description$
	 * 
	 * 
	 * @throws ServletException
	 *             $description$
	 * 
	 */
	public void init() throws ServletException {

		log.debug("Initializing PlinthOS");
		// Load the system properties.
		try {
			ProcessUtil.getSystemConfiguration().loadSystemProperties();
		} catch (Exception e) {
			log.error("Failed to initialize PlinthOS", e);
			throw new ServletException("Failed to initialize PlinthOS", e);
		}
		/*
		 * Create the request processor object to process the queued requests
		 */
		RequestProcessor requestProcessor = new RequestProcessor();
		requestProcessor.start();
		log.info("Leaving from init method   ");
	}
}