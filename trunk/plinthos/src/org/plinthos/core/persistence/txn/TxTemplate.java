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
package org.plinthos.core.persistence.txn;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.plinthos.core.framework.HibernateUtil;

public class TxTemplate {
	
	private static final Logger logger = Logger.getLogger(TxTemplate.class);
	
	private static int DEFAULT_TX_TIMEOUT_IN_SECONDS = 10;
	
	public <T> T execute(TxAction<T> action) {
		return execute(action, DEFAULT_TX_TIMEOUT_IN_SECONDS);
	}
	
	private Session getSession() {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		sf.openSession();
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	public <T> T execute(TxAction<T> action, int txTimeoutInSeconds) {

        T result = null;
        
        boolean txStarted = false;
		Session session = null;
    	Transaction tx = null;
        try {
        	session = getSession();
        	if( !session.getTransaction().isActive() ) {
	        	tx = session.beginTransaction();
	        	tx.setTimeout(txTimeoutInSeconds);
	        	txStarted = true;
        	}
        	else {
        		tx = session.getTransaction();
        		txStarted = false;
        	}
        	
        	result = action.run();
        	
        	if( txStarted ) {
        		tx.commit();
        	}
        }
        catch(RuntimeException e) {
        	try {
        		if( tx != null ) {
        			tx.rollback();
        		}
        	}
        	catch(RuntimeException logOnly) {
        		logger.error("Failed to rollback transaction: ", logOnly);
        	}
        	throw e;
        }
        finally {
        	// attempt to close session only if it is open and we didn't have outside txn.
        	if( session.isOpen() && txStarted == true) {
        		session.close();
        	}
        }
        
        return result;
	}
}
