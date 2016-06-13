package dataobjects;
import java.util.HashSet;
import java.util.Set;

public class DNATreeNode {

	
	private Set<NBAState> states = new HashSet<NBAState>();
	private int parent = 0;
	public Set<NBAState> getStates() {
		return states;
	}
	public void setStates(Set<NBAState> states) {
		this.states = states;
	}
	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}

	
	@Override
	public boolean equals(Object obj) {

		DNATreeNode other = (DNATreeNode) obj;
		
		return (this.getParent() == other.getParent() && this.getStates().equals(other.getStates()));
	}
	
	
	
	@Override
	public int hashCode() {
		
		int hash = 0;
		
		hash = 37 * hash + this.getParent();
		for (NBAState state : this.getStates()) {
			hash = 37 * hash + state.hashCode();
		}

		return hash;
	}
	
	public boolean isEliminated() {
		return (this.getParent() == 0 && this.getStates().isEmpty());
	}
}
