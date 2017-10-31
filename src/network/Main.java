package network;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);																	//create scanner for input
		boolean valid = true;																					//flag variable for correct user input
		int input = 0, numInputs = 0, numDataPoints = 0;														//numInputs here is #of input nodes; input is user choice
		Network network = null;

		//opening menu - prompt user to create a new data set
		do {
			valid = true;												//assume input is valid
			try {
				System.out.println("Would you like to create a new data set?\n1. Yes\n2. No");
				System.out.print(">");
				input = in.nextInt();									//attempt to read integer input for choice
			}
			catch (Exception e) {										//if that fails
				System.out.println("That is not an integer. Please enter an integer.\n");
				valid = false;											//input is invalid, flag
				in.nextLine();											//clear input buffer
			}
		} while (valid == false);										//repeat if bad choice

		ArrayList<Sample> samples = new ArrayList<Sample>();			//create list of samples to use - data set essentially

		//entering the dimensions - if the user chooses to create new data set
		if (input == 1) {
			do {
				valid = true;
				try {
					System.out.println("Please enter the number of dimensions for the Rosenbrock function:");
					numInputs = in.nextInt();
				}
				catch (Exception e) {
					System.out.println("That is not an integer. Please enter an integer.\n");
					valid = false;
					in.nextLine();
				}
			} while (valid == false);

			//enter data points (or inputs)
			do {
				valid = true;
				try {
					System.out.println("Please enter the number of data points you want:");
					numDataPoints = in.nextInt();
				}
				catch (Exception e) {
					System.out.println("That is not an integer. Please enter an integer.\n");
					valid = false;
					in.nextLine();
				}
			} while (valid == false);

			RosenbrockGenerator gen = new RosenbrockGenerator();
			samples = gen.generate(numInputs, numDataPoints);
		}

		//user chooses not to create data file - assumes one exists already
		else {
			numDataPoints = 0;
			try {
				Scanner s = new Scanner(new File("data.txt"));							//create a new scanner, checks lines of data in file
				while (s.hasNextLine()) {												//loop while there is another line
					String line = s.nextLine();											//grab the next line
					ArrayList<Double> inputs = new ArrayList<Double>();					//create an arraylist for the inputs
					int counter = 0;													//counter to determine size of input array
					Scanner lineScan = new Scanner(line);								//create new scanner that checks individual pieces of a line
					lineScan.useDelimiter(",");										//separates tokens with a comma, space (as they were inputted that way)
					while (lineScan.hasNext()) {										//loop while there are still tokens to be read
						counter++;														//update counter (1 more input)
						double element = Double.parseDouble(lineScan.next());
						inputs.add(element);				//parse the token to be a double and add to the input arraylist
					}														//update counter to reflect input size (total - 1, since last token is output
					counter--;
					double[] passIn = new double[counter];								//this is the array that will be passed to sample class
					for (int i = 0; i < counter; i++) {
						passIn[i] = inputs.get(i);										//initialize the input array
					}
					samples.add(new Sample(passIn, inputs.get(counter)));				//create new sample with input array, output, and add that sample to the list of samples
					numInputs = passIn.length;
					numDataPoints ++;
					lineScan.close();
				}
				s.close();
				System.out.println("Number of Inputs: " + numInputs);
				System.out.println("Number of Data Points: " + numDataPoints);
			}
			catch (Exception e) {
				System.out.println("File not found.");
			}
		}

		int numHidLayers = 2;
		int numHidNodes = 10;
		int numOutputs = 1;
		int hiddenActivation = 2; //sigmoidal
		int outputActivation = 1; //linear for function approximation

		in.close();		
			
		//create network and train with backprop
		network = new Network(numInputs, numHidLayers, numHidNodes, numOutputs, hiddenActivation, outputActivation);
		trainWithBackprop(network, samples);
		
		//create initial population of networks and train with genetic algorithm
		int popSize = 50;	//size of population
		ArrayList<Network> population = new ArrayList<Network>(); //initial population
		for(int i = 0; i < popSize; i++)
			population.add(new Network(numInputs, numHidLayers, numHidNodes, numOutputs, hiddenActivation, outputActivation));
		trainWithGA(population, samples);

	}
	
	//Train a network on the given list of samples with backpropagation
	public static void trainNetwork(Network network, List<Sample> samples) {
		for(int j = 0; j < 20; j++){
			double error = 0;
			for(int k = j*(samples.size()/20); k < (j+1)*(samples.size()/20); k++) {
				error += network.train(samples.get(k).getInputs(), samples.get(k).getOutput());
			}
			System.out.println("\tAverage Error: " + (error/(samples.size()/20)));
		}
	}
	
	//train a network with backpropagation and evaluate with 5x2 cross validation
	public static void trainWithBackprop(Network network, ArrayList<Sample> samples) {
		//5x2 cross validation
		double averageError = 0;	//error over all trials
		for(int i = 0; i < 10; i+=2){
			network.reset();
			Collections.shuffle(samples);

			//train network with first half of samples
			trainNetwork(network, samples.subList(0, samples.size()/2));
			
			//evaluate network on second half of samples
			double error = network.evaluate(samples.subList(samples.size()/2, samples.size()));
			System.out.println("Average Error of Trial " + (i+1) + ": " + (error) + "\n");
			averageError += error;
			
			network.reset();
			Collections.shuffle(samples);

			
			//train network with second half of samples
			trainNetwork(network, samples.subList(samples.size()/2, samples.size()));

			//evaluate network on first half of samples
			error = network.evaluate(samples.subList(0, samples.size()/2));
			System.out.println("Average Error of Trial " + (i+2) + ": " + (error) + "\n");
			averageError += error;
		}
		//average error of all trials
		averageError = averageError / 10;
		System.out.println("Average Error of 10 Trials: " + averageError);
	}
	
	//train a population of networks with a genetic algorithm and evaluate
	public static void trainWithGA(ArrayList<Network> population, ArrayList<Sample> samples) {
		int genNum = 1;
		for(int i = 0; i < 500; i++){	//a new generation is created every iteration
			double averageFitness = 0;
			double bestFitness = Double.MAX_VALUE;
			for(Network network : population) {
				double fitness = network.evaluate(samples.subList(0, samples.size()/2));
				network.setFitness(fitness);	//set the fitness of each to the average error of the test set
				averageFitness += fitness;
				if(fitness < bestFitness)
					bestFitness = fitness;
			}
			averageFitness /= population.size();
			System.out.println("Best Fitness of Generation    " + (genNum) + ": " + bestFitness);
			System.out.println("Average Fitness of Generation " + (genNum) + ": " + averageFitness + "\n");
			genNum++;
			/*for(Network network : population){
				System.out.println("\t"+ network.getFitness());
			}*/
			System.out.println();
			Collections.sort(population);	//sort networks by fitness
			/*for(Network network : population){
				System.out.println("\t"+ network.getFitness());
			}*/
			for(int j = 0; j < population.size(); j++)	//give worst network a fitness of 1, the next worse a fitness of 2, and so on
				population.get(j).setFitness(j+1);
			
			population = GeneticAlgorithm.createNextGeneration(population);
		}
	}
}