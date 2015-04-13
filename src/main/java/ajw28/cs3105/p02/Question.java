package ajw28.cs3105.p02;

import java.util.HashMap;

/**
 * Question represents a question that could be asked to a human.
 * Every question has a unique identifier (ID) and a string of the question to ask the human.
 */
public class Question{

    /**
     * Question ID
     */
    private int questionID;

    /**
     * Question As String
     */
    private String questionToAsk;

    /**
     * Correct answer which should be given for each concept
     */
    private HashMap<Concept, Double> conceptResponses = new HashMap<Concept, Double>();

    /**
     * Answer provided by human
     */
    private double answer = UNANSWERED;

    /**
     * Possible answer to question.
     */
    public static double YES=1, NO=0, UNANSWERED=0.5;

    /**
     * Create a question from a ID and question as a string
     * @param questionID Question ID
     * @param questionAsString Question As String
     */
    public Question(int questionID, String questionAsString){
        this.questionID = questionID;
        this.questionToAsk = questionAsString;
    }

    /**
     * Record ideal answer for when answering this question with the given concept in mind.
     * @param concept   Concept
     * @param answer    Answers (0 for No, 0.5 for Unknown and 1 for Yes)
     */
    public void addResponseForConcept(Concept concept, double answer){
        conceptResponses.put(concept, answer);
    }

    /**
     * Obtain the correct answer which should be given for a particular concept
     * @param concept   Concept
     * @return  Answer (0 for No, 0.5 for Unknown and 1 for Yes)
     */
    public double getResponseForConcept(Concept concept){
        return conceptResponses.get(concept);
    }

    /**
     * Question As String
     * @return  Question As String
     */
    public String toString() {
        return questionToAsk;
    }

    /**
     * Record response from User
     * @param didUserRespondYes True for yes and False for no
     */
    public void recordQuestionAnswer(boolean didUserRespondYes){
        if(didUserRespondYes)
            answer = YES;
        else
            answer = NO;
    }

    /**
     * Get Answer made by player
     * @return Answer
     */
    public double getAnswer() {
        return answer;
    }

    /**
     * Get Question ID
     * @return Question ID
     */
    public int getQuestionID() {
        return questionID;
    }
}
