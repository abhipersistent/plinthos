package org.plinthos.connector.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.plinthos.core.gateway.PlinthosGateway;

public class XmlRpcGatewayServlet extends HttpServlet{

	private static final long serialVersionUID = 69592122110818708L;

	private PlinthosGateway gateway = new PlinthosGateway();
	
	
	public XmlRpcGatewayServlet() {
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		String xmlData = IOUtils.toString(request.getInputStream(), "UTF-8");
		
		String responseData = executeCommand(xmlData);
		
		response.setContentType("text/xml");
		
		PrintWriter out = response.getWriter();
		
		out.print( responseData );
		out.flush();
		out.close();
	}
	
	private String executeCommand(String xmlRequest) {
		return gateway.executeXml(xmlRequest);
	}
}
