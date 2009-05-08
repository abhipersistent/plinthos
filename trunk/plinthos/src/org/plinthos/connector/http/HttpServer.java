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
package org.plinthos.connector.http;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

public class HttpServer {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(HttpServer.class);
	
	private static final int DEFAULT_PORT = 8080;
	
	private int port = DEFAULT_PORT;
	
	public HttpServer() {
	
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public void start() {
		//TODO: HttpServer vs Server
	    Server server = new Server();
	    Connector connector=new SocketConnector();
	    connector.setPort(port);
	    server.setConnectors(new Connector[]{connector});
	    
	    ServletHandler handler=new ServletHandler();
	    server.setHandler(handler);

	    
	    handler.addServletWithMapping("org.plinthos.connector.http.HttpServer$RootServlet", "/");	    
	    
	    handler.addServletWithMapping("org.plinthos.connector.http.UrlGatewayServlet", "/plinthos/test");
	    handler.addServletWithMapping(
	    		new ServletHolder(new XmlRpcGatewayServlet()), 
	    		"/plinthos/gateway");
	    
	    try {
	    	server.start();
	    }
	    catch(Exception e) {
	    	throw new RuntimeException(e);
	    }
	}
	
	public void shutdown() {
		
	}
	
	public static class RootServlet extends HttpServlet
	{
		private static final long serialVersionUID = -1397617509572585771L;

		protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	    	throws ServletException, IOException
	    {
	        response.setContentType("text/html");
	        response.setStatus(HttpServletResponse.SC_OK);

	        response.addHeader("Pragma", "no-cache");
	        response.addHeader("Expires", "-1");
	        response.addHeader("Cache-Control", "no-cache");
	        
	        Date date = new Date();
	        response.getWriter().println(
	        		"<html><body>PlinthOS, " + date.toString() + "</body></html>");
	    }
	}
	
}
