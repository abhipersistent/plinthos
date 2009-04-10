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
 * All checked exceptions encountered within PlinthOS services should 
 * use this class.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class PlinthosException extends Exception {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 5956748265124461531L;

	/**
	 * The debug information (typically the class name and method name (e.g.
	 * "Foo.bar")).
	 */
	private String debugInformation;

	/**
	 * The default exception message (used if the exception code is zero or
	 * cannot be resolved).
	 */
	private String message;

	/**
	 * The exception code.
	 */
	private int messageCode;

	/**
	 * The root exception. This may be null.
	 */
	private Throwable rootCause;


	/**
	 * Gets the root cause for exception
	 * 
	 * 
	 * @return Throwable root cause for exception
	 * 
	 */
	public Throwable getRootCause() {
		return rootCause;
	}

	/**
	 * Gets the debug information of the exception
	 * 
	 * @return String debug information of the exception
	 * 
	 */
	public String getDebugInformation() {
		return debugInformation;
	}

	/**
	 * Gets the exception message.
	 * 
	 * @return String exception message.
	 * 
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the message code for exception
	 * 
	 * @return int
	 * 
	 */
	public int getMessageCode() {
		return messageCode;
	}

	/**
	 * Constructor PlinthosException.
	 * @param code
	 * @param errMsgParams
	 * @param debugInformation
	 * @param rootCause
	 * @param locale
	 */
	public PlinthosException(int code, Object[] errMsgParams, String debugInformation, Throwable rootCause) {

		PlinthosMessageFactory messageFactory = null;
		this.messageCode = code;

		messageFactory = new PlinthosMessageFactory();

		if (errMsgParams == null) {
			this.message = messageFactory.getMessage(code);
		} else {
			this.message = messageFactory.getMessage(code, errMsgParams);
		}

		this.debugInformation = debugInformation;
		if (rootCause != null) {
			initCause(rootCause);
			this.rootCause = rootCause;
		}
	}


	/**
	 * Constructor PlinthosException.
	 * 
	 * @param code
	 * @param errMsgParams
	 * @param rootCause
	 */
	public PlinthosException(int code, Object[] errMsgParams, Throwable rootCause) {
		this(code, errMsgParams, null, rootCause);
	}


	/**
	 * Constructor PlinthosException.
	 * 
	 * @param code
	 * @param errMsgParams
	 */
	public PlinthosException(int code, Object[] errMsgParams) {
		this(code, errMsgParams, null, null);
	}

	/**
	 * Constructor PlinthosException.
	 * @param code
	 * @param rootCause
	 * @param debugInformation
	 */
	public PlinthosException(int code, Throwable rootCause, String debugInformation) {
		this(code, null, debugInformation, rootCause);
	}

	/**
	 * Constructor PlinthosException.
	 * 
	 * @param code
	 * @param rootCause
	 */
	public PlinthosException(int code, Throwable rootCause) {
		this(code, null, null, rootCause);
	}

	/**
	 * Constructor PlinthosException.
	 * @param code
	 * @param rootCause
	 * @param debugInformation
	 */
	public PlinthosException(int code, String debugInformation) {
		this(code, null, debugInformation, null);
	}

	/**
	 * Constructor PlinthosException.
	 * 
	 * @param code
	 */
	public PlinthosException(int code) {
		this(code, null, null, null);
	}

}
