package ajw28.cs3105.p02.part2.fsm;

/**
 * Class for representing a state in a FSM
 */
public class State {

    /**
     * State ID
     */
    private int id;

    /**
     * Name of State
     */
    private String name;

    /**
     * Create State using a given name
     * @param name
     */
    public State(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get State Name
     * @return State Name
     */
    public String getName() {
        return name;
    }

    /**
     * Get ID
     * @return ID
     */
    public int getId() {
        return id;
    }
}
