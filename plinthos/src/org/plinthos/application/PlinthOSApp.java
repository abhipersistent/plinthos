package org.plinthos.application;

import org.apache.log4j.Logger;

public class PlinthOSApp {

	private static final Logger log = Logger.getLogger(PlinthOSApp.class);
	
	public static void main(String[] args) {
		String startMsg = "starting PlinthOS..."; 
		log.info(startMsg);
		System.out.println(startMsg);
		PlinthOS plinthOS = new PlinthOS();
		plinthOS.start();
		String readyMsg = "PlinthOS is running";
		log.info(readyMsg);
		System.out.println(readyMsg);
	}
}
