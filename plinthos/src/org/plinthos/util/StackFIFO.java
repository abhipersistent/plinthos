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
package org.plinthos.util;


/**
 * This is a stack that implements a FIFO algorithm.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class StackFIFO {

	int size;
	long[] stack;

	public StackFIFO(int size) {
		this.size = size;
		stack = new long[size];

		for (int i = 0; i < size; i++) {
			stack[i] = Constants.ZERO_LONG;
		}
	}

	public void push(long val) {

		for (int i = size - 2; i >= 0; i--) {

			stack[i + 1] = stack[i];
		}

		stack[0] = val;
	}

	public double getMeanValue() {

		double meanValue = Constants.ZERO_DOUBLE;
		int nonZeroValues = 0;

		for (int i = size - 1; i >= 0; i--) {

			if (stack[i] > Constants.ZERO_LONG) {
				meanValue += stack[i];
				nonZeroValues++;
			}
		}
		if (nonZeroValues == 0)
			nonZeroValues = 1;

		meanValue /= nonZeroValues;

		return meanValue;
	}

	public int getSize() {
		return size;
	}

	public double[] toConstantArray(int dof) {

		double[] stack = new double[dof];

		for (int i = 0; i < dof; i++) {
			stack[i] = getMeanValue();
		}

		return stack;
	}
}
