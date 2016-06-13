package dataobjects;

import java.util.Comparator;

public class DNAComparator implements Comparator<DNAState> {

	@Override
	public int compare(DNAState arg0, DNAState arg1) {

		int[] firstUniquenessArray = arg0.getUniquenessArray();
		int[] secondUniquenessArray = arg1.getUniquenessArray();
		
		if ("Q0".equals(arg0.getName())) {
			return -1;
		} else if ("Q0".equals(arg1.getName())) {
			return 1;
		}
		
		for (int i = 0; i < firstUniquenessArray.length; i++) {
			if (firstUniquenessArray[i] < secondUniquenessArray[i]) {
				return -1;
			} else if (firstUniquenessArray[i] > secondUniquenessArray[i]) {
				return 1;
			}
		}
		
		return 0;
	}

}
