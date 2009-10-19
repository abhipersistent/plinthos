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
package org.plinthos.core.persistence;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.PlinthosRequestStatus;

class PlinthosRequestDAOHibernate extends GenericHibernateDAO<PlinthosRequest, Integer>
	implements PlinthosRequestDAO {

	@SuppressWarnings("unchecked")
	// @Override
	public List<PlinthosRequest> findByStatus(String status) {
		Query q = getSession().createQuery("from PlinthosRequest r where r.status = :status");
		q.setParameter("status", status);
		return (List<PlinthosRequest>)q.list();
	}

	@SuppressWarnings("unchecked")
	// @Override
	public List<PlinthosRequest> findNewRequests(int lastMaxRequestId, int maxResults) {
		Query q = getSession().createQuery("from PlinthosRequest r where r.id > :lastMaxRequestId and r.status = :status order by r.id");
		q.setParameter("lastMaxRequestId", lastMaxRequestId);
		q.setParameter("status", PlinthosRequestStatus.SUBMITTED);
		q.setMaxResults(maxResults);
		return (List<PlinthosRequest>)q.list();
	}

	@SuppressWarnings("unchecked")	
	// @Override
	public List<PlinthosRequest> findRequestsByCorrelationId(
			String correlationId) {
		Query q = getSession().createQuery("from PlinthosRequest r where r.correlationId = :correlationId");
		q.setParameter("correlationId", correlationId);
		return (List<PlinthosRequest>)q.list();
	}

	// @Override
	public Long findRequestCountWithStatus(String[] statuses) {
		Query q = getSession().createQuery("select count(*) from PlinthosRequest r where r.status in ( :statusList )");
		q.setParameterList("statusList", statuses);
		
		return (Long)q.uniqueResult();
	}

	@SuppressWarnings("unchecked")	
	@Override
	public List<PlinthosRequest> findRequestsByTaskTypeAndStatusAndCompletionTime(
			String taskType, String status, Date completionTime) {
		Query q = getSession().createQuery("from PlinthosRequest r where r.taskInfo.taskType = :taskType and r.status = :status and r.completionTime >= :completionTime");
		q.setParameter("taskType", taskType);
		q.setParameter("status", status);
		q.setParameter("completionTime", completionTime);
		return (List<PlinthosRequest>)q.list();
	}
	
	
	
}
