package ajw28.cs3105.p02.part2.fsm;

public class Transition {
    private int fromState;
    private int inSym;
    private int outSyms[] = new int[0];
    private int toState;

    public Transition(int fromState, int inSym, int[] outSyms, int toState) {
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