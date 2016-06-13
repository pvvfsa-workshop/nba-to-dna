package dataobjects;

public class StateLetterPair<T extends State> implements Comparable<StateLetterPair<T>> {

	private T state;
	private Character letter;
	public T getState() {
		return state;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getState().getName() + ";" + this.getLetter(); 
	}
	public void setState(T state) {
		this.state = state;
	}
	@Override
	public boolean equals(Object obj) {

		@SuppressWarnings("unchecked")
		StateLetterPair<T> other = (StateLetterPair<T>) obj;
		
		return (other.getLetter().equals(this.getLetter()) && other.getState().equals(this.getState()));
		
	}
	@Override
	public int hashCode() {
		
		int hash = 0;
		
		hash = hash * 37 + (int)this.getLetter();
		hash = hash * 37 + this.getState().hashCode();
		
		return hash;
	}
	public Character getLetter() {
		return letter;
	}
	public void setLetter(Character letter) {
		this.letter = letter;
	}
	public StateLetterPair(T state, Character letter) {
		super();
		this.state = state;
		this.letter = letter;
	}
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(StateLetterPair<T> o) {

		Comparable<T> thisState = (Comparable<T>)this.getState();
		T otherState = o.getState();
		
		
		if (thisState.compareTo(otherState) == 0) {
			
			Character thisLetter = this.getLetter();
			Character otherLetter = o.getLetter();
			
			return thisLetter.compareTo(otherLetter);
			
		} else {
			return thisState.compareTo(otherState);
		}
	}
}
