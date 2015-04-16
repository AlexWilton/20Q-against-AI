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

    /**
     * Construct a binary string represented as a double array from the ID.
     * @param numOfBits number of bits to be used to represent the output.
     * @return Binary string represented as a double array.
     */
    public double[] getIdEncodedInBinary(int numOfBits) {
        double[] result = new double[numOfBits];
        String binaryString = Integer.toBinaryString(id);
        while(binaryString.length() != numOfBits){
            binaryString = "0" + binaryString;
        }
        for(int resultIndex = 0; resultIndex < binaryString.length(); resultIndex++){
            if(binaryString.substring(resultIndex, resultIndex+1).equals("1"))
                result[resultIndex] = 1.0;
            else
                result[resultIndex] = 0.0;
        }
        return result;
    }
}
