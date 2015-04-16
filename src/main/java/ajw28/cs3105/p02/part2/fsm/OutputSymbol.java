package ajw28.cs3105.p02.part2.fsm;

/**
 * Class for representing a Output Symbol in a FSM
 */
public class OutputSymbol {

    /**
     * State ID
     */
    private int id;

    /**
     * String Representation of Output Symbol
     */
    private String strRepresentation;

    /**
     * Create State using a given strRepresentation
     * @param strRepresentation
     */
    public OutputSymbol(int id, String strRepresentation) {
        this.id = id;
        this.strRepresentation = strRepresentation;
    }

    /**
     * Get Output Symbol Name
     * @return Output Symbol Name
     */
    public String getStrRepresentation() {
        return strRepresentation;
    }

    /**
     * Get ID
     * @return ID
     */
    public int getId() {
        return id;
    }
}
