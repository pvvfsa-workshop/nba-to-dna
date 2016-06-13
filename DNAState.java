package dataobjects;

public class DNAState extends State implements Comparable<DNAState> {

	private int maxTreeSize;
	private DNATreeNode[] DNATree;
	private int[] uniquenessArray;
	
	public int getMaxTreeSize() {
		return maxTreeSize;
	}

	public void setMaxTreeSize(int maxTreeSize) {
		this.maxTreeSize = maxTreeSize;
	}

	public DNATreeNode[] getDNATree() {
		return DNATree;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getName();
	}

	public void setDNATree(DNATreeNode[] dNATree) {
		DNATree = dNATree;
	}
	
	public int[] getUniquenessArray() {
		return uniquenessArray;
	}

	public void setUniquenessArray(int[] uniquenessArray) {
		this.uniquenessArray = uniquenessArray;
	}
	
	public DNAState(String name, boolean isStarting, boolean isAccepting, int maxTreeSize) {
		super(name, isStarting, isAccepting);
		this.maxTreeSize = maxTreeSize;
		this.uniquenessArray = new int [maxTreeSize / 2];
		DNATree = new DNATreeNode[maxTreeSize];
		
		for (int i = 0; i < maxTreeSize; i++) {
			DNATree[i] = new DNATreeNode();
		}
		
		DNATree[0].setParent(-1);
	}
	/*
	public String toGraphVizString() {
		
		StringBuilder sb = new StringBuilder();
		boolean isEliminated = false;
		DNATreeNode currNode = null;
		
		for (int i = 0; i < maxTreeSize && !isEliminated; i++) {
			
			currNode = DNATree[i];
			
			if (currNode.getParent() == 0 && currNode.getStates().size() == 0) {
				isEliminated = true;
			} else {
				sb.append(i);
				sb.append(" [label=\"");
				sb.append(i);
				sb.append(": {");
				
				for (NBAState state : currNode.getStates()) {
					sb.append(state.getName());
					sb.append(", ");
				}
				
				if (currNode.getStates().size() > 0) {
					sb.setLength(sb.length() - 2);
				}
				
				sb.append("}\"]");
				sb.append(System.getProperty("line.separator"));
				
				if (currNode.getParent() != -1) {
					sb.append(currNode.getParent());
					sb.append(" -> ");
					sb.append(i);
					sb.append(System.getProperty("line.separator"));
				}
			}
		}
		
		return sb.toString();
		
	}*/

	@Override
	public boolean equals(Object obj) {

		DNAState other = (DNAState)obj;
		
		for (int i = 0; i < DNATree.length; i++) {
			if (!(other.getDNATree()[i].equals(this.getDNATree()[i]))) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public int hashCode() {
		
		int hash = 0;
		
		for (int i = 0; i < maxTreeSize; i++) {
			hash = 37 * hash + DNATree[i].hashCode();
		}
		
		return hash;
	}

	@Override
	public int compareTo(DNAState o) {

		int thisNum = Integer.parseInt(this.getName().split("Q")[1]);
		int otherNum = Integer.parseInt(o.getName().split("Q")[1]);
		
		if (otherNum > thisNum) {
			return -1;
		} else if (otherNum < thisNum) {
			return 1;
		} else {
			return 0;
		}
	}
	
	
}
