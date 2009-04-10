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
package org.plinthos.core.model;


/**
 * PlinthosRequestStatus contains the different states of the requested
 * report.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class PlinthosRequestStatus {
	
	// NORMAL
	public static final String SUBMITTED    = "SUBMITTED";
	public static final String IN_PROGRESS  = "RUNNING";	
	public static final String COMPLETED    = "COMPLETED";
	public static final String CANCELED     = "CANCELED";
	public static final String EXPIRED      = "EXPIRED";
	public static final String FAILED       = "FAILED";
	public static final String REJECTED     = "REJECTED";

	
	public static boolean isComplete(String status) {
		
		boolean result = false;
		
		if( PlinthosRequestStatus.COMPLETED.equals(status) ||
			PlinthosRequestStatus.CANCELED.equals(status) ||
			PlinthosRequestStatus.FAILED.equals(status) ||
			PlinthosRequestStatus.EXPIRED.equals(status) ||
			PlinthosRequestStatus.REJECTED.equals(status) ) {
				
			result = true;
		}
		else {
			result = false;
		}
		
		return result;
	}
	
}
