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
        System.out.println("Please respond 'true' or 'false' to every question'. (I won't understand anything else)");

        int questionNumber = 1;
        do{
            Question question = net.nextQuestion();

            System.out.println("\n" + questionNumber + ") " + question);
            boolean answer = getYesOrNoFromUser();
            question.recordQuestionAnswer(answer);

            questionNumber++;
        }while(net.hasNextQuestion() && questionNumber <= 20);

        Concept guess = net.makeBestGuess();
        System.out.println("After " + (questionNumber-1) + " questions, I am ready to guess..." +
                "\nI think that you've been thinking of a" + (guess.isFirstLetterOfNameAVowel() ? "n " : " ") + guess +
                "\nAm I right? (True/False)\n");
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

    private boolean getYesOrNoFromUser(){
        while(true){
            try{
                boolean answer;
                while(!scanner.hasNextBoolean()){}
                    answer = scanner.nextBoolean();

                return answer;
            }catch (Exception e){
                System.out.println("Please respond either 'true' or 'false' to the question.");

            }
        }
    }

}
