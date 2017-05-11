package isaac.nonlinear;

import isaac.math.BoundedNumeric;
import isaac.math.Evaluatable;
import isaac.math.Numeric;
import isaac.math.Vector;

public class GeneticOptimizer implements Optimizer{


	
	private Population population;
	private StoppingCriteria stoppingCriteria;

	public GeneticOptimizer(Population initialPopulation, StoppingCriteria stoppingCriteria, String fileName){
		this.stoppingCriteria = stoppingCriteria;
		this.population = initialPopulation;
	}

	public Individual getAlpha(){
		return population.getAlpha();
	}

	@Override
	public void run(Vector<BoundedNumeric> x, Vector<Evaluatable> residuals, Vector<BoundedNumeric> previousVariables) {
		population = new Population();
		for(int i=0;i<x.size();i++){
			population.addIndividual(new MinResidualIndividual(x,residuals));
		}
		while(!stoppingCriteria.isStoppingCriteria()){
			population.advance();
		}
		
	}


}
