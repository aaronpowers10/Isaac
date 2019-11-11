/*     */ package isaac.nonlinear;
/*     */ 
/*     */ import isaac.math.BoundedNumeric;
/*     */ import isaac.math.ComputationException;
/*     */ import isaac.math.Evaluatable;
/*     */ import isaac.math.LinearMath;
/*     */ import isaac.math.Matrix;
/*     */ import isaac.math.Numeric;
/*     */ import isaac.math.Setable;
/*     */ import isaac.math.Vector;
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
/*     */ public class NonlinearSystem
/*     */   implements Runnable
/*     */ {
/*     */   private Vector x;
/*     */   private Vector dx;
/*     */   private Vector residuals;
/*     */   private int iteration;
/*     */   private boolean isCanceled;
/*     */   private Matrix jacobian;
/*     */   private Matrix stepMatrix;
/*     */   private Matrix identity;
/*     */   private Numeric lamda;
/*     */   private double initialLamda;
/*     */   private double geometricFactor;
/*     */   private int maxIterations;
/*     */   
/*     */   public NonlinearSystem() {
/*  49 */     this.x = new Vector();
/*  50 */     this.dx = new Vector();
/*  51 */     this.residuals = new Vector();
/*  52 */     this.lamda = new Numeric(0.0D);
/*     */   }
/*     */ 
/*     */   
/*  56 */   public double lamda() { return this.lamda.get(); }
/*     */ 
/*     */ 
/*     */   
/*  60 */   public double geometricFactor() { return this.geometricFactor; }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public Vector variables() { return this.x; }
/*     */ 
/*     */ 
/*     */   
/*  68 */   public Vector residuals() { return this.residuals; }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public void setInitialLamda(double initialLamda) { this.initialLamda = initialLamda; }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public void setGeometricFactor(double geoFactor) { this.geometricFactor = geoFactor; }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public void setMaxIterations(int maxIterations) { this.maxIterations = maxIterations; }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public void addResidual(Evaluatable newResidual) { this.residuals.add(newResidual); }
/*     */ 
/*     */   
/*     */   public void addVariable(BoundedNumeric variable) {
/*  88 */     this.x.add(variable);
/*  89 */     this.dx.add(new Numeric(0.0D));
/*     */   }
/*     */   
/*     */   public boolean containsVariable(BoundedNumeric variable) {
/*  93 */     for (int i = 0; i < this.x.size(); i++) {
/*  94 */       if (this.x.get(i).equals(variable)) {
/*  95 */         return true;
/*     */       }
/*     */     } 
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/* 106 */       solve();
/* 107 */     } catch (ComputationException e) {
/* 108 */       System.out.println(e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public void solve() { solveIterate(); }
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
/*     */   private void solveIterate() {
/* 136 */     this.jacobian = new Matrix(numEqns(), numEqns());
/* 137 */     this.identity = LinearMath.identity(numEqns());
/* 138 */     this.lamda.set(this.initialLamda);
/* 139 */     this.isCanceled = false;
/* 140 */     this.iteration = 0;
/*     */     
/* 142 */     while (!isStopCriteria() && !this.isCanceled) {
/*     */ 
/*     */       
/*     */       try {
/* 146 */         setJacobian();
/* 147 */         setLamda();
/* 148 */         setStepMatrix();
/* 149 */         this.dx = LinearMath.multiply(this.stepMatrix, this.residuals);
/* 150 */         this.x.setEqual(LinearMath.subtract(this.x, this.dx));
/* 151 */         this.iteration++;
/* 152 */       } catch (ComputationException e) {
/* 153 */         throw new ComputationException(
/* 154 */             String.valueOf(e.getMessage()) + " On Iteration " + this.iteration + " Lamda = " + this.lamda.get());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     if (this.iteration > this.maxIterations - 1) {
/* 164 */       throw new ComputationException("System not solved in required iterations.");
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
/*     */   
/*     */   protected void setJacobian() {
/* 177 */     double stepSize = 1.0E-6D;
/* 178 */     for (int i = 0; i < numEqns(); i++) {
/* 179 */       for (int j = 0; j < numVars(); j++) {
/* 180 */         double y = this.residuals.get(i).get();
/* 181 */         ((Setable)this.x.get(j)).set(this.x.get(j).get() + stepSize);
/* 182 */         double yInc = this.residuals.get(i).get();
/* 183 */         ((Setable)this.x.get(j)).set(this.x.get(j).get() - stepSize);
/* 184 */         ((Setable)this.jacobian.get(i, j)).set((yInc - y) / stepSize);
/*     */       } 
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
/*     */   private void setStepMatrix() {
/* 209 */     this.stepMatrix = LinearMath.multiply(LinearMath.add(LinearMath.multiply(this.jacobian.transpose(), this.jacobian), LinearMath.multiply(this.identity, this.lamda)).inverse(), 
/* 210 */         this.jacobian.transpose());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   private void setLamda() { this.lamda.set(this.lamda.get() * this.geometricFactor); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isStopCriteria() {
/* 227 */     boolean result = true;
/* 228 */     for (int i = 0; i < numEqns(); i++) {
/* 229 */       if (Math.abs(this.residuals.get(i).get()) > 0.001D) {
/* 230 */         result = false;
/* 231 */       } else if (Double.isNaN(this.residuals.get(i).get())) {
/* 232 */         result = false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 238 */     if (this.iteration > this.maxIterations) {
/* 239 */       result = true;
/*     */     }
/* 241 */     return result;
/*     */   }
/*     */   
/*     */   private boolean isMaxStep() {
/* 245 */     boolean result = true;
/*     */     
/* 247 */     for (int i = 0; i < numEqns(); i++) {
/* 248 */       if (Math.abs(this.dx.get(i).get()) > 0.001D) {
/* 249 */         result = false;
/* 250 */       } else if (Double.isNaN(this.dx.get(i).get())) {
/* 251 */         result = false;
/*     */       } 
/*     */     } 
/* 254 */     if (this.iteration == 0) {
/* 255 */       return false;
/*     */     }
/* 257 */     return result;
/*     */   }
/*     */   
/*     */   public double maxResidual() {
/* 261 */     double max = 0.0D;
/* 262 */     for (int i = 0; i < this.residuals.size(); i++) {
/* 263 */       double res = this.residuals.get(i).get();
/* 264 */       if (res > max) {
/* 265 */         max = res;
/*     */       }
/*     */     } 
/* 268 */     return max;
/*     */   }
/*     */   
/*     */   private double residualSquare() {
/* 272 */     double result = 0.0D;
/* 273 */     for (int i = 0; i < numEqns(); i++) {
/* 274 */       result += Math.pow(this.residuals.get(i).get(), 2.0D);
/*     */     }
/* 276 */     return result;
/*     */   }
/*     */   
/*     */   private double avgRes() {
/* 280 */     double result = 0.0D;
/* 281 */     for (int i = 0; i < numEqns(); i++) {
/* 282 */       result += Math.abs(this.residuals.get(i).get());
/*     */     }
/* 284 */     return result / numVars();
/*     */   }
/*     */ 
/*     */   
/* 288 */   public int numVars() { return this.x.size(); }
/*     */ 
/*     */ 
/*     */   
/* 292 */   public int numEqns() { return this.residuals.size(); }
/*     */ }


/* Location:              C:\JCI_AP\002_Tools\EPC Shade\EPC Shade_001.jar!\isaac\nonlinear\NonlinearSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.2
 */