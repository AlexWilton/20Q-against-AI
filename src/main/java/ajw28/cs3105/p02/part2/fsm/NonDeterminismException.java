package ajw28.cs3105.p02.part2.fsm;

public class NonDeterminismException extends Exception {

	public String state;
	public String symbol;
	public String toString() {
		return "Adding this transition would make machine non-deterministic with two transitions from "
		+state+" on "+symbol;
	}
	
	public NonDeterminismException(String state, String symbol) {
		super();
		// TODO Auto-generated constructor stub
		this.state = state;
		this.symbol = symbol;
	}
	
}
