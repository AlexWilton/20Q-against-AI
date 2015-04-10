package ajw28.cs3105.p02;

import java.util.HashMap;

public class Question{
    private int questionID;
    private String questionToAsk;
    private HashMap<Concept, Boolean> conceptResponses = new HashMap<Concept, Boolean>();
    private ANSWER answer = ANSWER.UNANSWER;
    public enum ANSWER { YES, NO, UNANSWER };

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
            answer = ANSWER.YES;
        else
            answer = ANSWER.NO;
    }

}
