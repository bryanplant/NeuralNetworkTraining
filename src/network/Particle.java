package network;

/*	Particle class represents a single particle in the particle swarm. It has a velocity value, a cluster radius (representing the position),
 * 	and a personal best value representing how fit that cluster solution is.  
 */
public class Particle {
	private double velocity;
	private double clusterRad;
	private double fitness;
	
	//creates a new particle
	//@param lowVal - the low value in the dataset
	//@param highVal - the high value in the dataset
	public Particle(double lowVal, double highVal) {
		clusterRad = lowVal + (Math.random() * ((highVal - lowVal) + 1));				//initialize the cluster radius to be a random number in the data range
		velocity = 0.001 + (Math.random() * ((10 - 0.001) + 1));						//initialize velocity to be between 0.001 and 10 to encourage varying rates of convergence
		fitness = calcFitness(clusterRad, velocity);
	}
	
	public double calcFitness(double clusterRad, double velocity) {
		double fitVal;
		//ave. distance between data points in a cluster
		return fitVal;
	}
	
}
