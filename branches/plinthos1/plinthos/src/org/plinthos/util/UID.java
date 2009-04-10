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
package org.plinthos.util;

/**
 * This class generates a unique string to be used as an ID field. It uses the
 * class <CODE>java.rmi.server.UID</CODE> This class is a singleton and is
 * therefore shared for one VM.
 * 
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class UID {

	private static UID ivUID = null;

	private UID() {
	}

	/**
	 * This provides us with an instance of the class
	 */
	public static UID getUID() {
		if (ivUID == null) {
			synchronized (UID.class) {
				if (ivUID == null) {
					ivUID = new UID();
				}
			}
		}
		return ivUID;
	}

	/**
	 * This method generates a new ID by hashing the String value that is
	 * returned by the <CODE>toString()</CODE> method of the
	 * <CODE>java.rmi.server.UID</CODE> class.
	 */
	public Integer genID() {
		synchronized (ivUID) {
			java.rmi.server.UID uid = new java.rmi.server.UID();
			return new Integer(uid.hashCode());
		}
	}
}
