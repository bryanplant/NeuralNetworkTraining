package network;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class RosenbrockGenerator {
	public ArrayList<Sample> generate(int numInputs, int numDataPoints){
		Random random = new Random();
		ArrayList<Sample> samples = new ArrayList<Sample>();

		File outfile = null;						//create output file
		PrintWriter writer = null;
		try {
			outfile = new File("data.txt");
			writer = new PrintWriter(outfile, "UTF-8");

		} catch (Exception e) {
			System.out.println("Something went wrong generating the output file.");
		}

		double maxOutput = Double.MIN_VALUE;
		double minOutput = Double.MAX_VALUE;
		for(int i = 0; i < numDataPoints; i++){
			double inputs[] = new double[numInputs];
			for(int j = 0; j < numInputs; j++){
				inputs[j] = (random.nextDouble()*6)-3;
				writer.print(inputs[j] + ",");
			}
			double output = calculate(inputs);
			if(output > maxOutput)
				maxOutput = output;
			if(output < minOutput)
				minOutput = output;
			writer.println(output);
			samples.add(new Sample(inputs, output));
		}

		System.out.println("Range of Data: " + (maxOutput-minOutput));
		writer.close();

		return samples;
	}

	private double calculate(double[] inputs){
		/*double output = 0;
		for(int i = 0; i < inputs.length-1; i++){
			output += (Math.pow((1 - inputs[i]), 2) + (100 * Math.pow(inputs[i+1] - Math.pow(inputs[i], 2), 2)));	//Rosenbrock function
		}
		return output;*/
		return Math.pow(inputs[0],3);
	}
}
