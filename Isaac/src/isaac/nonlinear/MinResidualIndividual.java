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

import isaac.math.BoundedNumeric;
import isaac.math.Evaluatable;
import isaac.math.Vector;

public class MinResidualIndividual extends Individual{
	
	Vector<BoundedNumeric> x;
	private  Vector<Evaluatable> residuals;
	
	public MinResidualIndividual(Vector<BoundedNumeric> x, Vector<Evaluatable> residuals){
		this.x = x;
		this.residuals = residuals;
	}

	@Override
	public double fitness() {
		double max = 0;
		for(int i=0;i<residuals.size();i++){
			double residual = residuals.get(i).get();
			if(residual>max){
				max = residual;
			}
		}
		return max;
	}

	@Override
	public Individual copy() {
		return new MinResidualIndividual(x.copy(),residuals.copy());
	}

}
