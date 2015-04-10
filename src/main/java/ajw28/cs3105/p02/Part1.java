package ajw28.cs3105.p02;

public class Part1 {
    public static void main(String[] args){
        NeuralNet20Q neuralNet20Q = new NeuralNet20Q(); //load in default questions + concepts
        neuralNet20Q.train();
        GameEngine gameEngine = new GameEngine(neuralNet20Q);
        gameEngine.startGame();

    }

}

