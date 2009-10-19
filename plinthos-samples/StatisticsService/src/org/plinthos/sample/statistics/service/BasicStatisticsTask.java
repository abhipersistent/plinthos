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
package org.plinthos.sample.statistics.service;

import org.apache.commons.math.stat.StatUtils;
import org.plinthos.sample.statistics.data.BasicStatisticsRequest;
import org.plinthos.shared.plugin.PlinthosTask;
import org.plinthos.shared.plugin.PlinthosTaskStatus;

import com.thoughtworks.xstream.XStream;

/**
 * This class implements a basic statistical calculation for a dataset.
 * The Apache commons.math package is employed for this purpose.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class BasicStatisticsTask extends PlinthosTask {

	/*
	 * @see org.plinthos.sample.statistics.service.BasicStatistics#execute(java.lang.String,
	 *      java.lang.String)
	 */
	public String execute(String requestId, String xmlData) {
		
		XStream xstream = new XStream();
		xstream.setClassLoader(this.getClass().getClassLoader());
		BasicStatisticsRequest request = (BasicStatisticsRequest) xstream.fromXML(xmlData);
		
		evaluateStatistics(request);
		
		System.out.println("Basic Statistics for request entitled: "+ request.getLabel());
		System.out.println(request.toString());

		String taskStatus = PlinthosTaskStatus.COMPLETED;
		if( ctx.isTaskCancelled() ) {
			System.out.println("Task detected that it was cancelled");
			taskStatus = PlinthosTaskStatus.CANCELLED;
		}
		
		return taskStatus;
	}

	/**
	 * This method is responsible for getting the sum of two numbers
	 * 
	 * @param request
	 * @return
	 */
	private void evaluateStatistics(BasicStatisticsRequest request) {
		
		double[] x = request.getValues();
		
		request.setMax(StatUtils.max(x));
		request.setMin(StatUtils.min(x));
		request.setSum(StatUtils.sum(x));
		request.setAverage(StatUtils.mean(x));
		request.setGeometricMean(StatUtils.geometricMean(x));
		request.setVariance(StatUtils.variance(x));
		// pause for 10 second
		try {
			Thread.sleep(10000);
		}
		catch(InterruptedException e) {
			// ignore
		}
	}
}
