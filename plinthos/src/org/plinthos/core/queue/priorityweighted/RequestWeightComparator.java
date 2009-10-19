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
package org.plinthos.core.queue.priorityweighted;

import java.util.Comparator;

/**
 * This class is used to compare two <code> QueueRequest </code> objects based on the
 * time to live attribute
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class RequestWeightComparator implements Comparator<PriorityWeightedQueueRequest> {

	/**
	 * Compares two requests objects based on weight attribute
	 * 
	 * 
	 * 
	 * @param request1
	 *            First request object
	 * @param request2
	 *            Second request object
	 * 
	 * @return int
	 * 
	 */
	public int compare(PriorityWeightedQueueRequest request1, PriorityWeightedQueueRequest request2) {

		if ((request1 == null) || (request2 == null)) {
			throw new NullPointerException();
		}
		int result = 0;
		if (request1.getWeight() > request2.getWeight()) {
			result = 1;
		} else if ( request1.getWeight() < request2.getWeight() ) {
			result = -1;
		} else if ( request1.getWeight() == request2.getWeight() ) {
			result = 0;
		}
		return result;
	}
}
