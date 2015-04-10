package ajw28.cs3105.p02;

import java.util.HashMap;

public class Question{
    private int questionID;
    private String questionToAsk;
    private HashMap<Concept, Boolean> conceptResponses = new HashMap<Concept, Boolean>();
    private double answer = UNANSWERED;
    public static double YES=1, NO=0, UNANSWERED=0.5;

    public Question(int questionID, String questionToAsk){
        this.questionID = questionID;
        this.questionToAsk = questionToAsk;
    }

    public void addResponseForConcept(Concept concept, boolean answer){
        conceptResponses.put(concept, answer);
    }

    public boolean getResponseForConcept(Concept concept){
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
