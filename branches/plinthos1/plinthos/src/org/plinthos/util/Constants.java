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

import java.sql.Timestamp;

/**
 * The constants used in PlinthOS
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class Constants {

	public static final double ZERO_DOUBLE = 0.0;

	public static final float ZERO_FLOAT = 0.0f;

	public static final long ZERO_LONG = 0L;

	public static final int ZERO_INT = 0;

	public static final String HARDCODED = "HARDCODED";

	public static double ALPHA = 0.0;

	public static double BETA = 0.0;

	public static double GAMMA = 0.0;

	public static int FIFO_STACK_SIZE = 1;

	public static long EXPIRE_TIME_FACTOR = 10000;

	public static int RK4_SCHEME_GRID_POINTS = 5000;

	public static int RK4_SCHEME_INTEGRATION_TIME = 300;

	public static int RK4_DEFAULT_INITIAL_DOF = 6;

	public static int QUEUE_CAPACITY = 32;
	
	// this is 3/4 of the capacity
	public static int QUEUE_TOLERANCE_INDEX = 24;

	public static double QUEUE_TOLERANCE = 0.05d;

	public static double QUEUE_WEIGHT_TOLERANCE = 0.05d;

	// Priority Weighted Queue : Weight List values

	public static String QUEUE_WEIGHTS_TYPE = HARDCODED;

	public static int WEIGHT_LIST_START_INDEX = 1;

	// public static int WEIGHT_LIST_END_INDEX = 6;

	public static String WEIGHT_VALUES = "0.50d#0.25d#0.15d#0.05d#0.03d#0.02d";

	public static String WEIGHT_VALUE_SEPERATOR = "#";

	public static int MAX_THREAD_POOL_SIZE = 2;

	public static int MIN_THREAD_POOL_SIZE = 1;

	public static final String ERROR_BUNDLE = "org.plinthos.resbundles.ErrorMessages";

	public static String SUPPORT_EMAIL = "babis.marmanis@gmail.com";

	public static Timestamp SYSTEM_CONFIG_LAST_UPDATE;
	
	public static String SERVICES_LIBRARY="plinthos-lib";

	// Email related constants

	public static final String EMAIL_CONTENT_TYPE = "text/plain";

	public static final String EMAIL_MESSAGE = "Message";

	public static final String EMAIL_SUBJECT = "Subject";

	public static final String EMAIL_TO_ADDRESS = "ToAddress";

	public static final String MAILER_BEAN_JNDI_NAME = "java:/Mail";

}
