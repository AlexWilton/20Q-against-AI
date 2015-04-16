package ajw28.cs3105.p02.part2.fsm;

/**
 * Class for representing a state in a FSM
 */
public class State {

    /**
     * Name of State
     */
    private String name;

    /**
     * Create State using a given name
     * @param name
     */
    public State(String name) {
        this.name = name;
    }

    /**
     * Get State Name
     * @return State Name
     */
    public String getName() {
        return name;
    }
}
