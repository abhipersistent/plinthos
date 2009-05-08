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

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.plinthos.core.framework.HibernateUtil;



abstract class GenericHibernateDAO<T, ID extends Serializable> {

	private Class<T> persistentClass;
	
	@SuppressWarnings("unchecked")
	public GenericHibernateDAO() {
		this.persistentClass = (Class<T>)
			( (ParameterizedType) getClass().getGenericSuperclass() )
				.getActualTypeArguments()[0];
	}
	
	public void setSession(Session s) {
		//	this.session = s;
	}
	
	protected Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
	
	public Class<T> getPersistentClass() {
		return persistentClass;
	}
	
	@SuppressWarnings("unchecked")
	public T findById(ID id, boolean lock) {
		
		T entity;
		
		if( lock ) {
			entity = (T) getSession()
				.get(getPersistentClass(), id, LockMode.UPGRADE);
		}
		else {
			entity = (T) getSession()
				.get(getPersistentClass(), id);
		}
	
		return entity;
	}
	
	public List<T> findAll() {
		return findByCriteria();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance,
			String... excludeProperty) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		Example example = Example.create(exampleInstance);
		for(String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		
		crit.add(example);
		
		return crit.list();
	}

	public T makePersistent(T entity) {
		getSession().saveOrUpdate(entity);
		return entity;
	}
	
	public void makeTransient(T entity) {
		getSession().delete(entity);
	}
	
	public void flush() {
		getSession().flush();
	}
	
	public void clear() {
		getSession().clear();
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for(Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();
	}
	
}
