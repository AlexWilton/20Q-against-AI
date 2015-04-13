package ajw28.cs3105.p02.part1;

/**
 * Part 1 contains the main method for demonstrating the meeting of Part 1 Specifications of the practical
 */
public class Part1 {

    /**
     * Create a Neural Network. Train it. Launch an instance of 20 questions
     * and save any knowledge learnt from the game.
     * @param args No arguments are needed
     */
    public static void main(String[] args){
        NeuralNet20Q neuralNet20Q = new NeuralNet20Q(); //load in default questions + concepts
        neuralNet20Q.train();
        GameEngine gameEngine = new GameEngine(neuralNet20Q);
        gameEngine.playGame();
        neuralNet20Q.saveKnowledgeBase();
    }

}

