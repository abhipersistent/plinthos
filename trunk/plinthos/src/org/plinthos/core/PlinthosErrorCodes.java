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
 * This file contains the ERROR codes for PlinthOS.
 * 
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public interface PlinthosErrorCodes {

	final int NAMING_EXCEPTION            = 100006;
	final int SQL_EXCEPTION               = 100003;
	final int REQUEST_CANCELLATION_FAILED = 100011;
	final int UNKNOWN_EXCEPTION           = 200012;
	final int UNKNOWN_REQUEST_TYPE        = 200204;
	final int QUEUE_NOT_EMPTY_EXCEPTION   = 200205;
}
