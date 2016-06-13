package dataobjects;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import graphviz.GraphVizHelper;

public class DNA extends Automaton<DNAState> {

	private Map<StateLetterPair<DNAState>, Integer> numberingFunction;
	public Map<StateLetterPair<DNAState>, Integer> getNumberingFunction() {
		return numberingFunction;
	}

	public void setNumberingFunction(Map<StateLetterPair<DNAState>, Integer> numberingFunction) {
		this.numberingFunction = numberingFunction;
	}
	
	public void setNumber(DNAState state, Character letter, Integer number) {
		this.numberingFunction.put(new StateLetterPair<DNAState>(state, letter), number);
	}
	
	public int getNumber(DNAState state, Character letter) {
		return this.numberingFunction.get(new StateLetterPair<DNAState>(state, letter));
	}

	public int k;

	public DNA(Set<Character> alphabet, Set<DNAState> states, Map<StateLetterPair<DNAState>, Set<DNAState>> transFunc, int k) {
		super(alphabet, states, transFunc);
		// TODO Auto-generated constructor stub
		
		this.k = k;
	}
	
	public DNA(NBA nba) {
		super();
		this.numberingFunction = new HashMap<StateLetterPair<DNAState>, Integer>();
		this.setAlphabet(nba.getAlphabet());
		this.k = nba.getStates().size() * 2;
		LinkedList<DNAState> stateList = new LinkedList<DNAState>();
		
		// Initial state
		DNAState initialState = createState(true);
		initialState.getDNATree()[0].setStates(nba.getStartingStates());
		this.getStates().add(initialState);
		this.getStartingStates().add(initialState);
		
		// Keep a queue of states to process
		stateList.addFirst(initialState);
		DNAState newState, currState;
		int g, b;
		
		// Keep looping until we exhausted all states
		while (!stateList.isEmpty()) {
			currState = stateList.removeLast();
			
			for (Character c : getAlphabet()) {
				
				// Process each state and letter
				newState = spawn(currState, c, nba);
				applySeniority(newState);
				g = restoreUniqueness(newState);
				b = pack(newState);
				
				// When we have a new state check whether it is a new state
				DNAState existingState = findState(newState);
				
				if (existingState == null) {
					this.getStates().add(newState);
					stateList.addFirst(newState);
				} else {
					newState = existingState;
				}
				
				// Update transition function
				Set<DNAState> transition = new HashSet<DNAState>();
				transition.add(newState);
				setTransition(currState, c, transition);
				
				// Update number
				if (b <= g) {
					setNumber(currState, c, 2 * b);
				} else {
					setNumber(currState, c, (2 * g) + 1);
				}
			}
		}
		
		for (DNAState q : this.getStates()){	
			setUniqueArray(q, nba.getStates());		// for each state build a uniqueness array
		}
		
		relabelStates();
	}
	
	private void relabelStates() {
		
		TreeSet<DNAState> orderedStates = new TreeSet<DNAState>(new DNAComparator());
		orderedStates.addAll(this.getStates());
		
		// Relabel states according to uniqueness array
		int index = 0;
		for (DNAState state : orderedStates) {
			this.findState(state).setName("Q" + index);
			index++;
		}
	}
	
	private DNAState spawn(DNAState q, Character c, NBA nba) {
		
		DNAState newDNAState = createState(false);
		
		// Populate new state tree
		for (int i = 0; i < k / 2; i++) {
			
			DNATreeNode currNode = q.getDNATree()[i];
			Set<NBAState> newStates = new HashSet<NBAState>();
			
			// Calculate new states of current node
			for (NBAState state : currNode.getStates()) {
				newStates.addAll(nba.getTransition(state, c));
			}
			
			if (newStates.size() > 0) {
				newDNAState.getDNATree()[i].setStates(newStates);
				newDNAState.getDNATree()[i].setParent(currNode.getParent());
			}
			
			// k/2 = n
			// Calculate new states of the i + n node
			Set<NBAState> newChildStates = new HashSet<NBAState>();
			newChildStates.addAll(newStates);
			newChildStates.retainAll(nba.getAcceptingStates());
			
			if (newChildStates.size() > 0) {
				newDNAState.getDNATree()[i + k/2].setStates(newChildStates);
				newDNAState.getDNATree()[i + k/2].setParent(i);
			}
		}		
		
		return newDNAState;
		
		
	}
	
	// O(n)
	private void applySeniority(DNAState q) {
		
		Map<Integer, Set<NBAState>> uniqueStates = new HashMap<Integer, Set<NBAState>>();
		DNATreeNode currNode;
		Integer parent;
		Set<NBAState> states, parentStates;
		
		for (int i = 1; i < k; i++) {
			
			currNode = q.getDNATree()[i];
			parent = new Integer(currNode.getParent());
			
			// Get all states of parent so that we know which states to remove from juniors
			states = uniqueStates.get(parent);
			
			// Fix heredity
			parentStates = q.getDNATree()[currNode.getParent()].getStates();
			currNode.getStates().retainAll(parentStates);
			
			// This is the senior
			if (states == null) {
				states = new HashSet<NBAState>();
			} else { // Has an older brother
				
				// Remove states already contained in seniors
				for (NBAState state : states) {
					if (currNode.getStates().contains(state)) {
						currNode.getStates().remove(state);
					}
				}
			}
			
			states.addAll(currNode.getStates());
			uniqueStates.put(parent, states);
		}
	}
	
	// O(n^2)
	private int restoreUniqueness(DNAState q) {
		
		DNATreeNode currNode, parentNode;
		Set<NBAState> states;
		Set<Integer> children;
		int g = Integer.MAX_VALUE; // Smallest index whose descendants are eliminated
		
		for (int i = 0; i < k/2; i++) {
			
			// Accumulate states of parent to check for uniqueness violation
			// Accumulate also children to remove them later if needed
			states = new HashSet<NBAState>();
			children = new HashSet<Integer>();
			parentNode = q.getDNATree()[i];
			
			for (int j = 1; j < k; j++) {
				
				currNode = q.getDNATree()[j];
				if (currNode.getParent() == i) {
					
					// Eliminate son if parent is empty
					if (parentNode.getStates().isEmpty()) {
						eliminateNode(currNode);
						
					} else {  // Keep adding states
						states.addAll(currNode.getStates());
						children.add(new Integer(j));
					}
				}
			}
			
			// Check uniqueness
			if (parentNode.getStates().equals(states) && !states.isEmpty()) {
				for (Integer j : children) {
					eliminateNode(q.getDNATree()[j]);
				}
				
				if (i < g) {
					g = i;
				}
			}
		}
		
		return g;
	}
	
	// O(n^2)
	private int pack(DNAState q) {
		int b = -1; // Index of the first eliminated vertex
		DNATreeNode currNode, secCurrNode;
		
		for (int i = 0; i < k; i++) {
			currNode = q.getDNATree()[i];
			
			
			// Once found eliminated node pack rest of the tree
			if (currNode.isEliminated()) {
				if (b == -1) {
					b = i;
				}

				int j;
				for (j = i + 1; j < k; j++){
					
					secCurrNode = q.getDNATree()[j];
					
					// Pack non eliminated node 
					if (!secCurrNode.isEliminated()){
						currNode.setParent(secCurrNode.getParent());
						currNode.setStates(secCurrNode.getStates());
						eliminateNode(secCurrNode);
						break;
					}
				}
				
				for (int m = j + 1; m < k; m++){
					
					secCurrNode = q.getDNATree()[m];
					
					// Update parents
					if (secCurrNode.getParent() == j){
						secCurrNode.setParent(i);
					}		
				}
					
			}
		}
		
		return b;
	}
	
	private void eliminateNode(DNATreeNode d) {
		d.setParent(0);
		d.setStates(new HashSet<NBAState>());
	}
	
	private void setUniqueArray (DNAState q, Set<NBAState> nba_states){
		
		int n = nba_states.size();
		int max_index=0;
		boolean nba_stateFound = false;
		for (NBAState nba_state : nba_states){
			
			for (int i = 0; i < n; i++){
				
				if (q.getDNATree()[i].getStates().contains(nba_state)){
					
					nba_stateFound = true;
					if (i > max_index){
						max_index = i;
					}
				}
			}
			int nba_state_number = Integer.parseInt(nba_state.getName().split("q")[1]); 	//q0 -> 0 , q1 -> 1
			if (nba_stateFound){
				q.getUniquenessArray()[nba_state_number] = max_index;
			}
			else {
				q.getUniquenessArray()[nba_state_number] = -1;		//should print $ when contain -1 in array
			}
			max_index = 0;
			nba_stateFound = false;
		}
	}
	
	public DNAState createState(boolean isStarting) {
		int numStates = getStates().size();
		String stateName = "Q" + (numStates);
		DNAState state = new DNAState(stateName, isStarting, false, k);
		return state;
	}
	/*
	@Override
	public String toGraphVizString(){
		int n = k / 2;
		StringBuilder text = new StringBuilder();
		
		text.append("node [shape = circle]; ");
		for (DNAState q : this.getStates()) {
			text.append(q.getName());
			text.append(" ");
		}
		text.append(System.getProperty("line.separator"));
		text.append("node [style = invisible]; ");
		text.append(System.getProperty("line.separator"));
		for (DNAState q : this.getStartingStates()) {
			int number = Integer.parseInt(q.getName().split("Q")[1]);
			text.append(number);
			text.append(" -> ");
			text.append(q.getName());
			text.append("[label=\"start\"]");
			text.append(System.getProperty("line.separator"));
		}
		
		TreeSet<DNAState> sortedStates = new TreeSet<DNAState>(this.getStates());
        for (DNAState q : sortedStates){
        	
        		
        	// For GUI version
        	text.append(q.getName() + " [label=\"");
        	text.append(q.getName());
        	text.append("\"]");
        	
        	/*
        	text.append(q.getName() + " [label=\"[");
        	for (int i = 1; i < n-1; i++){
        		text.append(q.getDNATree()[i].getParent());
        		text.append(" ");
        	}
        	text.append(q.getDNATree()[n-1].getParent());
        	text.append("],[");
        	for (int i = 0; i < q.getUniquenessArray().length - 1; i++){
        		if (q.getUniquenessArray()[i] == -1){
        			text.append("$");
        		}
        		else {
        			text.append(q.getUniquenessArray()[i]);
        		}
        		text.append(" ");
        	}
        	if (q.getUniquenessArray()[q.getUniquenessArray().length - 1] == -1){
    			text.append("$");
    		}
    		else {
    			text.append(q.getUniquenessArray()[q.getUniquenessArray().length - 1]);
    		}
    		text.append("]\"]");
    		*//*

    		text.append(System.getProperty("line.separator"));
        }
        for (DNAState q : sortedStates){
        	
        	Set<DNAState> endState;
        	TreeSet<StateLetterPair<DNAState>> possibleStates = new TreeSet<StateLetterPair<DNAState>>();
        	
        	for (Character c : getAlphabet()) {
        		endState = this.getTransition(q, c);
        		possibleStates.add(new StateLetterPair<DNAState>(endState.iterator().next(), c));
        	}
        	
        	for (StateLetterPair<DNAState> stateLetter : possibleStates) {
        		text.append(q.getName());
        		text.append(" -> ");
        		text.append(stateLetter.getState().getName());
        		text.append(" [label=\"");
        		text.append(stateLetter.getLetter());
        		text.append("[");
        		text.append(getNumber(q, stateLetter.getLetter()));
        		text.append("]\"]");
        		text.append(System.getProperty("line.separator"));
        	}
        	
        }
		return text.toString();
	}*/
	
	public void DNAOutput(String outputPath){
		
		String text = GraphVizHelper.graphVizFromDNA(this);
		BufferedWriter output = null;
        try {
            File file = new File(outputPath);
            output = new BufferedWriter(new FileWriter(file));
            output.write(text);
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( output != null )
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
	}
	
	public String getTreeNodeGraph(String stateName) {
		
		DNAState currState = null;
		
		for (DNAState state : this.getStates()) {
			if (state.getName().equals(stateName)) {
				currState = state;
				break;
			}
		}
		
		return GraphVizHelper.graphVizFromDNAState(currState);
	}
}
