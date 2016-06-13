package dataobjects;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Automaton<T extends State> {

	private Set<Character> alphabet = new HashSet<Character>();
	private Set<T> states = new HashSet<T>();
	private Map<StateLetterPair<T>, Set<T>> transitionFunction = new HashMap<StateLetterPair<T>, Set<T>>();
	
	private Set<T> startingStates = new HashSet<T>();;
	private Set<T> acceptingStates = new HashSet<T>();;
	
	public Set<T> getStartingStates() {
		return startingStates;
	}

	public Set<T> getAcceptingStates() {
		return acceptingStates;
	}

	public Automaton(Set<Character> alphabet, Set<T> states, Map<StateLetterPair<T>, Set<T>> transFunc) {
		this.alphabet = alphabet;
		this.states = states;
		this.transitionFunction = transFunc;
		
		for (T state : states) {
			if (state.isAccepting()) {
				acceptingStates.add(state);
			}
		}
		
		for (T state : states) {
			if (state.isStarting()) {
				startingStates.add(state);
			}
		}
	}
	
	public Automaton() {
		this.alphabet = new HashSet<Character>();
		this.states = new HashSet<T>();
		this.transitionFunction = new HashMap<StateLetterPair<T>, Set<T>>();
	}
	
	public Set<Character> getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(Set<Character> alphabet) {
		this.alphabet = alphabet;
	}

	public Set<T> getStates() {
		return states;
	}

	public void setStates(Set<T> states) {
		this.states = states;
	}

	public Map<StateLetterPair<T>, Set<T>> getTransitionFunction() {
		return transitionFunction;
	}

	public void setTransitionFunction(Map<StateLetterPair<T>, Set<T>> transitionFunction) {
		this.transitionFunction = transitionFunction;
	}

	public Set<T> getTransition(T state, Character letter) {
		StateLetterPair<T> stateLetter = new StateLetterPair<T>(state, letter);
		
		if (this.getTransitionFunction().get(stateLetter) == null) {
			setTransition(state, letter, new HashSet<T>());
		}
		
		return (this.getTransitionFunction().get(stateLetter));
	}
	
	public void setTransition(T state, Character letter, Set<T> transition) {
		this.getTransitionFunction().put(new StateLetterPair<T>(state, letter), transition);
	}
	
	protected T findState(T stateToFind) {
		
		for (T state : this.getStates()) {
			if (state.equals(stateToFind)) {
				return state;
			}
		}
		
		return null;
	}
}
