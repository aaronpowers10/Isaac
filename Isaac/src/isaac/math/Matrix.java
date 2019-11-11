/*     */ package isaac.math;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Scanner;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Matrix
/*     */ {
/*     */   private ArrayList<Vector> vals;
/*     */   
/*  53 */   public Matrix(int numRows, int numColumns) { setSize(numRows, numColumns); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(int numRows, int numColumns) {
/*  63 */     this.vals = new ArrayList();
/*  64 */     for (int i = 0; i < numRows; i++) {
/*  65 */       Vector vect = new Vector(numColumns);
/*  66 */       this.vals.add(vect);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public int numRows() { return this.vals.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public int numColumns() { return ((Vector)this.vals.get(0)).size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public void set(int rowIndex, int colIndex, Evaluatable value) { ((Vector)this.vals.get(rowIndex)).set(colIndex, value); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public void set(int rowIndex, int colIndex, double value) { ((Vector)this.vals.get(rowIndex)).set(colIndex, new Numeric(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public Evaluatable get(int rowIndex, int colIndex) { return ((Vector)this.vals.get(rowIndex)).get(colIndex); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getColumn(int index) {
/* 119 */     Vector result = new Vector();
/* 120 */     for (int i = 0; i < numRows(); i++) {
/* 121 */       result.add(get(i, index));
/*     */     }
/* 123 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public Vector getRow(int index) { return ((Vector)this.vals.get(index)).copy(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColumn(int index, Vector vector) {
/* 143 */     for (int i = 0; i < numRows(); i++) {
/* 144 */       set(i, index, vector.get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   public void setRow(int index, Vector vector) { this.vals.set(index, vector); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void swap(int i, int j) {
/* 165 */     Vector tempRow = getRow(i);
/* 166 */     setRow(i, getRow(j));
/* 167 */     setRow(j, tempRow);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   public void multiplyRow(int i, Evaluatable scalar) { setRow(i, LinearMath.multiply(getRow(i), scalar)); }
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
/* 189 */   public void addRowTo(int i, int j, Evaluatable scalar) { setRow(j, LinearMath.add(LinearMath.multiply(getRow(i), scalar), getRow(j))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix transpose() {
/* 198 */     Matrix transMatrix = new Matrix(numColumns(), numRows());
/* 199 */     for (int i = 0; i < numColumns(); i++) {
/* 200 */       for (int j = 0; j < numRows(); j++) {
/* 201 */         transMatrix.set(i, j, get(j, i));
/*     */       }
/*     */     } 
/* 204 */     return transMatrix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix inverse() {
/* 213 */     Matrix reduced = LinearMath.reducedRowEchelon(LinearMath.augment(this, LinearMath.identity(numRows())));
/* 214 */     Matrix id = LinearMath.subMatrix(0, numColumns(), reduced);
/* 215 */     Matrix inv = LinearMath.subMatrix(numColumns(), reduced.numColumns(), reduced);
/*     */     
/* 217 */     if (id.numericallyEquals(LinearMath.identity(numRows()))) {
/* 218 */       return inv;
/*     */     }
/* 220 */     System.out.println(write());
/* 221 */     throw new ComputationException("Matrix inverse error: Singular Matrix");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix copy() {
/* 229 */     Matrix cloneMat = new Matrix(numRows(), numColumns());
/* 230 */     for (int i = 0; i < numRows(); i++) {
/* 231 */       cloneMat.setRow(i, getRow(i));
/*     */     }
/* 233 */     return cloneMat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String write() {
/* 240 */     String result = "";
/* 241 */     for (int i = 0; i < numRows(); i++) {
/* 242 */       result = String.valueOf(result) + getRow(i).write() + "\n";
/*     */     }
/* 244 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector toVector() {
/* 249 */     if (numRows() == 1)
/* 250 */       return getRow(0); 
/* 251 */     if (numColumns() == 1) {
/* 252 */       return getColumn(0);
/*     */     }
/* 254 */     return null;
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
/*     */   public boolean numericallyEquals(Matrix mat) {
/* 266 */     if (mat.numRows() != numRows()) {
/* 267 */       return false;
/*     */     }
/* 269 */     for (int i = 0; i < numRows(); i++) {
/* 270 */       if (!mat.getRow(i).numericallyEquals(getRow(i))) {
/* 271 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 275 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public double determinant() {
/* 280 */     if (numRows() != numColumns()) {
/* 281 */       throw new ComputationException("Cannot compute determinant of non-square matrix.");
/*     */     }
/*     */     
/* 284 */     double result = 0.0D;
/* 285 */     if (numRows() == 1) {
/* 286 */       result = get(0, 0).get();
/*     */     } else {
/* 288 */       for (int i = 0; i < numRows(); i++) {
/* 289 */         Matrix mat = new Matrix(numRows() - 1, numColumns() - 1);
/* 290 */         for (int rowIndex = 1; rowIndex < numRows(); rowIndex++) {
/* 291 */           int subColIndex = 0;
/* 292 */           for (int colIndex = 0; colIndex < numColumns(); colIndex++) {
/* 293 */             if (colIndex != i) {
/* 294 */               mat.set(rowIndex - 1, subColIndex, get(rowIndex, colIndex).copy());
/* 295 */               subColIndex++;
/*     */             } 
/*     */           } 
/*     */         } 
/* 299 */         result += Math.pow(-1.0D, i) * get(0, i).get() * mat.determinant();
/*     */       } 
/*     */     } 
/* 302 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEqual(Matrix matrix) {
/* 312 */     for (int i = 0; i < matrix.numRows(); i++) {
/* 313 */       ((Vector)this.vals.get(i)).setEqual(matrix.getRow(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 323 */     Matrix matrix = new Matrix(4, 4);
/*     */     try {
/* 325 */       Scanner in = new Scanner(new File("C:\\aaron\\Matrix1.txt"));
/* 326 */       int rowIndex = 0;
/* 327 */       while (in.hasNextLine()) {
/* 328 */         String line = in.nextLine();
/* 329 */         Scanner in2 = new Scanner(line);
/* 330 */         int columnIndex = 0;
/* 331 */         while (in2.hasNextDouble()) {
/* 332 */           matrix.set(rowIndex, columnIndex, new Numeric(in2.nextDouble()));
/* 333 */           columnIndex++;
/*     */         } 
/* 335 */         rowIndex++;
/* 336 */         in2.close();
/*     */       } 
/* 338 */       in.close();
/* 339 */     } catch (FileNotFoundException e) {
/*     */       
/* 341 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 344 */     System.out.println(matrix.determinant());
/*     */   }
/*     */ }


/* Location:              C:\JCI_AP\002_Tools\EPC Shade\EPC Shade_001.jar!\isaac\math\Matrix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.2
 */