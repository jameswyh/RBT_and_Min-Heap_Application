public class Triplet {
	//This class describes the triplet structure of the record of each building.
	
	int buildingNum;//the unique ID of each building.
	int executed_time;//the days have been worked on of each building.
	int total_time;//the total days that need to work on of each building.

	public Triplet(int buildingNum, int executed_time, int total_time) {
		this.buildingNum = buildingNum;
		this.executed_time = executed_time;
		this.total_time = total_time;
	}
}
