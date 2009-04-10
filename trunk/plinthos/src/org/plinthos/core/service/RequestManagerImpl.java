package org.plinthos.core.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.plinthos.core.framework.HibernateUtil;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.PlinthosRequestStatus;
import org.plinthos.core.persistence.DAOFactory;
import org.plinthos.core.persistence.PlinthosRequestDAO;

//TODO: add robust txn handling
class RequestManagerImpl implements RequestManager {

	private PlinthosRequestDAO requestDao;
	
	RequestManagerImpl() {
		this.requestDao = DAOFactory.getInstance().getRequestDAO(); 
	}

	@Override
	public void cancelRequest(int requestId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        PlinthosRequest r = requestDao.findById(requestId, false);
        if( r != null ) {
        	r.setCancelRequested(true);
        }

    	session.getTransaction().commit();
	}

	@Override
	public PlinthosRequest createRequest(PlinthosRequest r) {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
		
		r.setStatus(PlinthosRequestStatus.SUBMITTED);
		r.setSubmissionTime( new Date() );
		requestDao.makePersistent(r);

    	session.getTransaction().commit();
    	
		return r;
	}

	@Override
	public List<PlinthosRequest> findNewRequests(int lastMaxRequestId) {

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
		
		List<PlinthosRequest> results = requestDao.findNewRequests(lastMaxRequestId);

		session.getTransaction().commit();
		
		return results; 
	}

	@Override
	public PlinthosRequest getRequest(int requestId) {
		
		boolean txStarted = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		if( !session.getTransaction().isActive() ) {
			session.beginTransaction();
			txStarted = true;
		}
		
		PlinthosRequest r = requestDao.findById(requestId, false);

		if( txStarted ) {
			session.getTransaction().commit();
		}
		
		return r;
	}

	@Override
	public void updateRequestStatus(int requestId, String status) {

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
		
		PlinthosRequest r = getRequest(requestId);
		r.setStatus(status);
	
		if( r != null ) {
			r.setStatus(status);
			if( PlinthosRequestStatus.isComplete(status) ) {
				r.setCompletionTime(new Date());
			}
			else if( PlinthosRequestStatus.IN_PROGRESS.equals(status) ) {
				// do nothing
			}
			else {
				throw new RuntimeException("Invalid request status: " + status);
			}
		}

		session.getTransaction().commit();
	}

	
	@Override
	public void updateRequestStatus(int requestId, String status, String statusMessage) {

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
		
		PlinthosRequest r = getRequest(requestId);
		r.setStatus(status);
		r.setStatusMessage(statusMessage);
	
		if( r != null ) {
			r.setStatus(status);
			if( PlinthosRequestStatus.isComplete(status) ) {
				r.setCompletionTime(new Date());
			}
			else if( PlinthosRequestStatus.IN_PROGRESS.equals(status) ) {
				// do nothing
			}
			else {
				throw new RuntimeException("Invalid request status: " + status);
			}
		}

		session.getTransaction().commit();
	}

	@Override
	public void updateProgressMessage(int requestId, String progressMessage) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        PlinthosRequest r = requestDao.findById(requestId, false);
       	r.setProgressMessage(progressMessage);

    	session.getTransaction().commit();
	}

	@Override
	public void updateStatusMessage(int requestId, String statusMessage) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        PlinthosRequest r = requestDao.findById(requestId, false);
       	r.setStatusMessage(statusMessage);

    	session.getTransaction().commit();
	}
	
	@Override
	public void saveResults(int requestId, String data) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        PlinthosRequest r = requestDao.findById(requestId, false);
        r.setRequestResults(data);

    	session.getTransaction().commit();
	}

	@Override
	public List<PlinthosRequest> findRequestsByCorrelationId(
			String correlationId) {

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
		
		List<PlinthosRequest> results = requestDao.findRequestsByCorrelationId(correlationId);

		session.getTransaction().commit();
		
		return results; 
	}
}
