class CRC {

	public static void main(String[] args) {

		// Calculate CRC
		long bitSequence = Long.parseLong("11010011101100", 2);
		long generatorSequence = Long.parseLong("1011", 2);

		System.out.println(Long.toBinaryString(calculateCRC(bitSequence,14,generatorSequence)));

		// Check the CRC 
		bitSequence = Long.parseLong("11010011101100", 2);
		generatorSequence = Long.parseLong("1011", 2);
		long checkSequence = Long.parseLong("100", 2);
		System.out.println(checkCRC(bitSequence,14,generatorSequence,checkSequence));

	}

	public static long calculateCRC(long bitSequence, int inputLength, long generatorSequence) {

		int generatorLength = (int) (Math.floor(Math.log(generatorSequence)/ Math.log(2)) + 1);

		if(inputLength < generatorLength) {
			return (long) -1;
		}

		bitSequence <<= generatorLength-1;

		return division(bitSequence,generatorSequence);

	}

	
	public static boolean checkCRC(long bitSequence, int inputLength, long generatorSequence, long checkSequence) {

		return calculateCRC(bitSequence, inputLength, generatorSequence) == checkSequence;

	}

	public static long division(long data, long generator) {

		int generatorLength = (int) (Math.floor(Math.log(generator)/ Math.log(2)) + 1);
		int dataLength = (int) (Math.floor(Math.log(data)/ Math.log(2)) + 1);

		int pointer = generatorLength+1;

		long temp = (((1 << generatorLength)-1) & (data >> (dataLength-pointer+1)));

		while(pointer < dataLength) {
			long point = ((data >> (dataLength-pointer)) & 1);

			if(((temp >> (generatorLength-1)) & 1) == 1) {
				temp = ((temp ^ generator) << 1);
			}else {			
				temp = ((temp ^ 0) << 1);
			}

			temp = temp ^ point;

			pointer++;
		}

		if(((temp >> (generatorLength-1)) & 1) == 1) {
			temp = (temp ^ generator) << 1;
		}else {
			temp = ((temp ^ 0) << 1);
		}

		if(((temp >> (generatorLength-1)) & 1) == 1) {
			temp = (temp ^ generator);
		}

		return temp;
	}

}
