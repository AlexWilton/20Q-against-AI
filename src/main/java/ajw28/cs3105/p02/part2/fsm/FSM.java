package ajw28.cs3105.p02.part2.fsm;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

public class FSM {

//	private String[] inputSymbols = {"c10", "c20", "c50", "a", "b", "c", "r" };
//    private String[] outputSymbols = {"noOutput", "C10", "C20", "C30", "C40", "C50", "C60", "C70", "C80", "C90", "A", "B", "C"};
    private ArrayList<Transition> transitions = new ArrayList<>();
    public static final String DEFAULT_FSM_FILE_PATH = "vendingMachine.fsm";
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


    private void readInFSMtransitionsFromTableFile(){
        String line;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(tableFilePath));

            //parse table entries
            while ((line = reader.readLine()) != null) {
                if(line.equals("")) continue; //skip empty lines

                String[] columns = line.split(" ");
                State startState = new State(columns[0]);
                String inputSymbol = columns[1];
                String outputSymbol = columns[2];
                State destinationState = new State(columns[3]);
                transitions.add( new Transition(startState, destinationState, inputSymbol, outputSymbol));
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
	
}
