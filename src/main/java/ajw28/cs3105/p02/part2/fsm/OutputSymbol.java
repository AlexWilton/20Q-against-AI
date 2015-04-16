package ajw28.cs3105.p02.part2.fsm;

import org.encog.ml.data.MLData;

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


    /**
     * Converts neural network output (a sequence of binary digits) to an integer
     * @param netOuput Encog representation of a sequence of binary digits (outputed from the @see BasicNetwork.compute() method)
     * @return Integer ID for the concept determined by the neural network.
     */
    public static int convertOutputToID(MLData netOuput) {
        double[] output = netOuput.getData();
        String binaryString = "";
        for(double b_digit : output){
            if(b_digit >= 0.5)
                binaryString += "1";
            else
                binaryString += "0";
        }
        return Integer.parseInt(binaryString, 2);
    }
}
