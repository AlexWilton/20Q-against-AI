package ajw28.cs3105.p02.part2.fsm;

import java.io.*;
import java.util.ArrayList;

/**
 * Finite State Machine.
 * Reads in and stores a set of transitions defining a FSM.
 */
public class FSM {

    /**
     * Transitions making up FSM
     */
    private ArrayList<Transition> transitions = new ArrayList<>();

    /**
     * List of States used in FSM
     */
    private ArrayList<State> states = new ArrayList<>();

    /**
     * List of Input Symbols used in FSM
     */
    private ArrayList<InputSymbol> inputSymbols = new ArrayList<>();

    /**
     * List of Output Symbols used in FSM
     */
    private ArrayList<OutputSymbol> outputSymbols = new ArrayList<>();


    /**
     * Default File Path for Transition Data
     */
    public static final String DEFAULT_FSM_FILE_PATH = "vendingMachine.fsm";

    /**
     * Actual File Path for Transition Data
     */
    private String tableFilePath;

    /**
     * Create Finite State Machine using default FSM file path
     */
    public FSM(){
        this(DEFAULT_FSM_FILE_PATH);
    }

    /**
     * Create Finite State Machine using custom FSM file path
     * @param FSM_FILE_PATH Specified FSM data file path
     */
    public FSM(String FSM_FILE_PATH){
        tableFilePath = FSM_FILE_PATH;
        readInFSMtransitionsFromTableFile();
    }

    /**
     * Read in FSM transitions from data file
     */
    private void readInFSMtransitionsFromTableFile(){
        String line;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(tableFilePath));

            //parse table entries
            while ((line = reader.readLine()) != null) {
                if(line.equals("")) continue; //skip empty lines

                String[] columns = line.split(" ");
                String startStateName = columns[0];
                State startState = getStateUsingStateName(startStateName);
                if(startState == null){
                    startState = new State(states.size(), startStateName);
                    states.add(startState);
                }

                String endStateName = columns[3];
                State destinationState = getStateUsingStateName(endStateName);
                if(destinationState == null) {
                    destinationState = new State(states.size(), endStateName);
                    states.add(destinationState);
                }
                
                
                String inputSymbolString = columns[1];
                InputSymbol inputSymbol = getInputSymbolUsingStringRepresentation(inputSymbolString);
                if(inputSymbol == null){
                    inputSymbol = new InputSymbol(inputSymbols.size(), inputSymbolString);
                }
                String outputSymbolString = columns[2];
                OutputSymbol outputSymbol = getOutputSymbolUsingStringRepresentation(outputSymbolString);
                if(outputSymbol == null){
                    outputSymbol = new OutputSymbol(outputSymbols.size(), outputSymbolString);
                }
                transitions.add( new Transition(transitions.size(), startState, destinationState, inputSymbol, outputSymbol));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error! Couldn't find file: " + tableFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get State from the FSM list of states bases on a given name
     * @param name Name of state to be used for matching
     * @return Found State. (Null if not found)
     */
    private State getStateUsingStateName(String name){
        for(State s : states){
            if(s.getName().equals(name)) return s;
        }
        return null;
    }

    /**
     * Get Input Symbol from the FSM list of input symbols give its string representation
     * @param inputSymbolString string representation of input symbol to be used for matching
     * @return Found Input Symbol. (Null if not found)
     */
    private InputSymbol getInputSymbolUsingStringRepresentation(String inputSymbolString){
        for(InputSymbol inputSymbol : inputSymbols){
            if(inputSymbol.getStrRepresentation().equals(inputSymbolString)) return inputSymbol;
        }
        return null;
    }

    /**
     * Get Output Symbol from the FSM list of output symbols give its string representation
     * @param outputSymbolString string representation of output symbol to be used for matching
     * @return Found Output Symbol. (Null if not found)
     */
    private OutputSymbol getOutputSymbolUsingStringRepresentation(String outputSymbolString){
        for(OutputSymbol outputSymbol : outputSymbols){
            if(outputSymbol.getStrRepresentation().equals(outputSymbolString)) return outputSymbol;
        }
        return null;
    }

    /**
     * Get list of Transitions defining the FSM
     * @return list of Transitions defining the FSM
     */
    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    /**
     * Get list of state in the FSM
     * @return List of states in the FSM
     */
    public ArrayList<State> getStates() {
        return states;
    }
}
