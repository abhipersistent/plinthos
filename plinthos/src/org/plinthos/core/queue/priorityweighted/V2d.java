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

/**
 * A "light" two dimensional vector.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class V2d {
	protected double x1;
	protected double x2;

	public V2d() {
		this.x1 = 0.0;
		this.x2 = 0.0;
	}

	public V2d(double x1, double x2) {
		this.x1 = x1;
		this.x2 = x2;
	}

	public double getX1() {
		return x1;
	}

	public double getX2() {
		return x2;
	}

	public void setX1(double val) {
		x1 = val;
	}

	public void setX2(double val) {
		x2 = val;
	}
}
