package dataobjects;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import graphviz.GraphVizHelper;

public class NBAtoDNA {

	public static void main(String [] args) throws IOException
	{
		HashSet<Character> alphabet = new HashSet<Character>();
		alphabet.add('0');
		alphabet.add('1');
		HashSet<NBAState> states = new HashSet<NBAState>();
		Map<StateLetterPair<NBAState>, Set<NBAState>> transFunc = new HashMap<StateLetterPair<NBAState>, Set<NBAState>> ();
		FileReader input = null;
		String myLine;
		boolean startingState;
		boolean acceptingState;
		
		if (args.length <= 1) {
			System.out.println("No input or output recieved. Quitting.");
			return;
			
		}
		
		String inputPath = args[0];
		String outputPath = args[1];
		
		try {
			input = new FileReader(inputPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bufRead = new BufferedReader(input);
		while ((myLine = bufRead.readLine()) != null)
		{    
			if (!myLine.contains("->")) // a line with state, add it to NBA states
			{
				String [] parseStr = myLine.split("\\[");
				String State_name = parseStr[0];
				State_name = State_name.replaceAll("\\s+","");
				//System.out.println(State_name);
				startingState = myLine.contains("*");
				acceptingState = myLine.contains("$");
				NBAState nba_state = new NBAState(State_name, startingState, acceptingState);
				states.add(nba_state);
			}
			else // a line with transition
			{
				startingState = false;
				acceptingState = false;
				//System.out.println("false");
				String [] parse_startState = myLine.split("->");
				String State_name = parse_startState[0];
				State_name = State_name.replaceAll("\\s+","");
				//System.out.println(State_name);
				String [] parse_endState = parse_startState[1].split("\\[");
				String end_State = parse_endState[0];
				end_State = end_State.replaceAll("\\s+","");
				//System.out.println(end_State);
				Character letter = parse_endState[1].charAt(6);
				NBAState nbastate = findStateByName(states, State_name);
				StateLetterPair<NBAState> key = new StateLetterPair<NBAState>(nbastate, letter);
				//System.out.println(key);
				NBAState nba_endState=null;
				for (NBAState state: states)
				{
					if (state.getName().equals(end_State))
						nba_endState = state;
				}
				if (!transFunc.containsKey(key))
				{
					HashSet<NBAState> st = new HashSet<NBAState>();
					st.add(nba_endState);
					transFunc.put(key,st);
				}
				else 
				{
					transFunc.get(key).add(nba_endState);
				}
			}	
		}
		//create a NBA obj
		NBA nba = new NBA(alphabet,states, transFunc);
		System.out.println(GraphVizHelper.graphVizFromNBA(nba));
		// Do the construction
		DNA dna = new DNA(nba);
		
		// Output the construction
		dna.DNAOutput(outputPath);
	}
	
	public static NBAState findStateByName(Set<NBAState> states, String name) {
		
		for (NBAState state : states) {
			if (state.getName().equals(name)) {
				return state;
			}
		}
		
		return null;
	}
	
	public static String buildDNA(String nbaString) {
		
		HashSet<Character> alphabet = new HashSet<Character>();
		alphabet.add('0');
		alphabet.add('1');
		HashSet<NBAState> states = new HashSet<NBAState>();
		Map<StateLetterPair<NBAState>, Set<NBAState>> transFunc = new HashMap<StateLetterPair<NBAState>, Set<NBAState>> ();
		String myLine;
		boolean startingState;
		boolean acceptingState;
		
		String[] lines = nbaString.split(System.getProperty("line.separator"));
		//BufferedReader bufRead = new BufferedReader(input);
		//while ((myLine = strRead.readLine()) != null)
		for (int i = 0; i < lines.length; i++)
		{
			myLine = lines[i];
			if (!myLine.contains("->")) // a line with state, add it to NBA states
			{
				String [] parseStr = myLine.split("\\[");
				String State_name = parseStr[0];
				State_name = State_name.replaceAll("\\s+","");
				//System.out.println(State_name);
				startingState = myLine.contains("*");
				acceptingState = myLine.contains("$");
				NBAState nba_state = new NBAState(State_name, startingState, acceptingState);
				states.add(nba_state);
			}
			else // a line with transition
			{
				startingState = false;
				acceptingState = false;
				//System.out.println("false");
				String [] parse_startState = myLine.split("->");
				String State_name = parse_startState[0];
				State_name = State_name.replaceAll("\\s+","");
				//System.out.println(State_name);
				String [] parse_endState = parse_startState[1].split("\\[");
				String end_State = parse_endState[0];
				end_State = end_State.replaceAll("\\s+","");
				//System.out.println(end_State);
				Character letter = parse_endState[1].charAt(6);
				NBAState nbastate = findStateByName(states, State_name);
				StateLetterPair<NBAState> key = new StateLetterPair<NBAState>(nbastate, letter);
				//System.out.println(key);
				NBAState nba_endState=null;
				for (NBAState state: states)
				{
					if (state.getName().equals(end_State))
						nba_endState = state;
				}
				if (!transFunc.containsKey(key))
				{
					HashSet<NBAState> st = new HashSet<NBAState>();
					st.add(nba_endState);
					transFunc.put(key,st);
				}
				else 
				{
					transFunc.get(key).add(nba_endState);
				}
			}	
		}
		//create a NBA obj
		NBA nba = new NBA(alphabet,states, transFunc);
		//System.out.println(GraphVizHelper.graphVizFromNBA(nba));
		// Do the construction
		DNA dna = new DNA(nba);
		
		return GraphVizHelper.graphVizFromDNA(dna);
	}
}
