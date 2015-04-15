package ajw28.cs3105.p02.part1;

import org.encog.ml.data.MLData;

/**
 * Concept represents a concept that a human could be thinking about when playing 20 Questions.
 * Every concept has a unique identifier (ID) and a name.
 */
public class Concept {
    /**
     * Concept ID
     */
    private int conceptID;

    /**
     * Concept Name
     */
    private String name;

    /**
     * Create a Concept for a given ID and Name
     * @param conceptID Concept ID
     * @param name Concept Name
     */
    public Concept(int conceptID, String name) {
        this.conceptID = conceptID;
        this.name = name;
    }

    /**
     * Construct a binary string represented as a double array from the concept's ID.
     * @param numOfBits number of bits to be used to represent the output.
     * @return Binary string represented as a double array.
     */
    public double[] getIdEncodedInBinary(int numOfBits) {
        double[] result = new double[numOfBits];
        String binaryString = Integer.toBinaryString(conceptID);
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
     * Name of Concept
     * @return Concept Name
     */
    public String getName(){
        return name;
    }

    /**
     * Name of Concept
     * @return Concept Name
     */
    public String toString(){
        return name;
    }

    /**
     * Check whether the first letter of the concept name is a vowel.
     * @return True only if first letter is a vowel.
     */
    public boolean isFirstLetterOfNameAVowel(){
        switch (name.substring(0,1).toUpperCase()){
            case "A": case "E": case "O": case "U": case "I":
                return true;
            default:
                return false;

        }
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
