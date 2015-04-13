package ajw28.cs3105.p02.part2.fsm;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;

public class FSMSim {
	static final String[] ISyms = {"a", "b", "c", "r", "c10", "c20", "c50"};
	static final String[] OSyms = {"C10", "C20", "C50", "A", "B", "C", "beep"};

	public static void main(String[] args) {
        String filepath = "vendingMachine.fsm";

		Scanner s;
		try {
			s = new Scanner(new File(filepath));
		} catch (FileNotFoundException e) {
			System.err.println("fsm file "+filepath+" not found "+e);
			return;
		}
		Scanner s2;
		String line;
		String os;
		FSMModel m = new FSMModel( ISyms, OSyms	);
		do {
			
			if (!s.hasNext())
			{
				System.out.println("Empty input file "+filepath);
				return;
			}
			line = s.nextLine();
			int hashpos = line.indexOf('#');
			if (hashpos != -1)
			    line = line.substring(0,hashpos);
			s2 = new Scanner(line);
		} while (!s2.hasNext());
		m.SetStartState(s2.next());
		if (s2.hasNext())
		{	
				System.out.println("Too many tokens on first line, should just be start state name");
				System.out.println("  dicarding "+s2.nextLine());
		}
		while (s.hasNext())
		{
			line = s.nextLine();
			int hashpos = line.indexOf('#');
			if (hashpos != -1)
			    line = line.substring(0,hashpos);
			s2 = new Scanner(line);
			if (!s2.hasNext())
				continue;
			String fstate = s2.next();
			if (!s2.hasNext()) 
				System.out.println("line "+line+" has too few tokens, ignoring it");
			else 
			{				
				String onsym = s2.next();
				if (!s2.hasNext()) 
					System.out.println("line "+line+" has too few tokens, ignoring it");
				else
				{
					Vector<String> osyms = new Vector<String>();
					while(true) {
						os = s2.next();
						if (!s2.hasNext())
							break;
						osyms.add(os);
					}
					try {
						m.AddTransition(fstate, onsym, osyms.toArray(new String[osyms.size()]), os);
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		}
		m.ResetMachine();
		System.out.println("FSM Loaded, ready for input\n");
		s = new Scanner(System.in);
		while (s.hasNext()) {
			try {
				String [] output = m.processSymbol(s.next());
				for (String ops: output)
					System.out.print(ops+" ");
				System.out.println("");
			} catch (BadSymbolException e) {
				System.err.println(e);
			} catch (NotInitialisedException e) {
				System.err.println("This should never happen");
				return;
			}
			
		}
	}

}
