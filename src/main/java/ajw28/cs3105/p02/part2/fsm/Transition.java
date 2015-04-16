package ajw28.cs3105.p02.part2.fsm;

/**
 * Class for representing a Transition in a FSM
 */
public class Transition {

    /**
     * State which the Transition changes from
     */
    private State origin;

    /**
     * State which the Transition changes to
     */
    private State destination;

    /**
     * Input Symbol
     */
    private String inputSymbol;

    /**
     * Output Symbol
     */
    private String outputSymbol;

    /**
     * Construct a Transition Object
     * @param origin Originating State
     * @param destination Destination State
     * @param inputSymbol Input Symbol
     * @param outputSymbol Output Symbol
     */
    public Transition(State origin, State destination, String inputSymbol, String outputSymbol) {
        this.origin = origin;
        this.destination = destination;
        this.inputSymbol = inputSymbol;
        this.outputSymbol = outputSymbol;
    }

    /**
     * Get Originating State
     * @return Originating State
     */
    public State getOrigin() {
        return origin;
    }

    /**
     * Get Destination State
     * @return Destination State
     */
    public State getDestination() {
        return destination;
    }

    /**
     * Get Input Symbol
     * @return Input Symbol
     */
    public String getInputSymbol() {
        return inputSymbol;
    }

    /**
     * Get Output Symbol
     * @return Output Symbol
     */
    public String getOutputSymbol() {
        return outputSymbol;
    }
}