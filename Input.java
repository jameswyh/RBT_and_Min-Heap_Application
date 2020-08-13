public class Input {
	//This class describes the structure of Input command.
	int day;//the date when the input command is executed.
	String command;//the type of command, such as Insert and PrintBuilding.
	int num1;//the first number in the command
	int num2;//the second number in the command. 0 when there is no second number in the command.

	public Input(int day, String command, int num1, int num2) {
		this.day = day;
		this.command = command;
		this.num1 = num1;
		this.num2 = num2;
	}
}
