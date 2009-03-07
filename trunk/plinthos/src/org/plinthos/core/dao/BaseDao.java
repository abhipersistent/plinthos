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
package org.plinthos.core.dao;

import java.util.List;


/**
 * A generic Data access interface.
 * 
 * Provides basic data access API such as look up by Primary Key, persistence of specific
 * object, deletion of specific object, returning results of a named query etc.
 * 
 * May be extended to add Type specific API. For example, For Employee Type, you may add API like
 * findByDept(dept).
 * 
 * @author <a href="mailto:kishorekirdat@gmail.com">Kishore Kirdat</a>
 * @version 1.0
 */
public interface BaseDao<T> {
	/**
	 * Looks up an object based on PK.
	 * Returns null when no matching instance is found.
	 * @param id
	 * @return
	 */
	T findByPrimaryKey(Object id);

	/**
	 * Saves an object.
	 * @param t
	 */
	void save(T t);
	
	/**
	 * Deletes an object.
	 * @param t
	 */
	void delete(T t);
	
	/**
	 * Executes a named query and returns matching results.
	 * @param query
	 * @return
	 */
	List<T> executeQuery(final String query);
	
	/**
	 * Executes a named query using parameters specified and returns matching results.
	 * @param name
	 * @param params
	 * @return
	 */
	List<T> executeQuery(final String name, final Object... params);
}
