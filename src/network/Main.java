package network;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/*	Main class drives the creation and training of networks for Project 3. It reads in data files (which we manually have to specify), creates necessary network/population of networks,
 * 	and trains them using the prescribed algorithms. The user has the choice of training with backpropagation, the genetic algorithm, the mu plus lambda evolution strategy, or 
 * 	differential evolution. Each run prints the average error in order to give us a metric to compare the algorithms' performance. 
 */

public class Main {
	public static void main(String args[]) {
		ArrayList<Sample> samples = new ArrayList<Sample>();						//create list of samples to use - dataset essentially
		int numInputs = 0;
		int numDataPoints = 0;
		String filename = "tic-tac-toe.txt";
		try {
			Scanner s = new Scanner(new File(filename));							//create a new scanner, checks lines of data in file
			while (s.hasNextLine()) {												//loop while there is another line
				String line = s.nextLine();												//grab the next line
				ArrayList<Double> inputs = new ArrayList<Double>();						//create an arraylist for the inputs
				int counter = 0;														//counter to determine size of input array
				Scanner lineScan = new Scanner(line);									//create new scanner that checks individual pieces of a line
				lineScan.useDelimiter(",");												//separates tokens with a comma, space (as they were inputted that way)
				while (lineScan.hasNext()) {											//loop while there are still tokens to be read
					counter++;																//update counter (1 more input)
					double element = Double.parseDouble(lineScan.next());
					inputs.add(element);													//parse the token to be a double and add to the input arraylist
				}																		//update counter to reflect input size (total - 1, since last token is output
				counter--;
				double[] passIn = new double[counter];									//this is the array that will be passed to sample class
				for (int i = 0; i < counter; i++) {
					passIn[i] = inputs.get(i);											//initialize the input array
				}
				samples.add(new Sample(passIn, inputs.get(counter)));					//create new sample with input array, output, and add that sample to the list of samples
				numInputs = passIn.length;
				numDataPoints ++;
				lineScan.close();
			}
			s.close();																	//print information about the dataset
			System.out.println("Filename: " + filename);
			System.out.println("Number of Inputs: " + numInputs);
			System.out.println("Number of Data Points: " + numDataPoints);
			System.out.println();
		}
		catch (Exception e) {
			System.out.println("File not found.");
		}

		int numHidLayers = 2;															//specify network configurations - here is where we will tune the networks
		int numHidNodes = 20;
		int numOutputs = 1;
		int hiddenActivation = 2; 			//sigmoidal
		int outputActivation = 1; 			//linear for function approximation
		int popSize = 50;

		Scanner in = new Scanner(System.in);											//grab user input for the algorithm to use
		System.out.println("What algorithm do you want to use?");
		System.out.println("\t1)Backprop"); 
		System.out.println("\t2)Genetic Algorithm");
		System.out.println("\t3)Evolution Strategies");
		System.out.println("\t4)Differential Evolution");

		int selection = in.nextInt();
		in.close();

		Network bestNetwork = null;
		Collections.shuffle(samples);													//randomize the data

		switch(selection){
		case 1:																														//create network and train with backprop
			Network network = new Network(numInputs, numHidLayers, numHidNodes, numOutputs, hiddenActivation, outputActivation);
			bestNetwork = trainWithBackprop(network, samples.subList(0, samples.size()/2));											//train with first half of samples
			break;
		case 2:																														//create initial population of networks and train with genetic algorithm
			ArrayList<Network> population1 = new ArrayList<Network>(); 																//initial population
			for(int i = 0; i < popSize; i++) {
				population1.add(new Network(numInputs, numHidLayers, numHidNodes, numOutputs, hiddenActivation, outputActivation));
			}
			bestNetwork = trainWithGA(population1, samples.subList(0, samples.size()/2)); 											//train with first half of samples
			break;
		case 3:																														//create initial population of networks and train with evolution strategies
			ArrayList<Network> population2 = new ArrayList<Network>(); 																//initial population
			for(int i = 0; i < popSize; i++) {
				population2.add(new Network(numInputs, numHidLayers, numHidNodes, numOutputs, hiddenActivation, outputActivation));
			}
			bestNetwork = trainWithES(population2, samples.subList(0, samples.size()/2), popSize); 									//train with first half of samples
			break;
		case 4:																														//create initial population of networks and train with differential evolution
			ArrayList<Network> population3 = new ArrayList<Network>(); 																//initial population
			for(int i = 0; i < popSize; i++) {
				population3.add(new Network(numInputs, numHidLayers, numHidNodes, numOutputs, hiddenActivation, outputActivation));
			}
			bestNetwork = trainWithDE(population3, samples.subList(0, samples.size()/2)); 											//train with first half of samples
			break;
		}
		System.out.println("Average Error of Final Network: " + bestNetwork.evaluate(samples.subList(samples.size()/2, samples.size())));	//print network performance summary
	}

	//train a network with backpropagation and evaluate with 5x2 cross validation
	//@param network - the network to train
	//@param samples - the list of samples to train the network with
	public static Network trainWithBackprop(Network network, List<Sample> samples) {
		for(int j = 0; j < 20; j++){																	//this loop indicates the cross validation
			double error = 0;
			for(int k = j*(samples.size()/20); k < (j+1)*(samples.size()/20); k++) {
				error += network.train(samples.get(k).getInputs(), samples.get(k).getOutput());			//training happens here and returns an error value	
			}
			//System.out.println("\tAverage Error: " + (error/(samples.size()/20)));
			System.out.println((error/(samples.size()/20)));
		}
		return network;
	}

	//helper function for evolutionary algorithms to calculate the fitness of individuals in the population
	//@param population - list of networks that serve as the population
	//@param genNum - indicates the number of the current generation from the start
	public static void evaluatePopulation(ArrayList<Network> population, List<Sample> samples, int genNum){
		double averageFitness = 0;
		double bestFitness = Double.MAX_VALUE;
		for(Network network : population) {																	//iterate through population of networks
			double fitness = network.evaluate(samples);
			network.setFitness(fitness);																	//set the fitness of each to the average error of the test set
			averageFitness += fitness;																		//add together each fitness of population
			if(fitness < bestFitness)																		//find best fit individual - lowest average error
				bestFitness = fitness;
		}
		averageFitness /= population.size();																//calculate average fitness of population
		//System.out.println("Best Fitness of Generation    " + (genNum) + ": " + bestFitness);
		//System.out.println("Average Fitness of Generation " + (genNum) + ": " + averageFitness + "\n");		//print results of fitness determination
		System.out.println(bestFitness);
	}

	//train a population of networks with a genetic algorithm and evaluate
	//@param population - list of networks that serve as the population for the algorithm
	//@param samples - list of samples that function as the dataset
	public static Network trainWithGA(ArrayList<Network> population, List<Sample> samples) {
		int genNum = 1;																						//initialize the generation to 1
		int numGenerations = 500;																			//iterate through 50 generations
		while(true){																						//a new generation is created every iteration
			evaluatePopulation(population, samples, genNum);												//assess the fitness of the current generation
			Collections.sort(population);																	//sort networks by fitness from highest average error to lowest -- worst to best
			if(genNum == numGenerations)																	//end case,
				return population.get(population.size()-1);													//return best individual
			for(int j = 0; j < population.size(); j++)														//give worst network a fitness of 1, the next a fitness of 2, and so on
				population.get(j).setFitness(j+1);															//the best individual ends up with the highest fitness
			population = GeneticAlgorithm.createNextGeneration(population);									//create a new population of offspring
			genNum++;																						//iterate the generation counter
		}
	}

	//train a population of networks with the mu plus lambda evolution strategy
	//@param population - a list of networks that serves as the population
	//@param samples - a list of samples that serves as the dataset
	//@param popSize - indicates the size of the population
	public static Network trainWithES(ArrayList<Network> population, List<Sample> samples, int popSize){
		int genNum = 1;																						//initialize generation number and set end number again
		int numGenerations = 500;
		while(true){																						//loop forever (or until return condition in this case)
			evaluatePopulation(population, samples, genNum);												//calculate the fitness of the population
			Collections.sort(population);																	//sort population by fitness
			Collections.reverse(population);																//reverse order to go from best to worst
			if(genNum == numGenerations)																	//end case - if at 50 generations,
				return population.get(0);																	//return best individual
			ArrayList<Network> bestPop = new ArrayList<Network>();											//list representing the best individuals
			for(int j = 0; j < popSize; j++){
				bestPop.add(population.get(j));																//select the best individuals
			}
			bestPop.addAll(EvolutionStrategies.createNextGeneration(bestPop));								//add offspring to population
			population = bestPop;																			//set population to best individuals + offspring of best individuals			
			genNum++;																						//iterate generation counter
		}
	}

	//train a population of networks with differential evolution
	//@param population - a list of networks that serves as the population
	//@param samples - list of samples that serves as the dataset
	public static Network trainWithDE(ArrayList<Network> population, List<Sample> samples) {
		int genNum = 1;																						//initialize generation number, iterate to 50 as before
		int numGenerations = 500;
		while(true) {																						//continue to loop until return condition
			evaluatePopulation(population, samples, genNum);												//calculate fitness of population
			if(genNum > numGenerations) {																	//iterate until one more than the stopping condition
				Collections.sort(population);																//sort population by fitness
				return population.get(population.size()-1);													//return best individual
			}
			population = DifferentialEvolution.createNextGeneration(population, samples);					//create a new generation using DE
			genNum++;																						//iteration generation counter
		}
	}
}