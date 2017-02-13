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

import static isaac.math.LinearMath.add;
import static isaac.math.LinearMath.augment;
import static isaac.math.LinearMath.identity;
import static isaac.math.LinearMath.multiply;
import static isaac.math.LinearMath.reducedRowEchelon;
import static isaac.math.LinearMath.subMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Matrix class represents a 2D matrix of numerical values.
 *
 * @author Aaron Powers
 *
 */

public class Matrix<T extends Evaluatable<T>> {
	/** The values of the matrix in array form. */
	private ArrayList<Vector<T>> vals;

	/**
	 * Constructs matrix of given rows and columns with all zero elements.
	 *
	 * @param numRows
	 *            The number of rows.
	 * @param numColumns
	 *            The number of columns.
	 */
	public Matrix(int numRows, int numColumns) {
		setSize(numRows, numColumns);
	}

	/**
	 * Sets the size of the matrix and initializes all elements to zero.
	 * 
	 * @param numRows
	 * @param numColumns
	 */
	public void setSize(int numRows, int numColumns) {
		vals = new ArrayList<Vector<T>>();
		for (int i = 0; i < numRows; i++) {
			Vector<T> vect = new Vector<T>(numColumns);
			vals.add(vect);
		}
	}

	/**
	 * Returns the number of rows in the matrix.
	 * 
	 * @return
	 */
	public int numRows() {
		return vals.size();
	}

	/**
	 * Returns the number of columns in the matrix.
	 * 
	 * @return
	 */
	public int numColumns() {
		return vals.get(0).size();
	}

	/**
	 *
	 * @param rowIndex
	 * @param colIndex
	 * @param value
	 */
	public void set(int rowIndex, int colIndex, T value) {
		vals.get(rowIndex).set(colIndex, value);
	}

	/**
	 *
	 * @param rowIndex
	 * @param colIndex
	 * @return
	 */
	public T get(int rowIndex, int colIndex) {
		return vals.get(rowIndex).get(colIndex);
	}

	/**
	 * Returns a clone of the column at the given index.
	 * 
	 * @param index
	 * @return
	 */
	public Vector<T> getColumn(int index) {
		Vector<T> result = new Vector<T>();
		for (int i = 0; i < numRows(); i++) {
			result.add(get(i, index));
		}
		return result;
	}

	/**
	 * Returns a clone of the row at the given index.
	 * 
	 * @param index
	 * @return
	 */
	public Vector<T> getRow(int index) {
		return vals.get(index).copy();
	}

	/**
	 * Sets the column at the given index equal to a clone of the input vector.
	 * 
	 * @param index
	 * @param vector
	 */
	public void setColumn(int index, Vector<T> vector) {
		for (int i = 0; i < numRows(); i++) {
			set(i, index, vector.get(i));
		}
	}

	/**
	 * Sets the row at the given index equal to the input vector.
	 * 
	 * @param index
	 * @param vector
	 */
	public void setRow(int index, Vector<T> vector) {
		vals.set(index, vector);
	}

	/**
	 * Swaps the values of rows i and j.
	 * 
	 * @param i
	 * @param j
	 */
	public void swap(int i, int j) {
		Vector<T> tempRow = getRow(i);
		setRow(i, getRow(j));
		setRow(j, tempRow);
	}

	/**
	 * Multiplies row i by a scalar
	 * 
	 * @param i
	 * @param scalar
	 */
	public void multiplyRow(int i, T scalar) {
		setRow(i, multiply(getRow(i), scalar));
	}

	/**
	 * Adds a scalar multiply of row i to row j
	 * 
	 * @param i
	 * @param j
	 * @param scalar
	 */
	public void addRowTo(int i, int j, T scalar) {

		setRow(j, add(multiply(getRow(i), scalar), getRow(j)));
	}

	/**
	 * Returns the transpose of the current matrix.
	 *
	 * @return
	 */
	public Matrix<T> transpose() {
		Matrix<T> transMatrix = new Matrix<T>(numColumns(), numRows());
		for (int i = 0; i < numColumns(); i++) {
			for (int j = 0; j < numRows(); j++) {
				transMatrix.set(i, j, get(j, i));
			}
		}
		return transMatrix;
	}

	/**
	 * Returns the inverse of the current matrix.
	 *
	 * @return
	 */
	public Matrix<T> inverse() throws ComputationException {
		Matrix<T> reduced = reducedRowEchelon(augment(this, identity(numRows())));
		Matrix<T> id = subMatrix(0, numColumns(), reduced);
		Matrix<T> inv = subMatrix(numColumns(), reduced.numColumns(), reduced);

		if (id.numericallyEquals(identity(numRows()))) {
			return inv;
		} else {
			throw new ComputationException("Matrix inverse error: Singular Matrix");
		}
	}

	/**
	 * Creates a value clone of the current matrix.
	 */
	public Matrix<T> copy() {
		Matrix<T> cloneMat = new Matrix<T>(numRows(), numColumns());
		for (int i = 0; i < numRows(); i++) {
			cloneMat.setRow(i, getRow(i));
		}
		return cloneMat;
	}

	/**
	 * Returns string of matrix values.
	 */
	public String write() {
		String result = "";
		for (int i = 0; i < numRows(); i++) {
			result = result + getRow(i).write() + "\n";
		}
		return result;
	}

	public Vector<T> toVector() {

		if (numRows() == 1) {
			return getRow(0);
		} else if (numColumns() == 1) {
			return getColumn(0);
		} else {
			return null;
		}
	}

	/**
	 * Tests if the current matrix is value equivalent to the input matrix.
	 * 
	 * @param mat
	 *            The other matrix to compare.
	 * @return True if the two matrices are value equivalent.
	 */
	public boolean numericallyEquals(Matrix<T> mat) {
		if (mat.numRows() != this.numRows()) {
			return false;
		} else {
			for (int i = 0; i < numRows(); i++) {
				if (!mat.getRow(i).numericallyEquals(this.getRow(i))) {
					return false;
				}
			}
		}
		return true;
	}

	public double determinant() {
		if(numRows()!=numColumns()){
			throw new ComputationException("Cannot compute determinant of non-square matrix.");
		}
		
		double result = 0;
		if (numRows() == 1) {
			result = get(0, 0).get();
		} else {
			for (int i = 0; i < numRows(); i++) {
				Matrix<T> mat = new Matrix<T>(numRows() - 1, numColumns() - 1);
				for (int rowIndex = 1; rowIndex < numRows(); rowIndex++) {
					int subColIndex = 0;
					for (int colIndex = 0; colIndex < numColumns(); colIndex++) {
						if (colIndex != i){
							mat.set(rowIndex - 1, subColIndex, get(rowIndex, colIndex).copy());
							subColIndex++;
						}
					}
				}
				result += Math.pow(-1, i ) * get(0, i).get() * mat.determinant();
			}
		}
		return result;
	}
	
	public static void main(String[] args){
		Matrix<Numeric> matrix = new Matrix<Numeric>(4,4);
		try {
			Scanner in = new Scanner(new File("C:\\aaron\\Matrix1.txt"));
			int rowIndex = 0;
			while(in.hasNextLine()){
				String line = in.nextLine();
				Scanner in2 = new Scanner(line);
				int columnIndex = 0;
				while(in2.hasNextDouble()){
					matrix.set(rowIndex, columnIndex, new Numeric(in2.nextDouble()));
					columnIndex++;
				}
				rowIndex++;
				in2.close();
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(matrix.determinant());
	}

}
