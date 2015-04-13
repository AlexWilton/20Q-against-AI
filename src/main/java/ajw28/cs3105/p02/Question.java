package ajw28.cs3105.p02;

import java.util.HashMap;

public class Question{
    private int questionID;
    private String questionToAsk;
    private HashMap<Concept, Double> conceptResponses = new HashMap<Concept, Double>();
    private double answer = UNANSWERED;
    public static double YES=1, NO=0, UNANSWERED=0.5;

    public Question(int questionID, String questionToAsk){
        this.questionID = questionID;
        this.questionToAsk = questionToAsk;
    }

    /**
     * Record ideal answer for when answering this question with the given concept in mind.
     * @param concept   Concept
     * @param answer    Answers (0 for No, 0.5 for Unknown and 1 for Yes)
     */
    public void addResponseForConcept(Concept concept, double answer){
        conceptResponses.put(concept, answer);
    }

    public double getResponseForConcept(Concept concept){
        return conceptResponses.get(concept);
    }

    public String toString() {
        return questionToAsk;
    }

    public void recordQuestionAnswer(boolean didUserRespondYes){
        if(didUserRespondYes)
            answer = YES;
        else
            answer = NO;
    }

    public double getAnswer() {
        return answer;
    }
}
