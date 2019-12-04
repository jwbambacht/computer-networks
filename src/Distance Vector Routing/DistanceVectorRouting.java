import java.util.*;

/**
 * Routing table for dynamic distance vector routing.
 *
 * It holds a list of routes that can be queried to find the router which can reach the destination.
 */
class DistanceVectorRouting {

	public String identifier;
	public Map<String,String[]> routingTable;

	public static final String DATA_PACKET_TYPE = "D";
	public static final String ERROR_INVALID_TYPE = "INVALID TYPE";
	public static final String ERROR_NO_ROUTE = "NO ROUTE";
	public static final String MESSAGE_THANK_YOU = "THANK YOU";
	public static final String MESSAGE_ROUTE_RECEIVED = "RECEIVED";
	public static final String ROUTING_PACKET_TYPE = "R";

	public static void main(String[] args) {
		DistanceVectorRouting dvrR = new DistanceVectorRouting("004b");

		System.out.println("Response of routing packet: " + dvrR.processPacket("R 1530 80 1 72ba 10"));

		DistanceVectorRouting dvrD = new DistanceVectorRouting("004b");
		System.out.println("Response of data packet: " + dvrD.processPacket("D b974 004b eeb7c117b3b8821"));

	}

	public DistanceVectorRouting(String routerIdentifier) {

		this.identifier = routerIdentifier;
		this.routingTable = new HashMap<String,String[]>();

	}

	/**
	 * This processes the packets for you and calls the routing or data packet processors accordingly.
	 *
	 * @param packet Network packet supplied by our test cases.
	 * @return String of the reply from the incoming packet.
	 */
	public String processPacket(String packet) {
		String[] packetParts = packet.split(" ");
		String packetType = packetParts[0];
		switch(packetType) {
		case ROUTING_PACKET_TYPE:
			int numberOfRoutes = Integer.parseInt(packetParts[3]);
			ArrayList<Route> routes = new ArrayList<>();
			int index = 4;
			for (int i = 0; i < numberOfRoutes; i++) {
				routes.add(new Route(packetParts[index], Integer.parseInt(packetParts[index + 1])));
				index += 2;
			}
			return processRoutingPacket(packetParts[1], Integer.parseInt(packetParts[2]), routes);
		case DATA_PACKET_TYPE:
			return processDataPacket(packetParts[1], packetParts[2], packetParts[3]);
		default:
			return ERROR_INVALID_TYPE;
		}
	}

	/**
	 * Process the data packet, this function is called automatically from processPacket.
	 *
	 * @param source Original source of message.
	 * @param destination Final destination of message.
	 * @param message Fake message.
	 * @return The neighbour router which can route to the desired destination and the total latency
	 *         to the final destination. If no valid route can be found the function should return ERROR_NO_ROUTE,
	 *         which is mapped to 'NO ROUTE' in the code. If a message is directed to the current router,
	 *         the router should return MESSAGE_THANK_YOU, which is mapped to 'THANK YOU' in the code.
	 */
	private String processDataPacket(String source, String destination, String message) {

		if(this.identifier.equals(destination)) {
			return MESSAGE_THANK_YOU;
		}

		if(this.routingTable.containsKey(destination)) {
			String outwardRouter = this.routingTable.get(destination)[0];
			String latency = this.routingTable.get(destination)[1];

			return outwardRouter + " " + latency;
		}

		return ERROR_NO_ROUTE;
	}

	/**
	 * Process the router packet, this function is called automatically from processPacket.
	 *
	 * @param routerIdentifier Originating router id given in hexadecimal.
	 * @param latency  Latency from the origin router.
	 * @param routes The neighbours of the origin router given as a list of Route objects.
	 * @return MESSAGE_ROUTE_RECEIVED (which is mapped to 'RECEIVED' in the code).
	 */
	private String processRoutingPacket(String routerIdentifier, int latency, List<Route> routes) {

		if(this.routingTable.containsKey(routerIdentifier)) {
			int lat = Integer.parseInt(this.routingTable.get(routerIdentifier)[1]);

			if(latency <= lat) {
				String[] routeDetails = new String[] {routerIdentifier, Integer.toString(latency)};
				this.routingTable.put(routerIdentifier, routeDetails);
			}
		}else{
			String[] routeDetails = new String[] {routerIdentifier, Integer.toString(latency)};
			this.routingTable.put(routerIdentifier, routeDetails);
		}

		for(Route r : routes) {
			if(this.routingTable.containsKey(r.forwardingRouter)) {
				String[] fRouter = this.routingTable.get(r.forwardingRouter);
				if(latency+r.latency <= Integer.parseInt(fRouter[1])) {
					String lat = Integer.toString(latency+r.latency);
					this.routingTable.put(r.forwardingRouter,new String[] {routerIdentifier,lat});
				}
			}else{
				String lat = Integer.toString(latency+r.latency);
				this.routingTable.put(r.forwardingRouter,new String[] {routerIdentifier,lat});
			}
		}

		return MESSAGE_ROUTE_RECEIVED;
	}
}