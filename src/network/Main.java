package network;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
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
		
		ArrayList<Network> population = new ArrayList<Network>(); //initial population
		GeneticAlgorithm.createNextGeneration(population);		  //sample call for creating a new generation

		int numHidLayers = 2;
		int numHidNodes = 3;
		int numOutputs = 1;
		int hiddenActivation = 2; //sigmoidal
		int outputActivation = 1; //linear
		network = new Network(numInputs, numHidLayers, numHidNodes, numOutputs, hiddenActivation, outputActivation);

		in.close();

		//5x2 cross validation
		double averageError = 0;	//error over all folds
		for(int i = 0; i < 5; i++){
			//System.out.println("TEST " + (i+1) + "\n");
			network.reset();
			Collections.shuffle(samples);

			//train network
			for(int j = 0; j < 20; j++){
				double error = 0;
				for(int k = j*(samples.size()/40); k < (j+1)*(samples.size()/40); k++) {
					error += network.train(samples.get(k).getInputs(), samples.get(k).getOutput());
				}
				System.out.println("\tAverage Error: " + (error/(samples.size()/40)));
			}

			//evaluate network
			double error = 0;	//error for this fold
			for(int j = samples.size()/2; j < samples.size(); j++){
				error += network.evaluate(samples.get(j).getInputs(), samples.get(j).getOutput());
			}
			//average error of the test
			error = error/(samples.size()/2);
			System.out.println("Average Error of Test " + (i+1) + ": " + (error) + "\n");
			averageError += error;
		}
		//average error of all tests
		averageError = averageError / 5;
		System.out.println("Average Error of 5 Tests: " + averageError);
	}
}