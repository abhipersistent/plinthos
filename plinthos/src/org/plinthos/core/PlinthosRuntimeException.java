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
package org.plinthos.core;

/**
 * Facilitating the encapsulation of runtime throwable exceptions 
 * 
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class PlinthosRuntimeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1289710756266910990L;

	public PlinthosRuntimeException() {
		
	}

	public PlinthosRuntimeException(String argMessage) {
		super(argMessage);
	}

	public PlinthosRuntimeException(int code) {

	}

	public PlinthosRuntimeException(String argMessage, Throwable rootCause) {
		super(argMessage, rootCause);
	}
}
