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
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpServer {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(HttpServer.class);

	public static final int DEFAULT_IDLE_TIMEOUT=30000;
	public static final int DEFAULT_OUTPUT_BUFFER_SIZE=32768;
	public static final int DEFAULT_PORT = 8080;
	public static final int DEFAULT_SECURE_PORT = 8443;
	public static final String DEFAULT_SECURE_SCHEME="https";
	
	private int idleTimeout      = DEFAULT_IDLE_TIMEOUT;
	private int outputBufferSize = DEFAULT_OUTPUT_BUFFER_SIZE;
	private int port             = DEFAULT_PORT;
	private int securePort       = DEFAULT_SECURE_PORT;

	private String startedOn;
	
	public HttpServer() {
		startedOn= java.util.Calendar.getInstance().getTime().toString();
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void start() {
		// Create a basic Jetty server object that will be configured 
		// for multiple	connectors. This is code straight from the Jetty example
		Server server = new Server();

		// HTTP Configuration
		// HttpConfiguration is a collection of configuration information appropriate 
		// for http and https. The default scheme for http is <code>http</code> of course, 
		// as the default for secured http is <code>https</code>. 
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme(DEFAULT_SECURE_SCHEME);
		http_config.setSecurePort(securePort);
		http_config.setOutputBufferSize(outputBufferSize);

		// HTTP connector
		// The first server connector we create is the one for http, passing in
		// the http configuration we configured
		// above so it can get things like the output buffer size, etc. We also
		// set the port (8080) and configure an
		// idle timeout.
		ServerConnector http = new ServerConnector(server,
				new HttpConnectionFactory(http_config));
		http.setPort(port);
		http.setIdleTimeout(idleTimeout);

		// SSL Context Factory for HTTPS and SPDY
		// SSL requires a certificate so we configure a factory for ssl contents
		// with information pointing to what
		// keystore the ssl connection needs to know about. Much more
		// configuration is available the ssl context,
		// including things like choosing the particular certificate out of a
		// keystore to be used.
		/*
			SslContextFactory sslContextFactory = new SslContextFactory();
			sslContextFactory.setKeyStorePath(jetty_home + "/etc/keystore");
			sslContextFactory
					.setKeyStorePassword("OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
			sslContextFactory.setKeyManagerPassword("OBF:1u2u1wml1z7s1z7a1wnl1u2g");
		 */

		// HTTPS Configuration
		// A new HttpConfiguration object is needed for the next connector and
		// you can pass the old one as an
		// argument to effectively clone the contents. On this HttpConfiguration
		// object we add a
		// SecureRequestCustomizer which is how a new connector is able to
		// resolve the https connection before
		// handing control over to the Jetty Server.
		/*
			HttpConfiguration https_config = new HttpConfiguration(http_config);
			https_config.addCustomizer(new SecureRequestCustomizer());
		 */

		// HTTPS connector
		// We create a second ServerConnector, passing in the http configuration
		// we just made along with the
		// previously created ssl context factory. Next we set the port and a
		// longer idle timeout.
		/*
			ServerConnector https = new ServerConnector(server,
					new SslConnectionFactory(sslContextFactory, "http/1.1"),
					new HttpConnectionFactory(https_config));
			https.setPort(8443);
			https.setIdleTimeout(500000);
		 */

		// Here you see the server having multiple connectors registered with
		// it, now requests can flow into the server
		// from both http and https urls to their respective ports and be
		// processed accordingly by jetty. A simple
		// handler is also registered with the server so the example has
		// something to pass requests off to.

		// Set the connectors
		server.setConnectors(new Connector[] { http});

		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);

		handler.addServletWithMapping("org.plinthos.connector.http.HttpServer$RootServlet", "/");

		handler.addServletWithMapping("org.plinthos.connector.http.UrlGatewayServlet",
				"/plinthos/test");
		
		handler.addServletWithMapping(new ServletHolder(new XmlRpcGatewayServlet()), "/plinthos/gateway");

		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void shutdown() {

	}

	public static class RootServlet extends HttpServlet {
		private static final long serialVersionUID = -1397617509572585771L;

		protected void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			response.addHeader("Pragma", "no-cache");
			response.addHeader("Expires", "-1");
			response.addHeader("Cache-Control", "no-cache");

			Date date = new Date();
			response.getWriter().println("<html><body><h1>Welcome to <b>PlinthOS</b></h1>"+
			"<br/><p> The date/time is " + date.toString() + "</p></body></html>");
		}
	}

	/**
	 * @return the idleTimeout
	 */
	public int getIdleTimeout() {
		return idleTimeout;
	}

	/**
	 * @param idleTimeout the idleTimeout to set
	 */
	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	/**
	 * @return the outputBufferSize
	 */
	public int getOutputBufferSize() {
		return outputBufferSize;
	}

	/**
	 * @param outputBufferSize the outputBufferSize to set
	 */
	public void setOutputBufferSize(int outputBufferSize) {
		this.outputBufferSize = outputBufferSize;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the securePort
	 */
	public int getSecurePort() {
		return securePort;
	}

	/**
	 * @return the startedOn
	 */
	public String getStartedOn() {
		return startedOn;
	}

}
