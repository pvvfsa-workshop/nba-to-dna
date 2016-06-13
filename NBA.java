package dataobjects;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class NBA extends Automaton<NBAState> {

	public NBA(Set<Character> alphabet, Set<NBAState> states, Map<StateLetterPair<NBAState>, Set<NBAState>> transFunc) {
		super(alphabet, states, transFunc);
		// TODO Auto-generated constructor stub
	}
	
	public void packBeforeCreateDNA(){
		
		TreeSet<NBAState> sortedStates = new TreeSet<NBAState>(this.getStates());
		int i=0;
		for (NBAState q : sortedStates){
			q.setName("q" + i);
			i++;
		}
	}
	/*
	@Override
	public String toGraphVizString() {
		StringBuilder text = new StringBuilder();
		
		text.append("node [shape = doublecircle]; ");
		for (NBAState q : this.getAcceptingStates()) {
			text.append(q.getName());
			text.append(" ");
		}
		text.append(System.getProperty("line.separator"));
		text.append("node [shape = circle]; ");
		for (NBAState q : this.getStates()) {
			text.append(q.getName());
			text.append(" ");
		}
		text.append(System.getProperty("line.separator"));
		text.append("node [style = invisible]; ");
		text.append(System.getProperty("line.separator"));
		for (NBAState q : this.getStartingStates()) {
			int number = Integer.parseInt(q.getName().split("q")[1]);
			text.append(number);
			text.append(" -> ");
			text.append(q.getName());
			text.append("[label=\"start\"]");
			text.append(System.getProperty("line.separator"));
		}
		
		TreeSet<NBAState> sortedStates = new TreeSet<NBAState>(this.getStates());
		for (NBAState q : sortedStates){
        	
        	text.append(q.getName() + " [label=\"");
        	//if (q.isStarting()){
        	//	text.append("*");
        	//}
        	text.append(q.getName());
        	//if (q.isAccepting()){
        	//	text.append("$");
        	//}
    		text.append("\"]");
    		text.append(System.getProperty("line.separator"));
        }
        for (NBAState q : sortedStates){
        	
        	Set<NBAState> endState;
        	
        	for (Character c : getAlphabet()) {
        		endState = this.getTransition(q, c);
        		for (NBAState st : endState){
        			text.append(q.getName());
            		text.append(" -> ");
            		text.append(st.getName());
            		text.append(" [label=");
            		text.append(c.toString());
            		text.append("]");
            		text.append(System.getProperty("line.separator"));
        		}
        	}
        	
        }
		return text.toString();
	}*/
	
	public NBAState createState(int stateNum, boolean isStarting, boolean isAccepting) {
		String stateName = "q" + (stateNum);
		NBAState state = new NBAState(stateName, isStarting, isAccepting);
		this.getStates().add(state);
		
		if (state.isStarting()) {
			this.getStartingStates().add(state);
		}
		
		if (state.isAccepting()) {
			this.getAcceptingStates().add(state);
		}
		
		return state;
	}
	
	public void deleteState(String name) {

		NBAState stateToDelete = findState(new NBAState(name, false, false));
		Set<NBAState> transition;
		
		// Remove state itself
		this.getStates().remove(stateToDelete);
		this.getStartingStates().remove(stateToDelete);
		this.getAcceptingStates().remove(stateToDelete);
		
		// Remove transitions from state
		for (Character c : this.getAlphabet()) {
			
			StateLetterPair<NBAState> stateLetter = new StateLetterPair<NBAState>(stateToDelete, c);
			this.getTransitionFunction().remove(stateLetter);
		}
		
		// Remove transitions to state
		for (NBAState state : this.getStates()) {
			for (Character c : this.getAlphabet()) {
				
				transition = this.getTransition(state, c);
				transition.remove(stateToDelete);
				
				if (transition.size() > 0) {
					this.setTransition(state, c, transition);
				} else {
					// Check this code
					StateLetterPair<NBAState> stateLetter = new StateLetterPair<NBAState>(state, c);
					this.getTransitionFunction().remove(stateLetter);
				}

			}
		}
	}
	
	public void createTrans(Character c, String from, String to) {
		
		NBAState fromState = findState(new NBAState(from, false, false));
		NBAState toState = findState(new NBAState(to, false, false));
		
		Set<NBAState> transition = this.getTransition(fromState, c);
		transition.add(toState);
		this.setTransition(fromState, c, transition);
	}
	
	public void deleteTrans(Character c, String from, String to) {
		
		NBAState fromState = findState(new NBAState(from, false, false));
		NBAState toState = findState(new NBAState(to, false, false));
		
		// Remove from transition table
		Set<NBAState> transition = this.getTransition(fromState, c);
		transition.remove(toState);
		
		if (transition.size() > 0) {
			this.setTransition(fromState, c, transition);
		} else {
			// Check this code
			StateLetterPair<NBAState> stateLetter = new StateLetterPair<NBAState>(fromState, c);
			this.getTransitionFunction().remove(stateLetter);
		}
	}
}
