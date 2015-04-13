package ajw28.cs3105.p02.part2.fsm;

import java.util.Vector;

public class FSMModel {

	private class Transition {
		private int fromState;
		private int inSym;
		private int outSyms[] = new int[0];
		private int toState;

		private Transition(int fromState, int inSym, int[] outSyms, int toState) {
			this.fromState = fromState;
			this.inSym = inSym;
			this.outSyms = outSyms;
			this.toState = toState;
		}

		public int getFromState() {
			return fromState;
		}
		public int getInSym() {
			return inSym;
		}
		public int[] getOutSyms() {
			return outSyms;
		}
		public int getToState() {
			return toState;
		}	
	}
	
	private Vector<Transition> transitions= new Vector<Transition>();
	private String inSyms[];
	private String outSyms[];
	private Vector<String> states = new Vector<String>();
	private int startState;
	private int stateNow = -1;
	
	public FSMModel(String[] inSyms, String[] outSyms) {
		this.inSyms = inSyms;
		this.outSyms = outSyms;
	}
	
	private int stateByName( String name) {
		int pos = states.indexOf(name);
		if (pos == -1) 
		{	
				states.add(name);
				pos = states.indexOf(name);
		}
		return pos;
	}
	
	public void SetStartState( String state) {
		startState = stateByName(state);
	}
	
	private int InSymByName(String symName) throws BadSymbolException {
		for (int i = 0; i < inSyms.length; i++) {
			if (inSyms[i].equals(symName))
				return i;
		
		}
		throw new BadSymbolException(symName);	
	}
	
	private int OutSymByName(String symName) throws BadSymbolException {
		for (int i = 0; i < outSyms.length; i++) {
			if (outSyms[i].equals(symName))
				return i;
		}
		throw new BadSymbolException(symName);	
	}
	
	private Transition findTransition( int state, int inSym) {
		for (Transition t : transitions) {
			if (t.fromState == state && t.inSym == inSym) {
				return t;
			}
		}
		return null;
	}
	
	public void AddTransition( String fromState, String inSym,  String outSyms[], String toState) 
		throws BadSymbolException, NonDeterminismException {
		int fromStateNum = stateByName(fromState);
		int inSymNum = InSymByName(inSym);
		if ( null != findTransition(fromStateNum, inSymNum))
			throw new NonDeterminismException(fromState, inSym);
		int outSymNums[] = new int[outSyms.length];
		for (int i = 0; i < outSyms.length; i++) 
			outSymNums[i] = OutSymByName(outSyms[i]);
		transitions.add( new Transition(fromStateNum ,
				inSymNum, outSymNums, stateByName(toState)));
	}
	
	public boolean IsCompleteTransitionTable() {
		for (int i = 0; i < states.size(); i++)
			for (int j = 0; j < inSyms.length; j++)
				if (findTransition(i,j) == null)
					return false;
		return true;
	}
	
	public String[] processSymbol( String sym) throws BadSymbolException, NotInitialisedException {
		if (stateNow == -1)
			throw new NotInitialisedException();
		int symNum = InSymByName(sym);
		Transition t = findTransition( stateNow, symNum );
		stateNow = t.getToState();
		int outSymNums[] = t.getOutSyms();
		String[] outSymsList = new String[outSymNums.length];
		for (int i = 0; i < outSymNums.length; i++)
			outSymsList[i] = outSyms[outSymNums[i]];
		return outSymsList;
	}
	
	public void ResetMachine() {
		stateNow = startState;
	}
	
}
