package network;

import java.util.ArrayList;

public class PSO {
	private int particleNum = 20;					//use 20 particles
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	private Particle gBest;

	public ArrayList<Cluster> cluster(ArrayList<DataPoint> data) {
		for (int i = 0; i < particleNum; i++) {								//for each particle
			particles.add(new Particle());									//initialize a new particle
		}
		for (int t = 0; t < 500; t++) {										//run PSO for 500 iterations
			for (int i = 0; i < particleNum; i++) {								//for each particle
				double fitness = particles.get(i).calcFitness();				//calculate the fitness and update pBest if applicable
				if (fitness > gBest.getFitness()) {
					gBest = particles.get(i);									//choose particle with best fitness as gBest
				}
			}
			for (int i = 0; i < particleNum; i++) {								//for each particle
				particles.get(i).setGBest(gBest.getFitness());					//update the gBest of every particle
				particles.get(i).update();
			}
		}
		return null;
	}
}
