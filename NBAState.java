package dataobjects;

public class NBAState extends State implements Comparable<NBAState> {

	public NBAState(String name, boolean isStarting, boolean isAccepting) {
		super(name, isStarting, isAccepting);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		
		NBAState other = (NBAState) obj;
		
		return (this.getName().equals(other.getName()));
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getName();
	}

	@Override
	public int hashCode() {

		int hash = 0;
		
		hash = 37 * hash + this.getName().hashCode();
		
		return hash;
	}

	@Override
	public int compareTo(NBAState o) {

		int thisNum = Integer.parseInt(this.getName().split("q")[1]);
		int otherNum = Integer.parseInt(o.getName().split("q")[1]);
		
		if (otherNum > thisNum) {
			return -1;
		} else if (otherNum < thisNum) {
			return 1;
		} else {
			return 0;
		}
	}
}
