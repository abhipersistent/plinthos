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
package org.plinthos.core.service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.plinthos.core.framework.Constants;
import org.plinthos.core.framework.HibernateUtil;
import org.plinthos.core.model.SystemConfigurationProperty;
import org.plinthos.core.persistence.DAOFactory;
import org.plinthos.core.persistence.SystemConfigurationDAO;
import org.plinthos.core.persistence.txn.TxAction;
import org.plinthos.core.persistence.txn.TxTemplate;

//TODO: add robust txn handling
class ConstantsManagerImpl implements ConstantsManager {

	private static final Logger log = Logger.getLogger(ConstantsManagerImpl.class);

	ConstantsManagerImpl() {
		
	}
	
	public void loadSystemProperties() {
   		TxTemplate txTemplate = new TxTemplate();
   		
   		TxAction<List<SystemConfigurationProperty>> txAction = 
   			new TxAction<List<SystemConfigurationProperty>>() {
				@Override
				public List<SystemConfigurationProperty> run() {
			    	SystemConfigurationDAO dao = getSystemConfigurationDAO();
					return dao.findAll();
				}
   		};
   		
   		List<SystemConfigurationProperty> allProperties = 
   			txTemplate.execute(txAction);
   		
    	updateConstantValues(allProperties);
		Constants.SYSTEM_CONFIG_LAST_UPDATE = new Timestamp (System.currentTimeMillis());
	}

	private Map<String, String> toNameValueMap(List<SystemConfigurationProperty> properties) {
		Map<String, String> nameValueMap = new HashMap<String, String>();
		
		for(SystemConfigurationProperty p : properties ) {
			nameValueMap.put(p.getName(), p.getValue());
		}
		
		return nameValueMap;
	}
	
	protected SystemConfigurationDAO getSystemConfigurationDAO() {
		return DAOFactory.getInstance().getConfigurationDAO();
	}
    
    /**
	 * Method loadConstants $description$
	 *
	 * @param resultSet $description$
	 * @throws PlinthosException $description$
	 */
    private void updateConstantValues ( List<SystemConfigurationProperty> props ) {
		try {
			Map<String, String> propsMap = toNameValueMap(props);

			int modifiedCount = 0;
			
			Constants constants = new Constants();
			
			Field[] publicFields = constants.getClass().getDeclaredFields();

			for ( int i = 0; i < publicFields.length; i++ ) {
				Field publicField = publicFields [i];
				String fieldName = publicField.getName ();
				String value = propsMap.get( fieldName );
				// Check the property is final or not
				if ( !Modifier.isFinal( publicField.getModifiers() ) && ( null != value ) ) {
					String dataType = publicField.getType ().getName ();
					Object objVal = getWrapperObject(dataType, value);
					publicField.set( constants, objVal );
					log.info("Property " + publicField.getName() + " set to " + objVal );
					modifiedCount++;
				}
			}
			
			log.info ("Number of properties LOADED in Constants: " + modifiedCount );
		}	
		catch ( IllegalAccessException accessException ) {
			log.error (
				"IllegalAccessException thrown loadConstants(ResultSet resultSet) method",
				accessException );
		}
    }
    
	/**
	 * Returns the Wrapper Object based on the data type
	 *
	 *
	 * @param type data type
	 * @param value initial value for wrapper object
	 *
	 * @return Object
	 *
	 */
	private Object getWrapperObject(final String type, final String value) {

		if (type.equals("int")) {
			return new Integer(value);
		} else if (type.equals("double")) {
			return new Double(value);
		} else if (type.equals("long")) {
			return new Long(value);
		} else if (type.equals("float")) {
			return new Float(value);
		} else if (type.equals("boolean")) {
			return new Boolean(value);
		} else if (type.equals("java.lang.String")) {
			return value;
		} else if (type.equals("java.lang.Integer")) {
			return new Integer(value);
		}

		return null;
	}


	@Override
	public void initSystemProperties(Properties props) {
		SystemConfigurationDAO dao = getSystemConfigurationDAO();

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		boolean noError = false;
        session.beginTransaction();
        try {
	    	List<SystemConfigurationProperty> existingProperties = dao.findAll();
			
			Map<String, String> existingNameValueMap = toNameValueMap(existingProperties);    	
	    	Set<String> existingNamesSet = existingNameValueMap.keySet();
	    	
	    	for( Map.Entry<Object, Object> e : props.entrySet()) {
	    		
	    		if( existingNamesSet.contains(e.getKey()) ) {
	    			continue;
	    		}
	    		
	    		String pName = (String)e.getKey();
	    		String pValue = (String)e.getValue();
	
	    		SystemConfigurationProperty vo = new SystemConfigurationProperty();
	    		vo.setName( pName );
	    		vo.setValue( pValue );
	    		vo.setCreationDate(new Date());
	    		vo.setLastChanged(new Date());
	 		
	    		dao.makePersistent(vo);
	    		
	    		log.info("Loaded initial value for property: " + pName + "=" + pValue);    		
	    	}
	    	noError = true;
        }
        finally {
        	if( noError ) {
        		session.getTransaction().commit();
        	}
        	else {
        		session.getTransaction().rollback();
        	}
        }
	}
}
