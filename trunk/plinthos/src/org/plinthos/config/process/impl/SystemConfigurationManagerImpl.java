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
package org.plinthos.config.process.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;
import org.plinthos.config.process.SystemConfigurationManager;
import org.plinthos.core.PlinthosErrorCodes;
import org.plinthos.core.PlinthosException;
import org.plinthos.util.Constants;
import org.plinthos.util.DBConstants;
import org.plinthos.util.PlinthosDBUtil;
import org.plinthos.util.PlinthosUtil;


/**
 * SLSB implementation of SystemConfigurationManager.
 * 
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
@Stateless
@LocalBinding(jndiBinding="SystemConfigurationManagerLocal")
public class SystemConfigurationManagerImpl implements SystemConfigurationManager {

	private static final Logger log = Logger.getLogger(SystemConfigurationManagerImpl.class);
	
	/* (non-Javadoc)
	 * @see org.plinthos.config.process.SystemConfiguration#loadSystemProperties()
	 */
	public void loadSystemProperties() throws PlinthosException {
		log.debug ( "Entering into loadSystemProperties() method" );

		try {
			loadProperties( DBConstants.SELECT_ALL_SYSTEM_CONFIGURATION );
		} catch ( PlinthosException plinthosX ) {
			log.error ( "Exception in reconstructSystemConfiguration:" );

			throw plinthosX;
		}
		log.debug ( "Leaving from loadSystemProperties() method" );
	}

	/* (non-Javadoc)
	 * @see org.plinthos.config.process.SystemConfiguration#reloadSystemProperties()
	 */
	public void reloadSystemProperties() throws PlinthosException {
		log.debug ( "Entering into reloadSystemProperties() method" );
		loadSystemProperties ();
		log.debug ( "Leaving from reloadSystemProperties() method" );
	}

	/* (non-Javadoc)
	 * @see org.plinthos.config.process.SystemConfiguration#synchronizeSystemProperties()
	 */
	public void synchronizeSystemProperties() throws PlinthosException {
		log.debug ( "Entering into synchronizeSystemConfiguration method. " );
		try {
			if ( !getLastSynchronizedTime().equals(Constants.SYSTEM_CONFIG_LAST_UPDATE ) ) {
				loadProperties( retrieveUpdatedQuery() );
			}
		} catch ( PlinthosException plinthosX ) {
			log.error ( "Exception in synchronizeSystemConfiguration method" );

			throw plinthosX;
		}

		log.debug ( "Leaving from  synchronizeSystemConfiguration method. " );
	}

    /**
	 * This method returns the result set object, which consist of properties
	 *
	 * @param sqlQuery SQL query
	 * @throws PlinthosException if error occurs while executing the query
	 */
    private void loadProperties ( String sqlQuery ) throws PlinthosException {
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			// Grab a DB connection from the pool for the real work below
			connection = PlinthosDBUtil.getDBConnection();
			
			// get all records from SYSTEM_CONFIGURATION table
			resultSet = PlinthosDBUtil.executeQuery ( connection, sqlQuery );
			
			// Assing DB values to constants
			loadConstants( resultSet );
			Constants.SYSTEM_CONFIG_LAST_UPDATE = new Timestamp (System.currentTimeMillis());
			
		} catch ( PlinthosException plinthosX ) {
			log.error ( "Error occured while creating the bean" );
			throw plinthosX;
		} finally {
			PlinthosDBUtil.closeResultSet( resultSet );
			PlinthosDBUtil.closeConnection( connection );
		}
    }
    
    /**
	 * Method loadConstants $description$
	 *
	 * @param resultSet $description$
	 * @throws PlinthosException $description$
	 */
    private void loadConstants ( ResultSet resultSet ) throws PlinthosException {
		log.debug ("Entered SystemConfiguration.loadConstants(ResultSet resultSet) method" );

		try {
			Properties properties = new Properties();
			while ( resultSet.next() ) {
				String name = resultSet.getString ( "NAME" );
				String value = resultSet.getString ( "VALUE" );
				if ( value != null ) {
					properties.setProperty( name, value );
				}
			}

			int modifiedCount = 0;
			Constants constants = new Constants();
			
			Field[] publicFields = constants.getClass().getDeclaredFields();

			for ( int i = 0; i < publicFields.length; i++ ) {
				Field publicField = publicFields [i];
				String fieldName = publicField.getName ();
				String value = properties.getProperty ( fieldName );
				// Check the property is final or not
				if ( !Modifier.isFinal( publicField.getModifiers() ) && ( null != value ) ) {
					String dataType = publicField.getType ().getName ();
					Object objVal = PlinthosUtil.getWrapperObject(dataType, value);
					publicField.set( constants, objVal );
					log.info("Property " + publicField.getName() + " set to " + objVal );
					modifiedCount++;
				}
			}
			
			log.info ("Number of properties LOADED in Constants: " + modifiedCount );
			
		} catch ( SQLException sqlException ) {
			log.error (
				"SQLException thrown loadConstants(ResultSet resultSet) method",
				sqlException );
		} catch ( IllegalAccessException accessException ) {
			log.error (
				"IllegalAccessException thrown loadConstants(ResultSet resultSet) method",
				accessException );
		}

		log.debug (
			"Leaving SystemConfiguration.loadConstants(ResultSet _resultSet) method" );
    }
    
    /**
	 * Creates the sql query to  retrieve updated records
	 *
	 * @return String SQL query
	 */
    private String retrieveUpdatedQuery () {
		log.debug ( "Entering into retrieveUpdatedQuery method" );
		StringBuffer sb = new StringBuffer ();
		sb.append ( DBConstants.MODIFIED_RECORDS_FROM_SYSTEM_CONFIGURATION );
		sb.append ( DBConstants.MODIFIED_RECORDS_SUB_WHERE_CLAUSE );
		sb.append ( Constants.SYSTEM_CONFIG_LAST_UPDATE );
		sb.append ( DBConstants.SQL_QUOTES );
		String sqlQuery = sb.toString();
		log.info("Query to retrieve Updated Query " + sqlQuery);
		log.debug ( "Leaving from retrieveUpdatedQuery method" );
		return sqlQuery;
    }
    
    /**
	 * Returns the last updated time of the SYSTEM_CONFIGURATION table
	 *
	 * @return Timestamp last updated time
	 * @throws PlinthosException while executing the SQL query
	 */
    private Timestamp getLastSynchronizedTime () throws PlinthosException {
		log.debug ( "Entered getLastSynchronizedTime method" );
		ResultSet resultSet = null;
		Connection connection = null;
		Timestamp lastSynchronizedTimestamp = null;

		try {
			connection = PlinthosDBUtil.getDBConnection ();
			// Executes the query to get the MAX_LAST_UPDATE
			resultSet =	PlinthosDBUtil.executeQuery(connection, DBConstants.SELECT_MAX_LASTUPDATE_FROM_SYSTEM_CONFIGURATION );
			lastSynchronizedTimestamp = resultSet.getTimestamp(1);
			log.debug ( "lastSynchronizedTimestamp=" + lastSynchronizedTimestamp );
			return lastSynchronizedTimestamp;
		} catch ( SQLException sqlException ) {
			log.error ( "SQLException in getLastSynchronizedTime:", sqlException );

			throw new PlinthosException(PlinthosErrorCodes.SQL_EXCEPTION, sqlException, "SQLException in getLastSynchronizedTime" );
		} catch ( PlinthosException plinthosX ) {
			log.error ( "Unexpected exception in getLastSynchronizedTime:" );
			throw plinthosX;
		} finally {
			PlinthosDBUtil.closeConnection ( connection );
			PlinthosDBUtil.closeResultSet ( resultSet );
			log.debug ( "Leaving getLastSynchronizedTime method" );
		}
    }
}
