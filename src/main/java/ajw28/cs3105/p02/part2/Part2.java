package ajw28.cs3105.p02.part2;

import ajw28.cs3105.p02.part2.fsm.FSM;

/**
 * Part 2 contains the main method for demonstrating the meeting of Part 2 Specifications of the practical
 */
public class Part2 {

    /**
     * Create a Neural Network. Train it. Launch an instance of 20 questions
     * and save any knowledge learnt from the game.
     * @param args No arguments are needed
     */
    public static void main(String[] args){
        FSM fsm = new FSM();
        SelfProgrammingNeuralNet net = new SelfProgrammingNeuralNet(fsm);
        net.train();
    }

}
