import java.util.*;

class StaticRouting {

	public static int ERROR_NO_ROUTE = -1;
	public List<Route> routeMap;

	public static void main(String[] args) {
		StaticRouting table = new StaticRouting();
		table.addRoute(IPv4.toLong("123.234.79.0"), IPv4.toLong("255.255.255.0"), IPv4.toLong("157.55.27.91"));
		table.addRoute(IPv4.toLong("123.234.0.0"), IPv4.toLong("255.255.0.0"), IPv4.toLong("157.55.27.92"));
		table.addRoute(IPv4.toLong("123.234.0.1"), IPv4.toLong("255.0.0.0"), IPv4.toLong("157.55.27.93"));
		table.addRoute(IPv4.toLong("192.0.2.0"), IPv4.toLong("255.255.255.0"), IPv4.toLong("157.55.27.94"));
		long gateway = table.lookupRoute(IPv4.toLong("123.234.79.10"));

		System.out.println("Gateway: " + IPv4.toIP(gateway));
	}

	public StaticRouting() {
		this.routeMap = new ArrayList<>();
	}

	public void addRoute(long networkPrefix, long subnetMask, long gateway) {
		Route route = new Route(networkPrefix,subnetMask,gateway);
		this.routeMap.add(route);
	}

	public long lookupRoute(long address) {

		List<Route> results = new ArrayList<>();

		for(Route r : this.routeMap) {

			long prefix = r.calculatePrefix(address);

			if(r.networkPrefix == prefix) {
				results.add(r);
			}

		}

		if(results.size() == 1) {

			return results.get(0).getGateway();

		}else if(results.size() > 1) {

			long selectedLength = 0;
			int selectedIndex = 0;

			for(Route r : results) {
				long length = r.getLength();

				if(length > selectedLength) {
					selectedLength = length;
					selectedIndex = results.indexOf(r);
				}
			}

			return results.get(selectedIndex).getGateway();

		}

		return ERROR_NO_ROUTE;

	}
}

class Route {

	public long networkPrefix;
	public long subnetMask;
	public long gateway;

	public Route(long networkPrefix, long subnetMask, long gateway) {
		this.networkPrefix = networkPrefix;
		this.subnetMask = subnetMask;
		this.gateway = gateway;
	}

	public long calculatePrefix(long address) {
		return (address & this.subnetMask);
	}

	public long getSubnetMask() {
		return this.subnetMask;
	}

	public long getGateway() {
		return this.gateway;
	}

	public long getLength() {
		return Long.bitCount(this.subnetMask);
	}

	@Override
	public String toString() {
		return networkPrefix + " "+ this.subnetMask + " " + this.gateway + " " + this.getLength();
	}

}

class IPv4 {

	public static long toLong(String ip) {

		String[] parts = ip.split("\\.");
		String bitSequence = "";

		for(int i = 0; i < 4; i++) {
			String bits = Long.toBinaryString(Integer.parseInt(parts[i]));
			if(bits.length() < 8) {
				int toAppend = 8-bits.length();
				for(int j = 0; j < toAppend; j++) {
					bits = "0"+bits;
				}
			}

			bitSequence += bits;
		}

		return Long.parseLong(bitSequence,2);
	}

	public static String toIP(long bitSequence) {

		return ((bitSequence >> 24) & 0xFF) + "." + ((bitSequence >> 16) & 0xFF) + "." + ((bitSequence >> 8) & 0xFF) + "." + (bitSequence & 0xFF);
	}
}