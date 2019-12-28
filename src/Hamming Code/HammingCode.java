class HammingCode {

	public static void main(String[] args) {
		
	    // Calculate hamming code given a bitsequence with its length and parity
	    long bitSequence = Long.parseLong("1010110", 2);
	    boolean parity = true;
		
	    System.out.println(Long.toBinaryString(calcHamming(bitSequence, 7, parity)));
	    
	    // Correct hamming code given a bitsequence with its length and parity
	    bitSequence = Long.parseLong("110111010001", 2);
	    parity = true;
	    System.out.println(Long.toBinaryString(checkHamming(bitSequence, 10, parity)));

	}

	// Returns the hamming code of the given bit sequence.
	public static long calcHamming(long bitSequence, int inputLength, boolean isEvenParity) {
		if(inputLength == 0) {
			return (long) 0;
		}
		int m = (int) Math.floor(Math.log(bitSequence)/ Math.log(2)) + 1;

		String bitString = Long.toBinaryString(bitSequence);

		// Adding missing leading zeros
		if(m < inputLength) {
			int mm = inputLength-m;

			for(int i = 0; i < mm; i++) {
				bitString = "0"+bitString;
			}
		}

		char[] bits;

		if(bitSequence == 0) {
			String zeroBitString = "";
			for(int i = 0; i < inputLength; i++) {
				zeroBitString += "0";
			}
			bits = zeroBitString.toCharArray();
		}else{
			bits = bitString.toCharArray();  
		}

		m = inputLength;

		// Determine the amount of parity bits r
		int r = 0;

		while(m+r+1 > Math.pow(2,r)) {
			r++;
		}

		// Compute the powers of 2 and parity bits, knowing the total size of m+r;
		int cols = (int) Math.pow(2,r);
		int[] powers = new int[r];

		for(int i = 0; i < r; i++) {
			powers[i] = (int) Math.pow(2,i);
		}

		// Determine the location of the parity bits and save them in bitMatrix as -1
		int[] bitMatrix = new int[cols+1];
		int pointer = 0;
		int it = 1;

		while(pointer < m) {
			if(((it & (it - 1)) == 0)) {
				bitMatrix[it] = -1;
			}else{
				bitMatrix[it] = Integer.parseInt(String.valueOf(bits[pointer]));
				pointer++;
			}
			it++;
		}

		// Determine the value of the parity bits
		int[] parityCount = new int[r];

		for(int p = 0; p < r; p++) {
			int power = powers[p];
			int count = power;
			int parityC = 0;

			while(count < r+m+1) {

				for(int i = count; i < count+power; i++) {
					if(i > r+m+1){
						break;
					}

					if(bitMatrix[i] == 1) {
						parityC++;
					}
				}
				count += 2*power;
			}

			if((!isEvenParity && parityC % 2 == 0) || (isEvenParity && parityC % 2 != 0)) {
				parityCount[p] = 1; 
			}
		}

		int[] hammingCode = bitMatrix;

		int c = 0;
		String result = "";

		for(int i = 1; i < cols+1; i++) {
			if(bitMatrix[i] == -1) {
				hammingCode[i] = parityCount[c];
				c++;
			}
		}


		for(int i = 1; i < m+r+1; i++) {
			result += hammingCode[i];
		}

		return Long.parseLong(result,2);

	}

	// Returns the corrected hamming code of the given bit sequence.
	public static long checkHamming(long bitSequence, int inputLength, boolean isEvenParity) {

		if(inputLength == 0) {
			return (long) 0;
		}

		if(bitSequence == 0 && isEvenParity) {
			return bitSequence;
		}

		int m = (int) Math.floor(Math.log(bitSequence)/ Math.log(2)) + 1;

		String bitString = Long.toBinaryString(bitSequence);

		if(m < inputLength) {
			int mm = inputLength-m;

			for(int i = 0; i < mm; i++) {
				bitString = "0"+bitString;
			}
		}

		m = inputLength;

		char[] bits;
		String zeroBitString = "";

		if(bitSequence == 0) {
			for(int i = 0; i < inputLength; i++) {
				zeroBitString += "0";
			}
			bits = zeroBitString.toCharArray();
		}else{
			bits = bitString.toCharArray();  
		}

		int r = 0;

		while(Math.pow(2,r) <= m) {
			r++;
		}

		int[] powers = new int[r];

		for(int i = 0; i < r; i++) {
			powers[i] = (int) Math.pow(2,i);
		}

		int bitNumber = 0;

		if(bitSequence == 0) {

			int count = 0;

			for(int p = 0; p < r; p++) {
				count += powers[p];
			}

			long res = bitSequence ^ (1 << count-1);

			return res;

		}else{
			for(int p = 0; p < r; p++) {
				int power = powers[p];
				int count = power;
				int parityCount = 0;

				while(count < m+1) {

					for(int i = count; i < count+power; i++) {
						if(i > m){
							break;
						}

						if(bits[i-1] == '1') {
							parityCount ++;
						}

					}
					count += 2*power;
				}

				if((!isEvenParity && parityCount % 2 == 0) || (isEvenParity && parityCount % 2 != 0)) {
					bitNumber += power;
				}

			}

			if(bitNumber == 0) { 
				return bitSequence;
			}

			if(bitNumber > inputLength) {
				return bitSequence;
			}

			if(bits[bitNumber-1] == '1') {
				bits[bitNumber-1] = '0';
			}else{
				bits[bitNumber-1] = '1';
			}

			String result = "";

			for(int i = 0; i < inputLength; i++) {
				if(bits[i] == '1') {
					result += "1";
				}else{
					result += "0";
				}
			}
			return Long.parseLong(result,2);

		}
	}
}
