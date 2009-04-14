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
