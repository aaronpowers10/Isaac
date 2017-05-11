package isaac.math;

public class RandomGenerator {
	
	public static int randomInt(int minimum, int maximum){
		return minimum + (int)(Math.random() * ((maximum - minimum) + 1));
	}

}
