import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BackwardLearning {

	int numberOfPorts;

	Map<Integer, Integer> table;

	public static void main(String[] args) {

		BackwardLearning object = new BackwardLearning(5);

		// Format of frame: aaaabbbbxxxxxxxxxxxxxx where aaaa is the hex of destination, bbbb the hex of source, xxx... the message
		String frame1 = "00000001000b04171c9c53";
		String frame2 = "00030001000b5cc314c87b";
		String frame3 = "00000001000bcacfa68645";
		String frame4 = "00010002000bcac68645fa";
		String frame5 = "00020004000bcac68645fa";

		System.out.println(object.send(3, frame1));
		System.out.println(object.send(3, frame2));
		System.out.println(object.send(3, frame3));
		System.out.println(object.send(2, frame4));
		System.out.println(object.send(1, frame5));

	}

	public BackwardLearning(int numberOfPorts) {

		this.numberOfPorts = numberOfPorts;

		table = new HashMap<Integer,Integer>();

	}

	public List<Integer> send(int incomingPort, String frame) {

		int destination = Integer.parseInt(frame.substring(0,4),16);
		int source = Integer.parseInt(frame.substring(4,8),16);
		System.out.println();
		System.out.println("Source: " + source + ", Destination: " + destination);
		System.out.println("--> Known machines on port: " + table);

		if(destination == source) {
			table.put(source,incomingPort);
			System.out.println("--> Sending to: []");
			return new ArrayList<Integer>();
		}

		if(!table.containsKey(new Integer(source))) {
			System.out.println("--> Source " + source + " added to table on port " + incomingPort);
			table.put(source,incomingPort);
		}else{
			if(table.get(source) != incomingPort) {
				table.put(new Integer(source), incomingPort);
			}
		}

		List<Integer> list = new ArrayList<Integer>();

		if(table.containsKey(destination)) {
			System.out.println("--> Destination " + destination + " found on port " + table.get(new Integer(destination)));

			if(table.get(new Integer(destination)) != incomingPort) {
				list.add(table.get(new Integer(destination))); 
			}else{
				System.out.println("--> Destination is on same port as source");
				System.out.println("--> Sending to: " + list);

				return list;
			}

			System.out.println("--> Sending to: " + list);
			System.out.println("--> Known machines on port: " + table);

			return list;

		}else{

			System.out.println("--> Destination " + destination + " not found");  

			for(int i = 0; i < this.numberOfPorts; i++) {
				if(i != incomingPort) {
					list.add(i);
				}
			}

			System.out.println("--> Sending to: " + list);
			System.out.println("--> Known machines on port: " + table);

			return list;

		}

	}
}
