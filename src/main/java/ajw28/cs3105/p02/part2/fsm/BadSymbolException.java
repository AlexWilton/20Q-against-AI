package ajw28.cs3105.p02.part2.fsm;

public class BadSymbolException extends Exception {
	String name;

	BadSymbolException(String name) {
		super();
		this.name = name;
	}
	
	public String toString() {
		return "Bad Symbol "+name;
	}
	
}
