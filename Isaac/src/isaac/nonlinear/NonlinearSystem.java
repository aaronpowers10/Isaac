/*
 *
 *  Copyright (C) 2017 Aaron Powers
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package isaac.nonlinear;

import static isaac.math.LinearMath.add;
import static isaac.math.LinearMath.identity;
import static isaac.math.LinearMath.multiply;
import static isaac.math.LinearMath.subtract;

import isaac.math.BoundedNumeric;
import isaac.math.ComputationException;
import isaac.math.Evaluatable;
import isaac.math.Matrix;
import isaac.math.Numeric;
import isaac.math.Vector;

public class NonlinearSystem implements Runnable {

	private Vector<BoundedNumeric> x;
	private Vector<BoundedNumeric> previousX;
	private Vector<Numeric> dx;
	private Vector<Evaluatable> residuals;
	private int iteration;
	private boolean isCanceled;
	private Matrix<Numeric> jacobian;
	private Matrix<Numeric> stepMatrix;
	private Matrix<Numeric> identity;
	private Numeric lamda;
	private double initialLamda;
	private double geoFactor;
	private int maxIterations;
	private Optimizer preconditioner;
	private int maxTries;

	public NonlinearSystem() {
		x = new Vector<BoundedNumeric>();
		dx = new Vector<Numeric>();
		residuals = new Vector<Evaluatable>();
		lamda = new Numeric(0.0);
		maxTries = 10;
	}

	public void setInitialLamda(double initialLamda) {
		this.initialLamda = initialLamda;
	}

	public void setGeoFactor(double geoFactor) {
		this.geoFactor = geoFactor;
	}

	public void addResidual(Evaluatable newResidual) {
		residuals.add(newResidual);
	}

	public void addVariable(BoundedNumeric variable) {
		x.add(variable);
		dx.add(new Numeric(0.0));
	}

	/**
	 * Used for parallel runs to begin solution.
	 */
	public void run() {
		try {
			solve();
		} catch (ComputationException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Initiates the solution of the nonlinear system.
	 * 
	 * @throws ComputationException
	 */
	public void solve() throws ComputationException {

		previousX = x.copy();
		boolean solved = false;
		int tries = 0;
		while (!solved && tries < maxTries) {
			try {
				solveIterate();
				solved = true;
				geoFactor = Math.max(0.25, geoFactor * 0.5);
			} catch (ComputationException e) {
				System.out.println(e.getMessage());
				preconditioner.run(x, residuals, previousX);
				geoFactor = Math.min(0.99999, geoFactor + 0.5 * (1.0 - geoFactor));
				tries++;
			}
		}
		if(tries == maxTries){
			throw new ComputationException("System not solved after " + maxTries + " tries.");
		}

	}

	/**
	 * Solves the system of equations using an iterative solution of the form
	 * x_new = x_old - t*H*res where H is a matrix which depends on the
	 * algorithm being used, t is a scalar and res is the residuals vector
	 *
	 * stepMatrix := H
	 *
	 * All of these methods use the Jacobian in some form
	 * 
	 * @throws ComputationException
	 */
	private void solveIterate() throws ComputationException {
		
		jacobian = new Matrix<Numeric>(numEqns(), numEqns());
		identity = identity(numEqns());
		lamda.set(initialLamda);
		isCanceled = false;
		iteration = 0;
		
		while (!isStopCriteria() && !isCanceled) {
			try {
				setJacobian();
				setLamda();
				setStepMatrix();
				dx = multiply(stepMatrix, residuals);
				x.setEqual(subtract(x, dx));
				iteration++;
			} catch (ComputationException e) {
				throw new ComputationException(
						e.getMessage() + " On Iteration " + iteration + " Lamda = " + lamda.get());
			}
		}

		if (iteration > maxIterations - 1) {
			throw new ComputationException("System not solved in required iterations.");
		}
	}

	/**
	 * Sets the Jacobian matrix using finite differences.
	 */
	protected void setJacobian() {
		double y;
		double yInc;
		double stepSize = 0.0000000000001;
		for (int i = 0; i < numEqns(); i++) {
			for (int j = 0; j < numVars(); j++) {
				y = residuals.get(i).get();
				x.get(j).set( x.get(j).get() + stepSize);
				yInc = residuals.get(i).get();
				x.get(j).set( x.get(j).get() - stepSize);
				jacobian.get(i,j).set((yInc - y) / stepSize);
			}
		}
	}


	private void setStepMatrix() throws ComputationException {
		stepMatrix = multiply(add(multiply(jacobian.transpose(), jacobian), multiply(identity, lamda)).inverse(),
				jacobian.transpose());
	}

	private void setLamda() {
		lamda.set(lamda.get() * geoFactor);
	}

	private boolean isStopCriteria() {
		boolean result = true;

		for (int i = 0; i < numEqns(); i++) {
			if (Math.abs(residuals.get(i).get()) > 0.00001) {
				result = false;
			} else if (Double.isNaN(residuals.get(i).get())) {
				result = false;
			}
		}

		if (iteration > maxIterations) {
			result = true;
		}
		return result;
	}

	public int numVars() {
		return x.size();
	}

	public int numEqns() {
		return residuals.size();
	}
}
