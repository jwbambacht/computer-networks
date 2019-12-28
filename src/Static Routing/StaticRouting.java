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
