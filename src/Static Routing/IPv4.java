import java.util.*;

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