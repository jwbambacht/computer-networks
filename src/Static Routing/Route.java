import java.util.*;

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