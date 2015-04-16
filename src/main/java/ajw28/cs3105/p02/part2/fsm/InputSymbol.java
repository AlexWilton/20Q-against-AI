package ajw28.cs3105.p02.part2.fsm;

/**
 * Class for representing a Input Symbol in a FSM
 */
public class InputSymbol {

    /**
     * State ID
     */
    private int id;

    /**
     * String Representation of Input Symbol
     */
    private String strRepresentation;

    /**
     * Create State using a given strRepresentation
     * @param strRepresentation
     */
    public InputSymbol(int id, String strRepresentation) {
        this.id = id;
        this.strRepresentation = strRepresentation;
    }

    /**
     * Get Input Symbol Name
     * @return Input Symbol Name
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
