package network;

import java.util.ArrayList;
import java.util.Random;

public class Cluster {
	private DataPoint center;
	private ArrayList<DataPoint> members;
	
	Random rand = new Random();
	
	//Allow cluster to contain trivial number of Points (ArrayList)
	//Center variable that will be randomly generated and updated with the algorithm
	//Center will have same number of attributes as the DataPoint members
	
	public Cluster(int numFeatures, ArrayList<DataPoint> data) {
		center = new DataPoint(data.get(rand.nextInt(data.size())).getFeatures());	//set center to random point in data
	}
	
	public void addPoint(DataPoint point) {
		members.add(point);
	}
	
	public void removePoint(DataPoint point) {
		members.remove(point);
	}
	
	public void updateCenter() {
		//Logic to update center based on the current members
	}
	
	public DataPoint getCenter() {
		return center;
	}
	
	public ArrayList<DataPoint> getMembers() {
		return members;
	}
}
