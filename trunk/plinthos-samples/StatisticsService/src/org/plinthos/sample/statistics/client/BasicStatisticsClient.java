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
package org.plinthos.sample.statistics.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Random;

import org.plinthos.sample.statistics.data.BasicStatisticsRequest;
import org.plinthos.shared.gateway.commands.request.SubmitRequest;
import org.plinthos.shared.gateway.mapping.CommandXmlMapping;

import com.thoughtworks.xstream.XStream;

/**
 * This class provides the steps for accessing the service from client.
 * If you would like to submit directly a request into the database then 
 * the first argument should be the word "direct".
 * 
 * Alternatively, you can use the word "http" to submit the request via HTTP. 
 * There is a default proxy for XML-RPC that uses HTTP.
 *
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @param request
 * @return
 */
public class BasicStatisticsClient {

	/**
	 * Submit a set of data to PlinthOS for basic statistical processing.
	 * 
	 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		int loop = 1;
		
		if (args.length > 0) {
			if (args[1] != null) {
				loop = getLoop(args[1]);
			}
			
			if (args[0].equalsIgnoreCase("direct")) {
				Connection jdbc = null;
		
		        String userName = "root";
		        String password = "";
		        String url = "jdbc:mysql://localhost:3306/plinthos";
		        Class.forName ("com.mysql.jdbc.Driver").newInstance();
		        jdbc = DriverManager.getConnection (url, userName, password);
		        System.out.println ("Database connection established");
		
		        for (int i=0; i<loop;i++) {
			        String requestXml = getRequest();
			        
			        Statement s = jdbc.createStatement();
			        s.executeUpdate ("INSERT INTO request "+
			        "(USER_ID, TEMPLATE_PARAMS, STATUS, PRIORITY, EXPIRATION, SUBMISSION_TIME, REQUEST_TYPE) "+
			        " VALUES ('babis','"+requestXml+"','SUBMITTED', "+(i+1)+", (SELECT DATE_ADD(NOW(), INTERVAL 5 HOUR)), NOW(),'Basic-Statistics')");
			        s.close ();
			        
			        System.out.println("Submitted:\n"+requestXml);
		        }
		        
			} else if (args[0].equalsIgnoreCase("http")) {
	
		        for (int i=0; i<loop;i++) {
					// Create the XML request
					SubmitRequest xml = new SubmitRequest();
					xml.setUserId("babis");
					xml.setRequestData(getRequest());
					xml.setPriority(10);
					xml.setCorrelationId("CLIENT-ID-1");
					xml.setType("Basic-Statistics");
					
					CommandXmlMapping xmlMapping = new CommandXmlMapping(); 
					String xmlData = xmlMapping.toXML(xml);
					
					// Post it
				    //URL url = new URL("http://localhost:8080/plinthos/service/PlinthosServiceGateway");
					URL url = new URL("http://localhost:8080/plinthos/gateway");
					
				    java.net.HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
				    // Sets the request method to POST, and enable to send data
				    con.setRequestMethod("POST");
				    con.setDoOutput(true);
				    con.setDoInput(true);
				    con.setUseCaches(false);
		
				    // Sets the default Content-type and content length for POST requests
				    con.setRequestProperty( "Content-type", "application/x-www-form-urlencoded" );
				    //con.setRequestProperty( "Content-length", Integer.toString(postData.length()));
		
				    // Gets the output stream and POSTs data
				    OutputStream POSTStream = con.getOutputStream();
				    OutputStreamWriter POSTWriter = new OutputStreamWriter(POSTStream);
				    POSTWriter.write(xmlData);
				    POSTWriter.flush();
				    POSTWriter.close();
				    POSTWriter = null;
		
				    InputStreamReader in = new InputStreamReader (con.getInputStream());
				    BufferedReader reader = new BufferedReader(in);
		
				    String inputLine;
		
				    //Reads data
				    while ((inputLine = reader.readLine()) != null) {
				    
				        System.out.println(inputLine);
				    }
				    
				    in.close();
				    
				    if ( con != null ) {
				    	con.disconnect();
				    }
				}
			}
		} else {
			System.err.print("You must use at least one argument. Acceptable values are:\n    direct\n    http");
		}
	}

	private static int getLoop(String val) {
		int i = Integer.parseInt(val);
		
		return i > 0 ? i : 1;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	private static String getRequest() throws Exception {

		//Build the instance of BasicStatisticsRequest
		BasicStatisticsRequest request = new BasicStatisticsRequest("test data {&gt;}");
		
		double[] values = new double[10];
		
		Random rand = new Random();
		for (int i=0; i < 10; i++) {
			values[i] = rand.nextGaussian();
		}
		
		request.setValues(values);
		
		XStream xstream = new XStream();
		
		return xstream.toXML(request);
	}
}
