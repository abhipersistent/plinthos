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
package org.plinthos.shared.gateway.mapping;


import org.plinthos.shared.gateway.commands.admin.PingRequest;
import org.plinthos.shared.gateway.commands.admin.PingResponse;
import org.plinthos.shared.gateway.commands.common.Request;
import org.plinthos.shared.gateway.commands.common.Response;
import org.plinthos.shared.gateway.commands.request.CancelRequest;
import org.plinthos.shared.gateway.commands.request.CancelResponse;
import org.plinthos.shared.gateway.commands.request.FindRequest;
import org.plinthos.shared.gateway.commands.request.FindResponse;
import org.plinthos.shared.gateway.commands.request.RequestDetails;
import org.plinthos.shared.gateway.commands.request.SubmitRequest;
import org.plinthos.shared.gateway.commands.request.SubmitResponse;

import com.thoughtworks.xstream.XStream;

public class CommandXmlMapping {
	
	private XStream xStream = null;
	
	public CommandXmlMapping() {
		xStream = new XStream();
		
		/* Adding aliases so that xml messages won't have java packages */
		
		/* Request related commands */
		xStream.alias("CancelRequest", CancelRequest.class);
		xStream.alias("CancelResponse", CancelResponse.class);
		xStream.alias("FindRequest", FindRequest.class);
		xStream.alias("FindResponse", FindResponse.class);
		xStream.alias("SubmitRequest", SubmitRequest.class);
		xStream.alias("SubmitResponse", SubmitResponse.class);
		xStream.alias("RequestDetails", RequestDetails.class);
		
		/* Admin commands */
		xStream.alias("PingRequest", PingRequest.class);
		xStream.alias("PingResponse", PingResponse.class);
		
		xStream.alias("Request", Request.class);
		xStream.alias("Response", Response.class);
	}
	
	public String toXML(Object o) {
		return xStream.toXML(o);
	}
	
	public Object fromXML(String xml) {
		return xStream.fromXML(xml); 
	}
	
}
