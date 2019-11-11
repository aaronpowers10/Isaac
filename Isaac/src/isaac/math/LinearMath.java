/*     */ package isaac.math;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinearMath
/*     */ {
/*     */   public static Matrix identity(int size) {
/*  39 */     Matrix mat = new Matrix(size, size);
/*  40 */     for (int i = 0; i < size; i++) {
/*  41 */       mat.set(i, i, new Numeric(1.0D));
/*     */     }
/*  43 */     return mat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matrix augment(Matrix mat1, Matrix mat2) throws ComputationException {
/*  56 */     if (mat1.numRows() != mat2.numRows()) {
/*  57 */       throw new ComputationException("Attempted matrix augmentation without equal number of rows.");
/*     */     }
/*  59 */     Matrix augMat = new Matrix(mat1.numRows(), mat1.numColumns() + mat2.numColumns());
/*  60 */     for (int i = 0; i < augMat.numColumns(); i++) {
/*  61 */       if (i < mat1.numColumns()) {
/*  62 */         augMat.setColumn(i, mat1.getColumn(i));
/*     */       } else {
/*  64 */         int j = i - mat1.numColumns();
/*  65 */         augMat.setColumn(i, mat2.getColumn(j));
/*     */       } 
/*     */     } 
/*  68 */     return augMat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matrix subMatrix(int startColumn, int endColumn, Matrix mat) {
/*  84 */     Matrix sub = new Matrix(mat.numRows(), endColumn - startColumn);
/*  85 */     int j = 0;
/*  86 */     for (int i = startColumn; i < endColumn; i++) {
/*  87 */       sub.setColumn(j, mat.getColumn(i));
/*  88 */       j++;
/*     */     } 
/*     */     
/*  91 */     return sub;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matrix multiply(Matrix mat1, Matrix mat2) throws ComputationException {
/* 108 */     if (mat1.numRows() != mat2.numColumns()) {
/* 109 */       throw new ComputationException("Illegal matrix multiplication: Number of columns in matrix 1 do not match the number of rows in matrix 2");
/*     */     }
/*     */     
/* 112 */     Matrix result = new Matrix(mat1.numRows(), mat2.numColumns());
/* 113 */     for (int i = 0; i < mat1.numRows(); i++) {
/* 114 */       for (int j = 0; j < mat2.numColumns(); j++) {
/* 115 */         result.set(i, j, dotProduct(mat1.getRow(i), mat2.getColumn(j)));
/*     */       }
/*     */     } 
/* 118 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Evaluatable dotProduct(Vector vector1, Vector vector2) {
/* 133 */     if (vector1.size() != vector2.size()) {
/* 134 */       throw new ComputationException("Illegal vector dot product: Vectors are not of equal size");
/*     */     }
/* 136 */     double result = 0.0D;
/* 137 */     for (int i = 0; i < vector1.size(); i++) {
/* 138 */       result += vector1.get(i).get() * vector2.get(i).get();
/*     */     }
/* 140 */     return new Numeric(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matrix multiply(Matrix mat, Evaluatable scalar) {
/* 155 */     Matrix result = new Matrix(mat.numRows(), mat.numColumns());
/* 156 */     for (int i = 0; i < mat.numRows(); i++) {
/* 157 */       for (int j = 0; j < mat.numColumns(); j++) {
/* 158 */         result.set(i, j, new Numeric(scalar.get() * mat.get(i, j).get()));
/*     */       }
/*     */     } 
/* 161 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector multiply(Matrix mat, Vector vect) {
/* 177 */     if (mat.numColumns() != vect.size()) {
/* 178 */       throw new ComputationException(
/* 179 */           "Illegal matrix-vector multiplication: The number of rows of the matrix should equalthe size of the vector");
/*     */     }
/*     */     
/* 182 */     Vector result = new Vector();
/* 183 */     for (int i = 0; i < mat.numRows(); i++) {
/* 184 */       result.add(dotProduct(mat.getRow(i), vect));
/*     */     }
/* 186 */     return result;
/*     */   }
/*     */   
/*     */   public static Vector multiply(Vector vect, Matrix mat) {
/* 190 */     if (mat.numColumns() != vect.size()) {
/* 191 */       throw new ComputationException(
/* 192 */           "Illegal matrix-vector multiplication: The number of columns of the matrix should equalthe size of the vector");
/*     */     }
/*     */     
/* 195 */     Vector result = new Vector();
/* 196 */     for (int i = 0; i < mat.numColumns(); i++) {
/* 197 */       result.add(dotProduct(mat.getColumn(i), vect));
/*     */     }
/* 199 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector multiply(Vector vect, Evaluatable scalar) {
/* 211 */     Vector result = new Vector();
/* 212 */     for (int i = 0; i < vect.size(); i++) {
/* 213 */       result.add(new Numeric(scalar.get() * vect.get(i).get()));
/*     */     }
/* 215 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vector multiply(Vector vect, double scalar) {
/* 220 */     Vector result = new Vector();
/* 221 */     for (int i = 0; i < vect.size(); i++) {
/* 222 */       result.add(new Numeric(scalar * vect.get(i).get()));
/*     */     }
/* 224 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector divide(Vector vect, Evaluatable scalar) {
/* 236 */     Vector result = new Vector();
/* 237 */     for (int i = 0; i < vect.size(); i++) {
/* 238 */       result.add(new Numeric(vect.get(i).get() / scalar.get()));
/*     */     }
/* 240 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matrix add(Matrix mat1, Matrix mat2) throws ComputationException {
/* 256 */     if (mat1.numRows() != mat2.numRows() || mat1.numColumns() != mat2.numColumns()) {
/* 257 */       throw new ComputationException("Illegal matrix addition: The two matrices must be of equal size.");
/*     */     }
/* 259 */     Matrix result = new Matrix(mat1.numRows(), mat1.numColumns());
/* 260 */     for (int i = 0; i < mat1.numRows(); i++) {
/* 261 */       for (int j = 0; j < mat1.numColumns(); j++) {
/* 262 */         result.set(i, j, new Numeric(mat1.get(i, j).get() + mat2.get(i, j).get()));
/*     */       }
/*     */     } 
/*     */     
/* 266 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector add(Vector vect1, Vector vect2) {
/* 281 */     if (vect1.size() != vect2.size()) {
/* 282 */       throw new ComputationException("Illegal vector addition: The two vectors must be of equal size.");
/*     */     }
/* 284 */     Vector result = new Vector();
/* 285 */     for (int i = 0; i < vect1.size(); i++) {
/* 286 */       result.add(new Numeric(vect1.get(i).get() + vect2.get(i).get()));
/*     */     }
/* 288 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector subtract(Vector vect1, Vector vect2) {
/* 303 */     if (vect1.size() != vect2.size()) {
/* 304 */       throw new ComputationException("Illegal vector subtraction: The two vectors must be of equal size.");
/*     */     }
/* 306 */     Vector result = new Vector();
/* 307 */     for (int i = 0; i < vect1.size(); i++) {
/* 308 */       result.add(new Numeric(vect1.get(i).get() - vect2.get(i).get()));
/*     */     }
/* 310 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector subtract(Vector vect1, Evaluatable scalar) {
/* 316 */     Vector result = new Vector();
/* 317 */     for (int i = 0; i < vect1.size(); i++) {
/* 318 */       result.add(new Numeric(vect1.get(i).get() - scalar.get()));
/*     */     }
/* 320 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matrix reducedRowEchelon(Matrix mat) {
/* 333 */     Matrix result = mat.copy();
/* 334 */     for (int k = 0; k < mat.numRows(); k++) {
/* 335 */       double maxPiv = 0.0D;
/* 336 */       int pivIndex = k;
/* 337 */       for (int i = k; i < mat.numRows(); i++) {
/* 338 */         if (Math.abs(result.get(i, k).get()) > maxPiv) {
/* 339 */           maxPiv = Math.abs(result.get(i, k).get());
/* 340 */           pivIndex = i;
/*     */         } 
/*     */       } 
/* 343 */       result.swap(k, pivIndex);
/*     */       
/* 345 */       result.setRow(k, divide(result.getRow(k), result.getRow(k).get(k)));
/* 346 */       for (int i = 0; i < mat.numRows(); i++) {
/* 347 */         if (i != k) {
/* 348 */           result.addRowTo(k, i, new Numeric(-result.get(i, k).get()));
/*     */         }
/*     */       } 
/*     */     } 
/* 352 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\JCI_AP\002_Tools\EPC Shade\EPC Shade_001.jar!\isaac\math\LinearMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.2
 */