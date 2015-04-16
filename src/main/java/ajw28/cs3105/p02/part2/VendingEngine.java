package ajw28.cs3105.p02.part2;

import ajw28.cs3105.p02.part2.fsm.InputSymbol;
import ajw28.cs3105.p02.part2.fsm.OutputSymbol;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Vending Engine
 */
public class VendingEngine {

    /**
     * Self Programming Neural Net which is used to simulate a vending machine
     */
    private SelfProgrammingNeuralNet net;

    /**
     * Scanner used to obtain input from player
     */
    private Scanner scanner = new Scanner(System.in);

    /**
     * Construct a Vending Engine for a given neural network
     * @param net Neural Network
     */
    public VendingEngine(SelfProgrammingNeuralNet net) {
        this.net = net;
    }

    /**
     * Start vending machine simulator
     */
    public void startVending(){
        System.out.println("//// Welcome to Vending Machine Simulator 2015 ////\n");
        ArrayList<InputSymbol> availableInputs = net.getPossibleInputs();

        while(true) {
            for (int i = 0; i < availableInputs.size(); i++) {
                System.out.println(i + ") " + availableInputs.get(i).getStrRepresentation());
            }
            System.out.println("Choose an input to put into the vending machine. " +
                    "(Press a number corresponding to a desired input and press enter)\n");

            int choice = -1;
            while (choice < 0 || choice > availableInputs.size() - 1) choice = getNumberFromUser();

            OutputSymbol outputSymbol = net.interactWithMachine(availableInputs.get(choice));
            System.out.println("Output received from machine: " + outputSymbol.getStrRepresentation());


        }
    }

    /**
     * A helper method for waiting for a number input from user.
     * @return Number parsed from user's response in standard input
     */
    private int getNumberFromUser(){
        String line;
        while( (line = scanner.nextLine()) != "") {
            try {
                int result = Integer.parseInt(line);
                return result;
            }catch(NumberFormatException e){System.out.println("Number not recognised, please try again\n");}
        }
        System.out.println("Failed to read Input from user correctly. Answer 'no' assumed.");
        return -1;
    }



}
