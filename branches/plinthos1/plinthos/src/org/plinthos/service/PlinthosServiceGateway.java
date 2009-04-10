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
package org.plinthos.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.plinthos.core.model.PlinthosRequestStatus;
import org.plinthos.plugin.PlinthosXmlRequest;

import com.thoughtworks.xstream.XStream;

public class PlinthosServiceGateway extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8017973607482976442L;
	
	private final Logger log = Logger.getLogger(PlinthosServiceGateway.class);

	/**
	 * Constructor of the object.
	 */
	public PlinthosServiceGateway() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>Welcome to PlinthOS</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method.<BR/>");
		out.println("You should use the POST method to submit a service request.");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		 //Start timing
		 long t0 = System.currentTimeMillis();
		 		
		log.debug("Got a request on:"+t0);
		
		String xmlData = getFromInputStream((InputStream) request.getInputStream());
		
		XStream xstream = new XStream();
		
		PlinthosXmlRequest xmlRequest = (PlinthosXmlRequest) xstream.fromXML(xmlData);
		
		String responseStatus = submit(xmlRequest);
		
		// Can you make it simpler than that?  :-)
		response.setContentType("text/xml");
		
		PrintWriter out = response.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<PlinthosService>");
		out.print("  <Response>");
		out.print(responseStatus);		
		out.println ("</Response>");
		out.println("</PlinthosService>");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put all your initialization code here
	}

	private String submit(PlinthosXmlRequest xml) {
        
		// TODO: This will attempt to get a connection once.
		// Clearly, we can improve this by retrying a configurable number of times ...
		Connection jdbc = getConnection();

		if ( null == jdbc ) {
			
			//Oops, we didn't get a connection!
			log.error("Could not obtain a JDBC connection from the database!");
			
			return  PlinthosRequestStatus.JDBC_ERROR;

		} else {
			
			// Clearly, this could be rewritten more efficiently by using a PreparedStatement
			// This is just an example, we are not after an award here ... :-)
        	Statement s;
        	
			try {
				
				s = jdbc.createStatement();
				
			} catch (SQLException e) {
				
				log.error("A SQLException occured while creating a JDBC Statement:\n"+e.getMessage());
				log.error("Stack trace follows:\n");
				e.printStackTrace();
				
				return PlinthosRequestStatus.JDBC_ERROR;
			}

	        String sql = buildSql(xml);
	        
	        try {
	        	
				s.executeUpdate(sql);
				
			} catch (SQLException e) {
				
				log.error("A SQLException occured while executing a JDBC update statement:\n"+e.getMessage());
				log.error("Stack trace follows:\n");
				e.printStackTrace();
				
				return PlinthosRequestStatus.JDBC_ERROR;
			}
	        
	        try {
	        	
				s.close ();

	        } catch (SQLException e) {
				log.warn("A SQLException occured while closing a JDBC Statement:\n"+e.getMessage());
				log.warn("Stack trace follows:\n");
				e.printStackTrace();
			}
	        
	        System.out.println("Submitted request on: "+System.currentTimeMillis());
	        
			return PlinthosRequestStatus.SUBMITTED;
		}
	}
	
	private String buildSql(PlinthosXmlRequest xml) {
		
		StringBuilder sql = new StringBuilder("INSERT INTO request ");
		
		sql.append("(USER_ID, TEMPLATE_PARAMS, STATUS, PRIORITY, EXPIRATION, SUBMISSION_TIME, REQUEST_TYPE) ");
		sql.append("VALUES ('");
		
		// set the ID of the user who submitted the request
		// TODO: Here we could add authorization but for most purposes simple monitoring should suffice
		sql.append(xml.getUserId()).append("','");
		
		// set the request parameters
		sql.append(xml.getRequestXml()).append("','");
		
		// set the status
		sql.append("SUBMITTED").append("',");
		
		//Set the priority
		sql.append(xml.getPriority()).append(",");
		
		// If the expiration time is not set, we can resort to a default value
		// TODO: make the default value configurable
		String exp = xml.getExpiration();
		if (exp != null && exp.trim().length() > 0) {
			sql.append(exp).append(",");
		} else {
			sql.append("(SELECT DATE_ADD(NOW(), INTERVAL 5 HOUR)),");
		}
		
		// Using NOW() adopts the time frame of the server
		// Alternatively, we could use the submission time from the request (client time frame)
		// For most purposes, the difference will not be significant. However, if it is then you
		// should use the time frame that better fits your purposes.
		sql.append("NOW()").append(",'");
		
		sql.append(xml.getType()).append("')");		
		
		return sql.toString();
	}
	
    private Connection getConnection() {
		Connection jdbc = null;

        // TODO: DO NOT RUN LIKE THAT IN A PRODUCTION ENVIRONMENT
		//       THIS IS JUST AN EXAMPLE!
		String userName = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/plinthos";
        
        try {
			Class.forName ("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
        try {
			jdbc = DriverManager.getConnection (url, userName, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        System.out.println ("Database connection established");

        return jdbc;
    }
        
    /**
     * This method allows us to read the InputStream and create a String, which in turn can be
     * written into a file.
     *
     *
     * @param stream $description$
     *
     * @return String
     * @throws IOException
     */
    private String getFromInputStream (InputStream stream) throws IOException {

        byte[] buf = new byte [8 * 1024];
        
        StringBuilder sbuf = new StringBuilder();
        
        int result = 0;

        while (result != -1) {
        	
            result = stream.read (buf, 0, buf.length);

            if (result != -1) {
            	
                sbuf.append (new String (buf, 0, result));
            }
        }

        return sbuf.toString ();
    }
}
