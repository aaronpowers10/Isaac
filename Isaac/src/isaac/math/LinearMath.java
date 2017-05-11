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

import static java.lang.Math.abs;

/**
 * This class contains static methods relating to linear algebra.
 *
 * @author Aaron Powers
 *
 */
public class LinearMath {

	/**
	 * Creates the identity matrix with the given size.
	 *
	 * @param size
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Evaluatable> Matrix<T> identity(int size) {
		Matrix<T> mat = new Matrix<T>(size, size);
		for (int i = 0; i < size; i++) {
			mat.set(i, i, (T)(new Numeric(1.0)));
		}
		return mat;
	}

	/**
	 * Returns the augmented matrix created by combining the two matrices side
	 * by side.
	 *
	 * @param mat1
	 * @param mat2
	 * @return
	 * @throws ComputationException
	 */
	public static <T extends Evaluatable> Matrix<T> augment(Matrix<T> mat1, Matrix<T> mat2) throws ComputationException {
		if (mat1.numRows() != mat2.numRows()) {
			throw new ComputationException("Attempted matrix augmentation without equal number of rows.");
		}
		Matrix<T> augMat = new Matrix<T>(mat1.numRows(), mat1.numColumns() + mat2.numColumns());
		for (int i = 0; i < augMat.numColumns(); i++) {
			if (i < mat1.numColumns()) {
				augMat.setColumn(i, mat1.getColumn(i));
			} else {
				int j = i - mat1.numColumns();
				augMat.setColumn(i, mat2.getColumn(j));
			}
		}
		return augMat;
	}

	/**
	 * Returns the submatrix which contains all rows of the original matrix and
	 * columns from startColumn to endColumn.
	 *
	 * @param startColumn
	 *            The index of the column to start the submatrix.
	 * @param endColumn
	 *            The index of the column to terminate the submatrix.
	 * @param mat
	 *            An input matrix.
	 * @return The submatrix defined by the start and end columns.
	 */
	public static <T extends Evaluatable> Matrix<T> subMatrix(int startColumn, int endColumn, Matrix<T> mat) {
		Matrix<T> sub = new Matrix<T>(mat.numRows(), endColumn - startColumn);
		int j = 0;
		for (int i = startColumn; i < endColumn; i++) {
			sub.setColumn(j, mat.getColumn(i));
			j++;
		}

		return sub;
	}

	/**
	 *
	 * Performs matrix multiplication and returns a new matrix as result.
	 *
	 *
	 * @param mat1
	 *            First matrix in multiplication.
	 * @param mat2
	 *            Second matrix in multiplication
	 * @return Result of multiplication as Matrix.
	 * @throws ComputationException
	 */

	public static <T extends Evaluatable> Matrix<T> multiply(Matrix<T> mat1, Matrix<T> mat2) throws ComputationException {
		if (mat1.numColumns() != mat2.numRows()) {
			throw new ComputationException("Illegal matrix multiplication: Number of columns in matrix 1 do not match"
					+ " the number of rows in matrix 2");
		}
		Matrix<T> result = new Matrix<T>(mat1.numColumns(), mat2.numRows());
		for (int i = 0; i < mat1.numRows(); i++) {
			for (int j = 0; j < mat2.numColumns(); j++) {
				result.set(i, j, dotProduct(mat1.getRow(i), mat2.getColumn(j)));
			}
		}
		return result;
	}

	/**
	 * Performs a dot product between two vector objects.
	 *
	 * @param vector1
	 *            First vector in dot product.
	 * @param vector2
	 *            Second vector in dot product.
	 * @return Result of dot product.
	 * @throws ComputationException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Evaluatable, U extends Evaluatable> T dotProduct(Vector<T> vector1, Vector<U> vector2)  {
		if (vector1.size() != vector2.size()) {
			System.out.println(vector1.size() + " " + vector2.size());
			throw new ComputationException("Illegal vector dot product: Vectors are not of equal size");
		}
		double result = 0;
		for (int i = 0; i < vector1.size(); i++) {
			result = result + vector1.get(i).get() * vector2.get(i).get();
		}
		return (T)(new Numeric(result));
	}

	/**
	 * Performs the multiplication of a matrix by a scaler. The resulting matrix
	 * will have dimensions equal to the input matrix.
	 *
	 * @param mat
	 *            Matrix in multiplication.
	 * @param scalar
	 *            Scalar in multiplication.
	 * @return The resulting Matrix of the multiplication.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Evaluatable> Matrix<T> multiply(Matrix<T> mat, T scalar) {
		Matrix<T> result = new Matrix<T>(mat.numRows(), mat.numColumns());
		for (int i = 0; i < mat.numRows(); i++) {
			for (int j = 0; j < mat.numColumns(); j++) {
				result.set(i, j, (T)(new Numeric(scalar.get() * mat.get(i, j).get())));
			}
		}
		return result;
	}

	/**
	 * Performs the multiplication of a matrix and a vector which results in a
	 * vector of size equal to the number of columns of the input matrix.
	 *
	 *
	 * @param mat
	 *            Matrix in multiplication.
	 * @param vect
	 *            Vector in multiplication
	 * @return Resulting vector in array list form.
	 * @throws ComputationException
	 */
	public static <T extends Evaluatable, U extends Evaluatable> Vector<T> multiply(Matrix<T> mat, Vector<U> vect){
		if (mat.numRows() != vect.size()) {
			throw new ComputationException(
					"Illegal matrix-vector multiplication: The number of rows of the matrix should equal"
							+ "the size of the vector");
		}
		Vector<T> result = new Vector<T>();
		for (int i = 0; i < mat.numRows(); i++) {
			result.add (dotProduct(mat.getRow(i), vect));
		}
		return result;
	}

	public static <T extends Evaluatable> Vector<T> multiply(Vector<T> vect, Matrix<T> mat) {
		if (mat.numColumns() != vect.size()) {
			throw new ComputationException(
					"Illegal matrix-vector multiplication: The number of columns of the matrix should equal"
							+ "the size of the vector");
		}
		Vector<T> result = new Vector<T>();
		for (int i = 0; i < mat.numColumns(); i++) {
			result.add((dotProduct(mat.getColumn(i), vect)));
		}
		return result;
	}

	/**
	 * Performs multiplication of a vector by a scalar.
	 *
	 * @param vect
	 * @param scalar
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static  <T extends Evaluatable> Vector<T> multiply(Vector<T> vect, T scalar) {
		Vector<T> result = new Vector<T>();
		for (int i = 0; i < vect.size(); i++) {
			result.add((T)(new Numeric(scalar.get() * vect.get(i).get())));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static  <T extends Evaluatable> Vector<T> multiply(Vector<T> vect, double scalar) {
		Vector<T> result = new Vector<T>();
		for (int i = 0; i < vect.size(); i++) {
			result.add( (T)(new Numeric(scalar * vect.get(i).get())));
		}
		return result;
	}

	/**
	 * Performs division of a vector by a scalar.
	 *
	 * @param vect
	 * @param scalar
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Evaluatable> Vector<T> divide(Vector<T> vect, T scalar) {
		Vector<T> result = new Vector<T>();
		for (int i = 0; i < vect.size(); i++) {
			result.add( (T)(new Numeric(vect.get(i).get() / scalar.get())));
		}
		return result;
	}

	/**
	 * Performs the addition of two matrices. Simply adds each item of the same
	 * index.
	 *
	 * @param mat1
	 *            The first matrix in addition.
	 * @param mat2
	 *            The second matrix in addition.
	 * @return The result of the addition.
	 * @throws ComputationException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Evaluatable> Matrix<T> add(Matrix<T> mat1, Matrix<T> mat2) {
		if ((mat1.numRows() != mat2.numRows() || (mat1.numColumns() != mat2.numColumns()))) {
			throw new ComputationException("Illegal matrix addition: The two matrices must be of equal size.");
		}
		Matrix<T> result = new Matrix<T>(mat1.numRows(), mat1.numColumns());
		for (int i = 0; i < mat1.numRows(); i++) {
			for (int j = 0; j < mat1.numColumns(); j++) {
				result.set(i, j, (T)(new Numeric(mat1.get(i, j).get() + mat2.get(i, j).get())));
			}

		}
		return result;
	}

	/**
	 * Performs the addition of two vector objects.
	 *
	 * @param vect1
	 *            The first vector in the addition.
	 * @param vect2
	 *            The second vector in the addition.
	 * @return The result of the addition.
	 * @throws ComputationException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Evaluatable, U extends Evaluatable> Vector<T> add(Vector<T> vect1, Vector<U> vect2) {
		if (vect1.size() != vect2.size()) {
			throw new ComputationException("Illegal vector addition: The two vectors must be of equal size.");
		}
		Vector<T> result = new Vector<T>();
		for (int i = 0; i < vect1.size(); i++) {
			result.add((T)(new Numeric(vect1.get(i).get() + vect2.get(i).get())));
		}
		return result;
	}

	/**
	 * Performs the subtraction. of two vector objects.
	 *
	 * @param vect1
	 *            The first vector in the subtraction.
	 * @param vect2
	 *            The second vector in the subtraction.
	 * @return The result of the subtraction.
	 * @throws ComputationException
	 */
	@SuppressWarnings("unchecked")
	public static  <T extends Evaluatable, U extends Evaluatable, V extends Evaluatable> Vector<V> subtract(Vector<T> vect1, Vector<U> vect2) {
		if (vect1.size() != vect2.size()) {
			throw new ComputationException("Illegal vector subtraction: The two vectors must be of equal size.");
		}
		Vector<V> result = new Vector<V>();
		for (int i = 0; i < vect1.size(); i++) {
			result.add( (V)(new Numeric(vect1.get(i).get() - vect2.get(i).get())));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Evaluatable> Vector<T> subtract(Vector<T> vect1, T scalar) {

		Vector<T> result = new Vector<T>();
		for (int i = 0; i < vect1.size(); i++) {
			result.add( (T)(new Numeric(vect1.get(i).get() - scalar.get())));
		}
		return result;
	}

	/**
	 * Returns the reduced row echelon form of the input matrix using the
	 * Gauss-Jordan algorithm with pivoting.
	 *
	 * @param mat
	 * @return
	 * @throws ComputationException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Evaluatable> Matrix<T> reducedRowEchelon(Matrix<T> mat){
		Matrix<T> result = mat.copy();
		for (int k = 0; k < mat.numRows(); k++) {
			double maxPiv = 0;
			int pivIndex = k;
			for (int i = k; i < mat.numRows(); i++) {
				if (abs(result.get(i, k).get()) > maxPiv) {
					maxPiv = abs(result.get(i, k).get());
					pivIndex = i;
				}
			}
			result.swap(k, pivIndex);

			result.setRow(k, divide(result.getRow(k), result.getRow(k).get(k)));
			for (int i = 0; i < mat.numRows(); i++) {
				if (i != k) {
					result.addRowTo(k, i, (T)(new Numeric(-result.get(i, k).get())));
				}
			}
		}
		return result;
	}
}
