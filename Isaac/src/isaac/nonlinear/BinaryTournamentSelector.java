package isaac.nonlinear;

public class BinaryTournamentSelector implements Selector{

	@Override
	public Individual select(Population population) {
		Individual contestant1 = population.get((int)(Math.random()*(population.size()-1)));
		Individual contestant2 = population.get((int)(Math.random()*(population.size()-1)));
		if(contestant1.fitness() < contestant2.fitness()){
			if(Math.random() < 0.9)
				return contestant1;
			else
				return contestant2;
		} else {
			if(Math.random() < 0.9)
				return contestant2;
			else
				return contestant1;
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

}
