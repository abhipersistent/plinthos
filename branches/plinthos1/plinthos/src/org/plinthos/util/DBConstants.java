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
 * This interface contains all constants related database operation, which are
 * used in reporting engine.
 *
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public interface DBConstants {

	final String DATASOURCE_NAME = "java:/PlinthOSDS";

	String SELECT_ALL_SYSTEM_CONFIGURATION = "SELECT NAME, VALUE FROM SYSTEM_CONFIGURATION";

	String SELECT_MAX_LASTUPDATE_FROM_SYSTEM_CONFIGURATION = "SELECT MAX(LAST_CHANGED) FROM SYSTEM_CONFIGURATION";

	String MODIFIED_RECORDS_FROM_SYSTEM_CONFIGURATION =	"SELECT NAME, VALUE FROM SYSTEM_CONFIGURATION WHERE ";

	String MODIFIED_RECORDS_SUB_WHERE_CLAUSE = " LAST_CHANGED > '";

	String SQL_QUOTES = "'";
	String LEFT_CLOSING_BRAKET = "(";
	String RIGHT_CLOSING_BRAKET = ")";
	String SPACE = " ";
	String AS = "AS";
	String COMMA = ",";
	String FROM = "FROM";
	String WHERE = "WHERE";
	String EQUAL = "=";

	String SUM = "SUM";

	String DIV = "/";
}
