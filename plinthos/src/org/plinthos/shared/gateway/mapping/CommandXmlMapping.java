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
