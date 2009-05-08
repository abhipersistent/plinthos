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
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.plinthos.core.gateway.PlinthosGateway;
import org.plinthos.shared.gateway.commands.admin.PingRequest;
import org.plinthos.shared.gateway.commands.common.Response;
import org.plinthos.shared.gateway.commands.request.CancelRequest;
import org.plinthos.shared.gateway.commands.request.FindRequest;
import org.plinthos.shared.gateway.mapping.CommandXmlMapping;

public class UrlGatewayServlet  extends HttpServlet {

	private static final long serialVersionUID = -5544049561932733100L;
	
	private PlinthosGateway gateway = new PlinthosGateway();

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		String action = request.getParameter("action");
		String requestId = request.getParameter("requestId");
		String correlationId = request.getParameter("correlationId");
		String message = request.getParameter("message");
		
		Response plinthosResponse = null;
		String responseAsXml = null;		
		
		if( "cancel".equalsIgnoreCase(action) ) {
			CancelRequest r = new CancelRequest();
			r.setRequestId(requestId);
			
			plinthosResponse = gateway.execute(r);
		}
		else if( "find".equalsIgnoreCase(action) ) {
			FindRequest r = new FindRequest();
			r.setRequestId(requestId);
			r.setCorrelationId(correlationId);
			r.setIncludeRequestData(true);
			r.setIncludeResponseData(true);
			plinthosResponse = gateway.execute(r);
		}
		else if( "ping".equalsIgnoreCase(action) ) {
			PingRequest r = new PingRequest();
			r.setMessage(message);
			plinthosResponse = gateway.execute(r);
		}
		else if( "submit".equalsIgnoreCase(action) ) {
			plinthosResponse = new Response();
			plinthosResponse.setResponseStatus("ERROR");
			plinthosResponse.setResponseStatusMessage("Unsupported action: " + action);
		}
		else {
			plinthosResponse = new Response();
			plinthosResponse.setResponseStatus("ERROR");
			plinthosResponse.setResponseStatusMessage("Unknown action: " + action);
		}
		
		CommandXmlMapping xmlMapping = new CommandXmlMapping();
		responseAsXml = xmlMapping.toXML(plinthosResponse);
		
		response.setContentType("text/xml");
		
		PrintWriter out = response.getWriter();
		
		out.print( responseAsXml );
		out.flush();
		out.close();
	}

	
	
}
