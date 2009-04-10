package org.plinthos.application;

import org.apache.log4j.Logger;

public class PlinthOSApp {

	private static final Logger log = Logger.getLogger(PlinthOSApp.class);
	
	public static void main(String[] args) {
		log.info("starting PlinthOS...");
		PlinthOS plinthOS = new PlinthOS();
		plinthOS.start();
		log.info("*** PlinthOS is running");
	}
}
