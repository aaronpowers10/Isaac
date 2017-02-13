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
package isaac.math;

public class Numeric implements Evaluatable<Numeric>, Setable {
	
	private double value;
	
	public Numeric(double value){
		set(value);
	}

	@Override
	public void set(double value) {
		this.value = value;
	}

	@Override
	public double get() {
		return value;
	}

	@Override
	public Numeric copy() {
		return new Numeric(value);
	}

}