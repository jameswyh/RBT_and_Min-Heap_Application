import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class risingCity {

	public static void main(String[] args) throws Exception {

		RedBlackTree tree = new RedBlackTree();
		MinHeap heap = new MinHeap();
		String string = new String();

		int gCounter = 0;//the global counter

		File file = new File(args[0]);//get input file from command line
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		List<Input> InputList=new ArrayList<Input>();//list to store command
		while ((st = br.readLine()) != null) {
			String a[] = st.split(":");//split the command
			int gt = Integer.parseInt(a[0]);
			String b[] = a[1].split("\\(");
			String command = b[0].replaceAll("\\s","");
			int num1 = 0;
			int num2 = 0;
			if(b[1].contains(",")) {
				String c[] = b[1].split(",");
				num1 = Integer.parseInt(c[0]);
				num2 = Integer.parseInt(c[1].replaceAll("\\)",""));
			}
			else {
				num1 = Integer.parseInt(b[1].replaceAll("\\)",""));
			}
			Input input = new Input(gt, command, num1, num2);
			InputList.add(input);//add the command to the input list
		}
		int commandcount = 0;//count the number of command in the input list
		Triplet min = heap.removeMin();//get min from minheap
		int daycount = 0;//counter to count 5 days

		do{
			if(min != null) {
				min.executed_time ++;
				daycount ++;
				gCounter ++;
				if(commandcount < InputList.size() && gCounter == InputList.get(commandcount).day) {
					//when global counter equals the date in command, execute the command
					if(InputList.get(commandcount).command.equals("Insert")) {
						Node node = new Node(new Triplet(InputList.get(commandcount).num1, 0, InputList.get(commandcount).num2));
						heap.add(node.triplet);
						tree.addNode(node);
						commandcount ++;
					}
					else {
						if(InputList.get(commandcount).num2 == 0) {
							Node node = tree.lookUpNode(new Node(new Triplet(InputList.get(commandcount).num1,0,0)));
							if(node != null) {
								string += "("+node.triplet.buildingNum+","+node.triplet.executed_time+","+node.triplet.total_time+")\n";
							}
							else {
								string += "(0,0,0)\n";
							}
						}
						else {
							String str = tree.midOrder(tree.root, InputList.get(commandcount).num1, InputList.get(commandcount).num2);
							if(str == null) {
								string += "(0,0,0)\n";
							}
							else {
								string = string + str.substring(5) + "\n";
							}
						}
						commandcount ++;
					}
				}

				if(min.executed_time == min.total_time) {//if a building complete
					tree.deleteNode(new Node(min), false, false);//delete it from the RBT
					string += "("+min.buildingNum +","+ gCounter +")\n";
					min = heap.removeMin();//get a new min from the minheap
					daycount = 0;//reset 5day counter to 0
				}
				else if(daycount == 5) {//if 5 days and not complete
					heap.add(min);//add the building back to the minheap
					daycount = 0;//reset 5day counter to 0
					min = heap.removeMin();//get a new min from the minheap
				}
			}
			else {
				gCounter = InputList.get(commandcount).day;
				if(InputList.get(commandcount).command.equals("Insert")) {
					Node node = new Node(new Triplet(InputList.get(commandcount).num1, 0, InputList.get(commandcount).num2));
					heap.add(node.triplet);
					tree.addNode(node);
					commandcount ++;
					min = heap.removeMin();
				}
				else {
					string += "(0,0,0)\n";
					commandcount ++;
				}
			}
		}while(gCounter <= InputList.get(InputList.size()-1).day || min != null);
		writeFile(string);
	}
	public static void writeFile(String str) {
		//print all the output into the output file.
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter("output_file.txt");
			fileWriter.write(str);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
