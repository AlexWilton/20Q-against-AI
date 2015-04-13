package ajw28.cs3105.p02.part1;

import java.util.Scanner;

/**
 * Game Engine facilitates game play. Including asking human player questions, interpreting responses
 * and relating to the 20 questions neural network.
 */
public class GameEngine {

    /**
     * Neural Net which is used for obtaining questions, depositing answers and guessing player's concept.
     */
    private NeuralNet20Q net;

    /**
     * Scanner used to obtain input from player
     */
    private Scanner scanner = new Scanner(System.in);

    /**
     * Construct a Game Engine for a given neural network
     * @param net Neural Network
     */
    public GameEngine(NeuralNet20Q net) {
        this.net = net;
    }

    /**
     * Start game play experience for human playing making use of standard input and output.
     * Questions are asked, answers recorded and knowledge base updated if neural network is wrong.
     */
    public void playGame(){
        System.out.println("//// Welcome to 20 Questions ////" +
                "\nPlease think of a (simple) animal. Hold it in your mind and then press enter");
        scanner.nextLine(); //wait for enter to be pressed
        System.out.println("Please respond 'yes' (or 'y') or 'no' (or 'n') to every question'. (I won't understand anything else)");

        int questionNumber = 1;
        do{
            Question question = net.nextQuestion();

            System.out.println("\n" + questionNumber + ") " + question);
            boolean answer = getYesOrNoFromUser();
            question.recordQuestionAnswer(answer);

            questionNumber++;
        }while(net.hasNextQuestion() && !net.isReadyToGuess() && questionNumber <= 20);

        Concept guess = net.makeBestGuess();
        System.out.println("After " + (questionNumber-1) + " questions, I am ready to guess..." +
                "\nI think that you've been thinking of a" + (guess.isFirstLetterOfNameAVowel() ? "n " : " ") + guess +
                "\nAm I right? (Yes/No)\n");
        boolean amIRight = getYesOrNoFromUser();
        if(amIRight){
            System.out.println("Fantastic! My creator will be so proud of me! Thank you for playing!");
        }else{
            System.out.println("What were you actually thinking of? (Please help me learn)");
            String realAnswer = "";
            while(realAnswer.length() < 2) realAnswer = scanner.nextLine();
            boolean needN = (new Concept(-1, realAnswer).isFirstLetterOfNameAVowel());

            System.out.println("So you were thinking of a" + (needN ? "n " : " ") + realAnswer + "" +
                            "\nI would to add this question to my knowledge base, however, I'm having trouble distinguishing this concept from another concept." +
                    "\nPlease provide me with a question where the answer is yes for " + realAnswer + " and no for " + guess);

            String newQuestion = "";
            while(newQuestion.length() < 7) newQuestion = scanner.nextLine();


            System.out.println("I've added your answer and question to my knowledge base (along with the answers which you gave to my questions)" +
                    "\nThanks for playing, have a great day!");
            net.recordConceptAndQuestionFromHuman(realAnswer, newQuestion);
        }

    }

    /**
     * A helper method for waiting for a boolean input from user.
     * @return Boolean parsed from user's response in standard input
     */
    private boolean getYesOrNoFromUser(){
        String line;
        while( (line = scanner.nextLine()) != "") {
            if (line.equalsIgnoreCase("y") || line.equalsIgnoreCase("yes")) {
                return true;
            } else if (line.equalsIgnoreCase("n") || line.equalsIgnoreCase("no")) {
                return false;
            }
        }
        System.out.println("Failed to read Input from user correctly. Answer 'no' assumed.");
        return false;
    }

}
