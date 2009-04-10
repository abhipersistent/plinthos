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
package org.plinthos.core.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.ejb3.annotation.LocalBinding;
import org.plinthos.core.dao.PlinthosRequestDao;
import org.plinthos.core.model.RegisteredTask;
import org.plinthos.core.model.PlinthosRequest;
import org.plinthos.core.model.PlinthosRequestStatus;
import org.plinthos.core.model.impl.PlinthosRequestImpl;
import org.plinthos.util.UID;

/**
 * Stateless EJB Implementation of PlinthosRequestDao interface.
 * 
 * 
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
@Stateless
@LocalBinding(jndiBinding="PlinthosRequestDaoLocal")
public class PlinthosRequestDaoImpl implements PlinthosRequestDao {

	@PersistenceContext(unitName="PLINTHOS_DB")
	private EntityManager em;
	
	/* (non-Javadoc)
	 * @see org.plinthos.core.dao.PlinthosRequestDao#createRequest(org.plinthos.core.model.RegisteredTask, java.lang.String, int, int, java.lang.String)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public PlinthosRequest createRequest(RegisteredTask taskInfo, String userId, int priority, int expiresAfterMinutes, String xmlData) {
		PlinthosRequest plinthosRequest = new PlinthosRequestImpl();
		
        plinthosRequest.setTask(taskInfo);
        plinthosRequest.setId(Math.abs(UID.getUID().genID().intValue()));
        plinthosRequest.setUserId(userId);
        plinthosRequest.setSubmissionTime(new Timestamp(System.currentTimeMillis()));
        long expirationTime = System.currentTimeMillis() + (expiresAfterMinutes*60*1000);
        
        plinthosRequest.setExpiration(new Date(expirationTime));
        plinthosRequest.setPriority(priority);

        plinthosRequest.setRequestParams(xmlData);
        plinthosRequest.setStatus(PlinthosRequestStatus.SUBMITTED);
        em.persist(plinthosRequest);
        return plinthosRequest;
	}

	/* (non-Javadoc)
	 * @see org.plinthos.core.dao.BaseDao#delete(java.lang.Object)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(PlinthosRequest posRequest) {
		PlinthosRequest request = findByPrimaryKey(posRequest.getId());
		em.remove(request);
	}

	/* (non-Javadoc)
	 * @see org.plinthos.core.dao.BaseDao#executeQuery(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<PlinthosRequest> executeQuery(String query) {
		return (List<PlinthosRequest>) em.createNamedQuery(query).getResultList();
	}

	/* (non-Javadoc)
	 * @see org.plinthos.core.dao.BaseDao#executeQuery(java.lang.String, java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public List<PlinthosRequest> executeQuery(String name, Object... params) {
		final Query query = em.createNamedQuery(name);
        for(int i = 0; i < params.length; i++) {
            query.setParameter(i+1, params[i]);
        }
        return (List<PlinthosRequest>)query.getResultList();
	}

	/* (non-Javadoc)
	 * @see org.plinthos.core.dao.BaseDao#findByPrimaryKey(java.lang.Object)
	 */
	public PlinthosRequest findByPrimaryKey(Object id) {
		return (PlinthosRequest) em.find(PlinthosRequestImpl.class, id);
	}
	
	/* (non-Javadoc)
	 * @see org.plinthos.core.dao.BaseDao#save(java.lang.Object)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void save(PlinthosRequest posRequest) {
		em.persist(posRequest);
	}

}
