package ajw28.cs3105.p02.part2.fsm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class FSM {

	static final String[] ISyms = {"a", "b", "c", "r", "c10", "c20", "c50"};
	static final String[] OSyms = {"C10", "C20", "C50", "A", "B", "C", "beep"};
	public static final String FILE_PATH = "vendingMachine.fsm";

	public static FSM parse() {
		Scanner s = null;
		try {
			s = new Scanner(new File(FILE_PATH));
		} catch (FileNotFoundException e) {
			System.err.println("fsm file " + FILE_PATH + " not found " + e);
			System.exit(-1);
		}
		Scanner s2;
		String line;
		String os;
		FSM fsm = new FSM(ISyms, OSyms);
		do {
			if (!s.hasNext()) {
				System.out.println("Empty input file " + FILE_PATH);
				System.exit(-1);
			}
			line = s.nextLine();
			int hashpos = line.indexOf('#');
			if (hashpos != -1)
				line = line.substring(0, hashpos);
			s2 = new Scanner(line);
		} while (!s2.hasNext());
		fsm.SetStartState(s2.next());
		if (s2.hasNext()) {
			System.out.println("Too many tokens on first line, should just be start state name");
			System.out.println("  dicarding " + s2.nextLine());
		}
		while (s.hasNext()) {
			line = s.nextLine();
			int hashpos = line.indexOf('#');
			if (hashpos != -1)
				line = line.substring(0, hashpos);
			s2 = new Scanner(line);
			if (!s2.hasNext())
				continue;
			String fstate = s2.next();
			if (!s2.hasNext())
				System.out.println("line " + line + " has too few tokens, ignoring it");
			else {
				String onsym = s2.next();
				if (!s2.hasNext())
					System.out.println("line " + line + " has too few tokens, ignoring it");
				else {
					Vector<String> osyms = new Vector<String>();
					while (true) {
						os = s2.next();
						if (!s2.hasNext())
							break;
						osyms.add(os);
					}
					try {
						fsm.AddTransition(fstate, onsym, osyms.toArray(new String[osyms.size()]), os);
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		}
		fsm.ResetMachine();
		System.out.println("FSM Loaded.\n");
		return fsm;
	}

	private Vector<Transition> transitions= new Vector<Transition>();
	private String inSyms[];
	private String outSyms[];
	private Vector<String> states = new Vector<String>();
	private int startState;
	private int stateNow = -1;
	
	public FSM(String[] inSyms, String[] outSyms) {
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
			if (t.getFromState() == state && t.getInSym() == inSym) {
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
