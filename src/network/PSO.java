package network;

import java.util.ArrayList;

public class PSO {
	private int particleNum = 20;					//use 20 particles
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	
	public ArrayList<Cluster> cluster(ArrayList<DataPoint> data) {
			for (int i = 0; i < particleNum; i++) {								//for each particle
				particles.add(new Particle());									//initialize a new particle
			}
			//for each particle
				//calc fitness
				//if fitness is better than best fitness value pBest in history
					//set current val as new pBest
			//choose particle with best fitness as gBest
			//for each particle
				//calc particle velocity according to equation : velocity = velocity + learningFactor1 * rand() * (pBest - currParticle) + learningFactor2 * rand() * (gBest - present)
					//learning factors usually = 2, currParticle refers to position; pos and velocity are vectors
				//update particle position according to equation : present = present + velocity
			//continue till stopping condition
		return null;
	}
}
