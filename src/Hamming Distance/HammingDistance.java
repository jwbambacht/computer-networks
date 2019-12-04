import java.util.ArrayList;
import java.util.List;

class HammingDistance {

	public static void main(String[] args) {

		ArrayList<Long> list = new ArrayList<>();
		list.add(Long.parseLong("00000000", 2));
		list.add(Long.parseLong("00001111", 2));
		list.add(Long.parseLong("11110000", 2));
		list.add(Long.parseLong("11111111", 2));

		System.out.println(calculate(list));

	}

	// Returns the hamming distance of the code, or -1 if it cannot be calculated.
	public static long calculate(List<Long> code) {

		if(code.size() <= 1) {
			return (long) -1;
		}

		int x = 0;
		long minDistance = Long.MAX_VALUE;

		while(x < code.size()) {
			for(int j = x; j < code.size(); j++) {
				if(j != x) {
					long dist = Long.bitCount(code.get(x)^code.get(j));
					if(dist < minDistance) {
						minDistance = dist;
					}
				}
			}
			x++;
		}

		return minDistance;

	}
}
