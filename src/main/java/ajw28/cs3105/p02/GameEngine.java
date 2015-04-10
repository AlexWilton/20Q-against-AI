package ajw28.cs3105.p02;

import java.util.Scanner;

public class GameEngine {
    private NeuralNet20Q net;
    private Scanner scanner = new Scanner(System.in);

    public GameEngine(NeuralNet20Q net) {
        this.net = net;
    }

    public void startGame(){
        System.out.println("//// Welcome to 20 Questions ////\nPlease think of a (simple) animal. Hold it in your mind and then press enter");
        scanner.nextLine(); //wait for enter to be pressed

        int questionNumber = 1;
        do{
            Question question = net.getNextQuestion();

            System.out.println("\n" + questionNumber + ") " + question);
            boolean answer = getYesOrNoFromUser();
            question.recordQuestionAnswer(answer);

            questionNumber++;
        }while(net.isReadyToGuess());

        Concept guess = net.getBestGuess();
        System.out.println("After " + questionNumber + " questions, I am ready to guess..." +
                "\nI think that you've been thinking of a" + (guess.isFirstLetterOfNameAVowel() ? "n" : "") + guess +
                "\nAm I right? (Yes/No)\n");
        boolean amIRight = getYesOrNoFromUser();
        if(amIRight){
            System.out.println("Fantastic! My creator will be so proud of me! Thank you for playing!");
        }else{
            System.out.println("What were you actually thinking of? (Please help me learn)");
            String realAnswer = "";
            while(realAnswer.length() < 2) realAnswer = scanner.nextLine();
            System.out.println("I've added your answer to my knowledge base (along with the answers which you gave to my questions)");
            net.recordConceptFromHumanAfterAnsweringQuestions(realAnswer);
        }

    }

    private boolean getYesOrNoFromUser(){
        while(true){
            try{
                boolean answer = scanner.nextBoolean();
                return answer;
            }catch (Exception e){
                System.out.println("Please respond either 'yes' or 'no' to the question.");
            }
        }
    }

}
