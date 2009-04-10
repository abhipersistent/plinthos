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
package org.plinthos.sample.statistics.data;

/**
 * This class encapsulates the information that will be transferred between
 * the client and the PlinthOS service.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class BasicStatisticsRequest {

	/* Input value */
	private String label;
	private double[] values;
	
	/* Output values to be evaluated by the service */
	private double geometricMean;
	private double min;
	private double max;
	private double sum;
	private double variance;
	private double average;
	
	/**
	 * @return the geometricMean
	 */
	public double getGeometricMean() {
		return geometricMean;
	}

	/**
	 * @param geometricMean the geometricMean to set
	 */
	public void setGeometricMean(double geometricMean) {
		this.geometricMean = geometricMean;
	}

	/**
	 * @return the min
	 */
	public double getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(double min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public double getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(double max) {
		this.max = max;
	}

	/**
	 * @return the sum
	 */
	public double getSum() {
		return sum;
	}

	/**
	 * @param sum the sum to set
	 */
	public void setSum(double sum) {
		this.sum = sum;
	}

	/**
	 * @return the variance
	 */
	public double getVariance() {
		return variance;
	}

	/**
	 * @param variance the variance to set
	 */
	public void setVariance(double variance) {
		this.variance = variance;
	}

	/**
	 * @return the average
	 */
	public double getAverage() {
		return average;
	}

	/**
	 * @param average the average to set
	 */
	public void setAverage(double average) {
		this.average = average;
	}

	/**
	 * @param label
	 */
	public BasicStatisticsRequest(String label) {
		this(label,null);
	}

	/**
	 * @param label
	 * @param values
	 */
	public BasicStatisticsRequest(String label, double[] values) {
		this.label = label;
		this.values = values;
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the values
	 */
	public double[] getValues() {
		return values;
	}
	/**
	 * @param values the values to set
	 */
	public void setValues(double[] values) {
		this.values = values;
	}
	
	public String toString() {
		StringBuilder val = new StringBuilder(this.getClass().getName());
		val.append("\n");
		val.append("Label   : ").append(getLabel()).append("\n");;
		val.append("Values  :").append(java.util.Arrays.toString(getValues())).append("\n");;
		val.append("========================================================\n");
		val.append("Minimum        : ").append(getMin()).append("\n");;
		val.append("Maximum        : ").append(getMax()).append("\n");;
		val.append("Average        : ").append(getAverage()).append("\n");;
		val.append("Variance       : ").append(getVariance()).append("\n");;
		val.append("Geometric Mean :  ").append(getGeometricMean()).append("\n");;
		
		return val.toString();
	}
}
