package isaac.nonlinear;

public class MaxGenerationsStoppingCriteria implements StoppingCriteria{

	private int maxGenerations;
	private int currentGeneration;

	public MaxGenerationsStoppingCriteria(int maxGenerations){
		this.maxGenerations = maxGenerations;
		currentGeneration = 0;
	}

	@Override
	public boolean isStoppingCriteria() {
		currentGeneration++;
		if(currentGeneration < maxGenerations){
			return false;
		} else {
			return true;
		}
	}

}
