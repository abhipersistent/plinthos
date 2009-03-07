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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.plinthos.core.dao.BaseDao;

/**
 * Generalized implementation of BaseDao using javax.persistence.EntityManager.
 * 
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public class GenericEntityManagerDao<T> implements BaseDao<T> {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(GenericEntityManagerDao.class);

	/**
	 * The entity manager.
	 */
	private EntityManager em;
	
	private Class<? extends T> _clazz;
	
	
	public GenericEntityManagerDao(final Class<? extends T> clazz, EntityManager em) {
        _clazz = clazz;
        this.em = em;
    }
	
	/* (non-Javadoc)
	 * @see com.zeborg.dao.BaseDao#delete(java.lang.Object)
	 */
	public void delete(final T t) {
		em.remove(t);
	}

	/* (non-Javadoc)
	 * @see com.zeborg.dao.BaseDao#findByPrimaryKey(java.lang.String)
	 */
	public T findByPrimaryKey(final Object id) {
		return (T) em.find(_clazz, id);
	}

	/* (non-Javadoc)
	 * @see com.zeborg.dao.BaseDao#save(java.lang.Object)
	 */
	public void save(final T t) {
		em.persist(t);
	}

	/* (non-Javadoc)
	 * @see com.zeborg.dao.BaseDao#executeQuery(java.lang.String)
	 */
    @SuppressWarnings({"unchecked"})
    public List<T> executeQuery(final String query) {
        return (List<T>) em.createNamedQuery(query).getResultList();
    }

    /* (non-Javadoc)
	 * @see com.zeborg.dao.BaseDao#executeQuery(java.lang.String, java.lang.Object...)
	 */
    @SuppressWarnings({"unchecked"})
    public List<T> executeQuery(final String name, final Object... params) {
        final Query query = em.createNamedQuery(name);
        for(int i = 0; i < params.length; i++) {
            query.setParameter(i+1, params[i]);
        }
        return (List<T>)query.getResultList();
    }
    
	/**
	 * Getter for the em.
	 *		To be used by subclasses.
	 * @return the em
	 */
	protected final EntityManager getEntityManager() {
		return em;
	}

	
}
