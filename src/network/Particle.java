package network;

/*	Particle class represents a single particle in the particle swarm. It has a velocity value, a cluster radius (representing the position),
 * 	and a personal best value representing how fit that cluster solution is.  
 */
public class Particle {
	private double velocity;
	private double clusterRad;
	private Cluster thisCluster;
	private double fitness;
	private double pBest;
	private double gBest;
	
	//creates a new particle
	public Particle() {
		
		/*
		 * NEED TO TUNE CLUSTERRAD RANGE
		 */
		
		
		clusterRad = 0.001 + (Math.random() * ((10 - 0.001) + 1));					//initialize the cluster radius to be a random number between 0.001 and 10
		velocity = 0.001 + (Math.random() * ((10 - 0.001) + 1));					//initialize velocity to be between 0.001 and 10 to encourage varying rates of convergence
		cluster();
	}
	
	//creates the cluster associated with this particle
	public void cluster() {
		//thisCluster = new Cluster();
	}
	
	//calculates the fitness of this particle
	public double calcFitness(double clusterRad, double velocity) {
		double fitVal = 0;
		//ave. distance between data points in a cluster
		return fitVal;
	}
}