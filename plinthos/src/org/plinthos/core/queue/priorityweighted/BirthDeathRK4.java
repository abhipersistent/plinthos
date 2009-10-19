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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


/**
 * A simple Rung-Kutta solver for Birth-Death Queueing system.
 * 
 * @author <a href="mailto:babis.marmanis@gmail.com">Babis Marmanis</a>
 * @version 1.0
 */
public class BirthDeathRK4 {

	private static final Logger log = Logger.getLogger(BirthDeathRK4.class.getName());

	/** The integration infinitesimal */
	private double dt;

	/** Total number of iterations */
	private int ntotal;

	/** Total number of degrees of freedom */
	private int dof;

	/** The time interval (in seconds) that this simulation runs for */
	int timeInterval = MathConstants.ZERO_INT;

	/** This array holds the coefficients that govern the dynamics of the system. */
	public V3d[] coef;

	/** This array holds the Initial Conditions for the probabilities */
	public double[] ic;

	public BirthDeathRK4(int dof, int ntotal, int timeInterval) {

		this.dof = dof;
		this.ntotal = ntotal;

		this.timeInterval = timeInterval;

		// Define the time-step
		dt = (double) timeInterval / ntotal;

		// Initial Conditions
		ic = new double[dof + 1];

		// ----------------- INITIAL CONDITIONS -------------------
		// At t=0, the probability that we have no users is exactly 1
		// which means that ... we have no users!
		//
		// If the initial conditions are different you should call the
		// setInitialConditions(...) method BEFORE you call solve(...)
		//
		ic[0] = MathConstants.ONE_DOUBLE;

		for (int i = 1; i < dof; i++) {
			ic[i] = MathConstants.ZERO_DOUBLE;
		}
		// -----------------------------------------------------------

	}

	public void setInitialConditions(double[] val) {
		ic = val;
	}

	public double[] solve(double[] lambda, double[] mu) {

		//
		// L_(k-1), -(L_k + M_k), M_(k+1)
		// coef[.][0], coef[.][1], coef[.][2]
		//
		// where L_k: The rate at which births occur when the population is of
		// size k
		// M_k: The rate at which deaths occur when the population is of size k
		//

		// Initial Conditions
		coef = new V3d[dof + 1];

		for (int i = 0; i < dof; i++) {

			if (i > 0 && i < dof - 1) {

				coef[i] = new V3d(lambda[i - 1], -(lambda[i] + mu[i]), mu[i]);

			} else if (i == 0) {

				coef[i] = new V3d(MathConstants.ZERO_DOUBLE, -(lambda[i] + mu[i]), mu[i]);

			} else if (i == dof - 1) {

				coef[i] = new V3d(lambda[i - 1], -(lambda[i] + mu[i]), MathConstants.ZERO_DOUBLE);

			}
		}

		// The Runge-Kuta variables
		double[] k1 = new double[dof + 1];
		double[] k2 = new double[dof + 1];
		double[] k3 = new double[dof + 1];
		double[] k4 = new double[dof + 1];

		// Two dummy variables.
		double prb = MathConstants.ZERO_DOUBLE;

		long beginTime = System.currentTimeMillis();

		for (int i = 0; i < ntotal; i++) {

			// ////////////////////
			// First step of RK4 /
			// ////////////////////
			try {

				for (int osc = 0; osc < dof; osc++) {

					if (osc > 0 && osc < dof - 1) {

						prb = dt
								* (coef[osc].getX1() * ic[osc - 1] + coef[osc].getX2() * ic[osc] + coef[osc].getX3()
										* ic[osc + 1]);

					} else if (osc == 0) {

						prb = dt * (coef[osc].getX2() * ic[osc] + coef[osc].getX3() * ic[osc + 1]);

					} else if (osc == dof - 1) {

						double sumP = MathConstants.ZERO_DOUBLE;

						for (int j = 0; j < dof; j++) {
							sumP += ic[j];
						}

						prb = dt
								* ((MathConstants.ONE_DOUBLE - sumP) + coef[osc].getX1() * ic[osc - 1] + coef[osc].getX2()
										* ic[osc]);
					}

					k1[osc] = prb;

				}

			} catch (Exception eX) {
				eX.printStackTrace();
			}

			// /////////////////////
			// Second step of RK4 /
			// /////////////////////
			try {

				for (int osc = 0; osc < dof; osc++) {

					if (osc > 0 && osc < dof - 1) {

						prb = dt
								* (coef[osc].getX1() * (ic[osc - 1] + 0.5 * k1[osc - 1]) + coef[osc].getX2()
										* (ic[osc] + 0.5 * k1[osc]) + coef[osc].getX3()
										* (ic[osc + 1] + 0.5 * k1[osc + 1]));

					} else if (osc == 0) {

						prb = dt
								* (coef[osc].getX2() * (ic[osc] + 0.5 * k1[osc]) + coef[osc].getX3()
										* (ic[osc + 1] + 0.5 * k1[osc + 1]));

					} else if (osc == dof - 1) {

						double sumP = MathConstants.ZERO_DOUBLE;

						for (int j = 0; j < dof; j++) {
							sumP += ic[j];
						}

						prb = dt
								* ((MathConstants.ONE_DOUBLE - sumP) + coef[osc].getX1()
										* (ic[osc - 1] + 0.5 * k1[osc - 1]) + coef[osc].getX2()
										* (ic[osc] + 0.5 * k1[osc]));
					}

					k2[osc] = prb;
				}

			} catch (Exception eX) {
				log.error(eX.getMessage());
			}

			// ////////////////////
			// Third step of RK4 /
			// ////////////////////
			try {

				for (int osc = 0; osc < dof; osc++) {

					if (osc > 0 && osc < dof - 1) {

						prb = dt
								* (coef[osc].getX1() * (ic[osc - 1] + 0.5 * k2[osc - 1]) + coef[osc].getX2()
										* (ic[osc] + 0.5 * k2[osc]) + coef[osc].getX3()
										* (ic[osc + 1] + 0.5 * k2[osc + 1]));

					} else if (osc == 0) {

						prb = dt
								* (coef[osc].getX2() * (ic[osc] + 0.5 * k2[osc]) + coef[osc].getX3()
										* (ic[osc + 1] + 0.5 * k2[osc + 1]));

					} else if (osc == dof - 1) {

						double sumP = MathConstants.ZERO_DOUBLE;

						for (int j = 0; j < dof; j++) {
							sumP += ic[j];
						}

						prb = dt
								* ((MathConstants.ONE_DOUBLE - sumP) + coef[osc].getX1()
										* (ic[osc - 1] + 0.5 * k2[osc - 1]) + coef[osc].getX2()
										* (ic[osc] + 0.5 * k2[osc]));
					}

					k3[osc] = prb;
				}
			} catch (Exception eX) {
				log.error(eX.getMessage());
			}

			// /////////////////////
			// Fourth step of RK4 /
			// /////////////////////
			try {

				for (int osc = 0; osc < dof; osc++) {

					if (osc > 0 && osc < dof - 1) {

						prb = dt
								* (coef[osc].getX1() * k3[osc - 1] + coef[osc].getX2() * k3[osc] + coef[osc].getX3()
										* k3[osc + 1]);

					} else if (osc == 0) {

						prb = dt * (coef[osc].getX2() * k3[osc] + coef[osc].getX3() * k3[osc + 1]);

					} else if (osc == dof - 1) {

						double sumP = MathConstants.ZERO_DOUBLE;

						for (int j = 0; j < dof; j++) {
							sumP += k3[j];
						}

						prb = dt
								* ((MathConstants.ONE_DOUBLE - sumP) + coef[osc].getX1() * k3[osc - 1] + coef[osc].getX2()
										* k3[osc]);
					}

					k4[osc] = prb;
				}
			} catch (Exception eX) {
				log.error(eX.getMessage());
			}

			// Next time step
			for (int osc = 0; osc < dof; osc++) {

				ic[osc] += MathConstants.ONE_OVER_SIX * (k1[osc] + (2.0 * k2[osc]) + (2.0 * k3[osc]) + k4[osc]);

			}

		} // -- End of time loop

		// Before we return the values we zero the last probability.
		ic[dof - 1] = 0.0;

		// Measure the solver's time
		long execTime = System.currentTimeMillis() - beginTime;

		log.debug("Total Execution Time: " + execTime + " (milli-seconds)");

		return ic;
	}

	public static void main(String[] args) {

		// Set up the LOG4J configuration based on the log4j.properties file
		// PropertyConfigurator.configure("log4j.properties");

		BasicConfigurator.configure();
		
		log.info("\n  BirthDeathRK4 Solver \n   by Babis Marmanis \n");

		int dof = Integer.parseInt(args[0]);

		BirthDeathRK4 bdrk4 = new BirthDeathRK4(dof, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		log.info("BirthDeathRK4 has been initialized.");

		double[] lambda = new double[dof];
		double[] mu = new double[dof];

		for (int i = 0; i < dof; i++) {
			lambda[i] = 0.5;
			mu[i] = 0.4; // Constants.ZERO_DOUBLE;
		}

		double[] prob = bdrk4.solve(lambda, mu);

		for (int i = 0; i < dof; i++) {
			log.info("prob[" + i + "] = " + prob[i]);
		}

		log.info("****************************************************");
		log.info("***  CALCULATION HAS BEEN COMPLETED SUCCESSFULLY ***");
		log.info("****************************************************");
	}
}
