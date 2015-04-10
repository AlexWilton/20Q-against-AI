package ajw28.cs3105.p02;

public class Concept {
    private int conceptID;
    private String name;

    public Concept(int conceptID, String name) {
        this.conceptID = conceptID;
        this.name = name;
    }

    public double[] getIdEncodedInBinary(int numOfBits) {
        double[] result = new double[numOfBits];
        String binaryString = Integer.toBinaryString(conceptID);
        for(int resultIndex = 0; resultIndex < binaryString.length(); resultIndex++){
            if(binaryString.substring(resultIndex, resultIndex+1).equals("1"))
                result[resultIndex] = 1.0;
            else
                result[resultIndex] = 0.0;
        }

        return result;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        return name;
    }

    public boolean isFirstLetterOfNameAVowel(){
        switch (name.substring(0,1).toUpperCase()){
            case "A": case "E": case "O": case "U": case "I":
                return true;
            default:
                return false;

        }
    }
}
