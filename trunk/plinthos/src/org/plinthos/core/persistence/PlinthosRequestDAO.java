package org.plinthos.core.persistence;

import java.util.List;

import org.plinthos.core.model.PlinthosRequest;

public interface PlinthosRequestDAO extends GenericDAO<PlinthosRequest, Integer> {

	List<PlinthosRequest> findByStatus(String status);	
	
	List<PlinthosRequest> findNewRequests(int lastMaxRequestId);
	
	List<PlinthosRequest> findRequestsByCorrelationId(String correlationId);
	
	Long findRequestCountWithStatus(String[] statuses);
}
