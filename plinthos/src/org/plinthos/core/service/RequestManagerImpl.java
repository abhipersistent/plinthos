package org.plinthos.core.service;

import java.util.Date;
import java.util.List;

import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.PlinthosRequestStatus;
import org.plinthos.core.persistence.DAOFactory;
import org.plinthos.core.persistence.PlinthosRequestDAO;
import org.plinthos.core.persistence.txn.TxAction;
import org.plinthos.core.persistence.txn.TxTemplate;

class RequestManagerImpl implements RequestManager {

	private PlinthosRequestDAO requestDao;
	
	RequestManagerImpl() {
		this.requestDao = DAOFactory.getInstance().getRequestDAO(); 
	}
	
	@Override
	public void cancelRequest(final int requestId) {
		
		TxTemplate txTemplate = new TxTemplate();
		
		TxAction<PlinthosRequest> txAction = 
			new TxAction<PlinthosRequest>() {

				@Override
				public PlinthosRequest run() {
			        PlinthosRequest r = requestDao.findById(requestId, false);
			        if( r != null ) {
			        	r.setCancelRequested(true);
			        }
			        return null;
				}
		};
		
		txTemplate.execute(txAction);
	}

	@Override
	public PlinthosRequest createRequest(final PlinthosRequest r) {
		
		TxTemplate txTemplate = new TxTemplate();
		
		TxAction<PlinthosRequest> txAction = 
			new TxAction<PlinthosRequest>() {

				@Override
				public PlinthosRequest run() {
					r.setStatus(PlinthosRequestStatus.SUBMITTED);
					r.setSubmissionTime( new Date() );
					return requestDao.makePersistent(r);
				}
		};
		
		return txTemplate.execute(txAction);
	}

	@Override
	public List<PlinthosRequest> findNewRequests(final int lastMaxRequestId) {

		TxTemplate txTemplate = new TxTemplate();
		
		TxAction<List<PlinthosRequest>> txAction = 
			new TxAction<List<PlinthosRequest>>() {

				@Override
				public List<PlinthosRequest> run() {
					return requestDao.findNewRequests(lastMaxRequestId);
				}
		};
		
		return txTemplate.execute(txAction);
	}

	@Override
	public PlinthosRequest getRequest(final int requestId) {
		
		TxTemplate txTemplate = new TxTemplate();
		
		TxAction<PlinthosRequest> txAction = 
			new TxAction<PlinthosRequest>() {

				@Override
				public PlinthosRequest run() {
					return requestDao.findById(requestId, false);
				}
		};
		
		return txTemplate.execute(txAction);
	}
	
	@Override
	public void updateRequestStatus(final int requestId, final String status, final String statusMessage) {

		
		TxTemplate txTemplate = new TxTemplate();
		
		TxAction<PlinthosRequest> txAction = 
			new TxAction<PlinthosRequest>() {
				@Override
				public PlinthosRequest run() {
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
					return null;
				}
		};
		
		txTemplate.execute(txAction);
	}

	@Override
	public void updateProgressMessage(final int requestId, final String progressMessage) {

		TxTemplate txTemplate = new TxTemplate();
		
		TxAction<PlinthosRequest> txAction = 
			new TxAction<PlinthosRequest>() {
				@Override
				public PlinthosRequest run() {
			        PlinthosRequest r = requestDao.findById(requestId, false);
			       	r.setProgressMessage(progressMessage);
			       	return null;
				}
			
		};
		
		txTemplate.execute(txAction);
	}

	@Override
	public void updateStatusMessage(final int requestId, final String statusMessage) {

		TxTemplate txTemplate = new TxTemplate();
		
		TxAction<PlinthosRequest> txAction = 
			new TxAction<PlinthosRequest>() {
				@Override
				public PlinthosRequest run() {
			        PlinthosRequest r = requestDao.findById(requestId, false);
			       	r.setStatusMessage(statusMessage);
					return null;
				}
		};
		
		txTemplate.execute(txAction);
	}
	
	@Override
	public void saveResults(final int requestId, final String data) {

		TxTemplate txTemplate = new TxTemplate();
		
		TxAction<PlinthosRequest> txAction = 
			new TxAction<PlinthosRequest>() {
				@Override
				public PlinthosRequest run() {
			        PlinthosRequest r = requestDao.findById(requestId, false);
			        r.setRequestResults(data);
			        return null;
				}
		};
		
		txTemplate.execute(txAction);
	}

	@Override
	public List<PlinthosRequest> findRequestsByCorrelationId(
			final String correlationId) {

		TxTemplate txTemplate = new TxTemplate();
		
		TxAction<List<PlinthosRequest>> txAction = 
			new TxAction<List<PlinthosRequest>>() {
				@Override
				public List<PlinthosRequest> run() {
					return requestDao.findRequestsByCorrelationId(correlationId);
				}
		};
		
		return txTemplate.execute(txAction);
	}

	@Override
	public long findIncompleteRequestCount() {
		
		TxTemplate txTemplate = new TxTemplate();

		TxAction<Long> txAction = 
			new TxAction<Long>() {
				@Override
				public Long run() {
					String[] statusList = new String[] { 
							PlinthosRequestStatus.SUBMITTED, 
							PlinthosRequestStatus.IN_PROGRESS };
					
					return requestDao.findRequestCountWithStatus(statusList);
				}
		};
		
		return txTemplate.execute(txAction);
	}
}
