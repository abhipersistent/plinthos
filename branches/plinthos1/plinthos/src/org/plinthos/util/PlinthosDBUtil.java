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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.plinthos.core.PlinthosErrorCodes;
import org.plinthos.core.PlinthosException;


/**
 * Class PlinthosDBUtil $detailed description$
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class PlinthosDBUtil {

	public static final Logger log = Logger.getLogger(PlinthosDBUtil.class);

	/**
	 * Returns a connection to the PlinthOS database.
	 * 
	 * @return
	 * @throws PlinthosException
	 */
	public static Connection getDBConnection() throws PlinthosException {
		
		DataSource ds = (DataSource) ServiceLocator.getInstance().getFromJNDI(DBConstants.DATASOURCE_NAME);
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			throw new PlinthosException(PlinthosErrorCodes.SQL_EXCEPTION, e);
		}
	}

	/**
	 * Close a database connection.
	 * 
	 * @param conn
	 */
	public static void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException sqlException) {
			log.error("SQL Exception in closeConnection method", sqlException);
		}
	}

	/**
	 * Close a statement (does not throw exception).
	 * 
	 * @param stmt
	 */
	public static void closeStatement(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException sqlException) {
			log.error("SQL Exception in closeStatement method", sqlException);
		}
	}

	/**
	 * Close a result set (does not throw exception).
	 * 
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException sqlException) {
			log.error("SQL Exception in closeResultSet method", sqlException);
		}
	}

	/**
	 * This method executes the query and returns RsultSet.
	 * 
	 * @param con
	 * @param query
	 * @return
	 * @throws PlinthosException
	 */
	public static boolean execute(Connection con, String query) throws PlinthosException {

		log.debug("Entering into executeQuery method");

		try {
			Statement statement = con.createStatement();

			boolean result = statement.execute(query);
			statement.close();
			return result;

		} catch (SQLException sqlException) {
			log.error("SQL Exception in executeQuery method", sqlException);

			throw new PlinthosException(PlinthosErrorCodes.SQL_EXCEPTION, sqlException);
		}
	}

	/**
	 * This method executes the query and returns RsultSet.
	 * 
	 * @param con
	 * @param query
	 * @return
	 * @throws PlinthosException
	 */
	public static ResultSet executeQuery(Connection con, String query) throws PlinthosException {

		log.debug("Entering into executeQuery method");

		try {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			// statement.close();
			return resultSet;
		} catch (SQLException sqlException) {
			log.error("SQL Exception in executeQuery method", sqlException);
			throw new PlinthosException(PlinthosErrorCodes.SQL_EXCEPTION, sqlException);
		}
	}

	/**
	 * @param writeConnection
	 * @param string
	 * @return
	 */
	public static PreparedStatement createPreapreStatement(Connection writeConnection, String query)
			throws PlinthosException {
		try {
			PreparedStatement pstmt = writeConnection.prepareStatement(query);
			return pstmt;
		} catch (SQLException sqlException) {
			log.error("SQL Exception in createPreapreStatement method", sqlException);
			throw new PlinthosException(PlinthosErrorCodes.SQL_EXCEPTION, sqlException);
		}

	}

}
