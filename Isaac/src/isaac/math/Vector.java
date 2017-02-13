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

import java.util.ArrayList;

public class Vector<T extends Evaluatable<T>> {

	private ArrayList<T> vals;

	/**
	 * Constructs a blank vector.
	 */
	public Vector() {
		vals = new ArrayList<T>();
	}

	/**
	 * Constructs the vector with the given size. All elements initialized to
	 * zero.
	 * 
	 * @param n
	 *            The size of the vector.
	 */
	@SuppressWarnings("unchecked")
	public Vector(int n) {
		vals = new ArrayList<T>(n);
		for (int i = 0; i < n; i++) {
			vals.add((T) (new Numeric(0)));
		}
	}

	/**
	 * Adds a new value to the vector.
	 * 
	 * @param value
	 */
	public void add(T value) {
		vals.add(value);
	}

	/**
	 * Removes the value at the given index.
	 * 
	 * @param index
	 */
	public void remove(int index) {
		vals.remove(index);
	}

	/**
	 * Sets the value of the vector element at the given index.
	 * 
	 * @param index
	 * @param value
	 */
	public void set(int index, T value) {
		vals.set(index, value);
	}

	/**
	 * Returns the value of the element with the given index.
	 * 
	 * @param index
	 * @return
	 */
	public T get(int index) {
		return vals.get(index);
	}

	/**
	 * Returns the size of the vector.
	 * 
	 * @return
	 */
	public int size() {
		return vals.size();
	}

	/**
	 * Returns a value clone of the current vector.
	 */
	public Vector<T> copy() {
		Vector<T> cloneVect = new Vector<T>();
		for (int i = 0; i < size(); i++) {
			cloneVect.add(get(i).copy());
		}
		return cloneVect;
	}

	/**
	 * Creates a string of the vector values.
	 */
	public String write() {
		String result = "";
		for (int i = 0; i < size(); i++) {
			result = result + Double.toString(get(i).get()) + " ";
		}
		return result;
	}

	/**
	 * Tests if the current vector is value equivalent to the input vector.
	 * 
	 * @param vect
	 *            The other vector to compare.
	 * @return True if the two vectors are value equivalent.
	 */
	public boolean numericallyEquals(Vector<T> vect) {
		if (vect.size() != this.size()) {
			return false;
		} else {
			for (int i = 0; i < size(); i++) {
				if (!vect.get(i).numericallyEquals(this.get(i))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Sets the current vector value equivalent to the input vector.
	 * 
	 * @param vect
	 *            Values of this vector will be used to populate the current
	 *            vector
	 */
	public void setEqual(Vector<T> vect) {
		for (int i = 0; i < vect.size(); i++) {
			this.set(i, vect.get(i).copy());
		}
	}

	public double distanceTo(Vector<T> vect) {
		double sum = 0;
		for (int i = 0; i < size(); i++) {
			sum += (get(i).get() - vect.get(i).get()) * (get(i).get() - vect.get(i).get());
		}
		return Math.sqrt(sum);
	}

}
