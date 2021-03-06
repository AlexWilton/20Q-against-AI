package ajw28.cs3105.p02.part1;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Neural Network. Must be trained using training data (initially read in from a file). Provides questions to be asked
 * to user and can provide a best guess of what it thinks the current user is thinking of based on received answers.
 */
public class NeuralNet20Q {
    /**
     * Default File Path for question data
     */
    public static final String DEFAULT_Q_FILE_PATH = "20q_data.csv";

    /**
     * Actual File Path (used) for question data
     */
    private String questionFilePath;

    /**
     * Has the set of questions and concepts read in from the question data file changed
     */
    private boolean knowledgeBaseChanged = false;

    /**
     * Set of questions for distinguishing between concepts
     */
    private ArrayList<Question> questions = new ArrayList<Question>();

    /**
     * Set of Concepts a user could be thinking of.
     */
    private ArrayList<Concept> concepts = new ArrayList<Concept>();

    /**
     * Neural Network
     */
    private BasicNetwork net;

    /**
     * Number of input units
     */
    private int numberOfInputUnits;
    /**
     * Number of Hidden users
     */
    private int numberOfHiddenUnits;

    /**
     * Number of Output Units
     */
    private int numberOfOutputUnits;

    /**
     * Maximum allowed error before trained may be allowed to finish
     */
    public static double MAX_ERROR = 0.05;

    /**
     * Create Neural Net using data from default question data file path
     */
    public NeuralNet20Q(){
        this(DEFAULT_Q_FILE_PATH);
    }

    /**
     * Create Neural Net using data from a specified question data file path
     * @param q_file_path Specified question data file path
     */
    public NeuralNet20Q(String q_file_path){
        questionFilePath = q_file_path;
        readInQuestionsAndConceptsFromCSV(q_file_path);
        numberOfInputUnits = questions.size();
        numberOfHiddenUnits = 2;
        numberOfOutputUnits = (int) Math.ceil(Math.log(concepts.size()) / Math.log(2));
        constructNet();
    }

    /**
     * Create and write to a csv file the question data (comprised of all questions and concepts)
     * @param q_file_path File path of output csv file
     */
    private void storeChangesToCSV(String q_file_path){
        try {
            FileWriter writer = new FileWriter(q_file_path);

            //create head line for csv
            writer.append("Question ID,Question");
            for(Concept c : concepts){
                writer.append("," + c.getName());
            }
            writer.append("\n");

            //fill in question and answers for each concept line by line
            for(int i=0; i<questions.size(); i++){
                Question q = questions.get(i);
                writer.append( q.getQuestionID() + "," + q);
                for(Concept c : concepts){
                    writer.append( "," + q.getCorrectResponseForConcept(c));
                }
                writer.append("\n");
            }

            writer.flush();
            writer.close();
        }catch (IOException e){
            System.out.println("Attempt to write knowledge base to file: " + q_file_path + " failed!");
        }
    }

    /**
     * Read in from a csv file the question data (comprised of all questions and concepts)
     * @param q_file_path File path of input csv file
     */
    private void readInQuestionsAndConceptsFromCSV(String q_file_path) {
        String line;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(q_file_path));

            //parse header
            if ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                for(int conceptNum=0; conceptNum < columns.length -2; conceptNum++) {
                    concepts.add(new Concept(conceptNum, columns[conceptNum + 2]));
                }
            }

            //parse question data
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");

                //parse question
                int questionId = Integer.parseInt(columns[0]);
                Question newQuestion = new Question(questionId, columns[1]);
                questions.add(newQuestion);

                //parse question answers
                for(int conceptNum=0; conceptNum < columns.length -2; conceptNum++) {
                    int columnOfConceptAnswer = conceptNum + 2;
                    double answer = Double.parseDouble(columns[columnOfConceptAnswer]);
                    newQuestion.addResponseForConcept(concepts.get(conceptNum), answer);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error! Couldn't find file: " + q_file_path);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Build Neural Network
     */
    private void constructNet(){
        net = new BasicNetwork();

        //construct input units

        net.addLayer(new BasicLayer(null, true, numberOfInputUnits));

        //construct hidden units
        net.addLayer(new BasicLayer(new ActivationSigmoid(), true, numberOfHiddenUnits));

        //construct output units
        net.addLayer(new BasicLayer(new ActivationSigmoid(), false, numberOfOutputUnits));

        net.getStructure().finalizeStructure();
        net.reset();
    }

    /**
     * Train Neural Network using ideal set of question answers for each concept.
     */
    public void train() {
        double[][] netInputs = calculateIdealInputs();
        double[][] netOuts = new double[concepts.size()][numberOfOutputUnits];
        for(int conceptNum=0; conceptNum < concepts.size(); conceptNum++){
            netOuts[conceptNum] = concepts.get(conceptNum).getIdEncodedInBinary(numberOfOutputUnits);
        }

        MLDataSet trainingSet = new BasicMLDataSet(netInputs, netOuts);

        // train the neural network
        while(true){
            Backpropagation trainer = new Backpropagation(net, trainingSet, 0.3, 0.8);

            int numOfEpochsToTrainFor = 10000 + 100 * concepts.size() * numberOfHiddenUnits;
            trainer.iteration(numOfEpochsToTrainFor);
            if(trainer.getError() > MAX_ERROR){
                //add hidden unit and reconstructNet and trainer
                System.out.println("Trained for " + numOfEpochsToTrainFor + " epochs. Maximum Error (" + MAX_ERROR + ") not reached. " +
                        "Increasing number of hidden units from " + numberOfHiddenUnits + " to " + (numberOfHiddenUnits +1) + " and will try again");
                numberOfHiddenUnits++;
                constructNet();
            }else{
                trainer.finishTraining();
                break;
            }
        }

        System.out.println("Neural Network successfully trained!\n");

    }

    /**
     * Calculate array of ideal inputs needed for each concept.
     * @return Array of inputs which should lead to each concept
     */
    private double[][] calculateIdealInputs(){
        double[][] netInputs = new double[concepts.size()][questions.size()];
        for(int conceptNum=0; conceptNum < concepts.size(); conceptNum++){
            Concept concept = concepts.get(conceptNum);
            for(int questionNum=0; questionNum < concepts.size(); questionNum++){
                double val = questions.get(questionNum).getCorrectResponseForConcept(concept);
                netInputs[conceptNum][questionNum] = val;
            }
        }
        return netInputs;
    }

    /**
     * Calculate array of question answers.
     * @return Array of inputs
     */
    private double[] calculateNetInputsFromQuestionAnswers(){
        double[] questionAnswers = new double[questions.size()];
        for(int i=0; i < questionAnswers.length; i++){
            questionAnswers[i] = questions.get(i).getAnswer();
        }
        return questionAnswers;
    }

    /**
     * Determined next question to ask player
     * @return Question. Null is returned if there are no more questions to ask.
     */
    public Question nextQuestion() {
        ArrayList<Question> unaskedQuestions = new ArrayList<Question>();
        for(Question q : questions)
            if(q.getAnswer() == Question.UNANSWERED) unaskedQuestions.add(q);

        /*Calculate how many possible concepts there would be after asking each unasked question*/
        int[] numberOfPossibleConceptsAfterAskingQuestion = new int[unaskedQuestions.size()];
        for(int questionIndex=0; questionIndex < unaskedQuestions.size(); questionIndex++){
            Question currentQuestion = unaskedQuestions.get(questionIndex);
            currentQuestion.recordQuestionAnswer(1.0);
            int remainingConceptsIfAnsweredYes = possibleConceptsForCurrentAnswerSet().size();
            currentQuestion.recordQuestionAnswer(0.0);
            int remainingConceptsIfAnsweredNo = possibleConceptsForCurrentAnswerSet().size();
            currentQuestion.markAnswerAsUnasked();
            numberOfPossibleConceptsAfterAskingQuestion[questionIndex] = remainingConceptsIfAnsweredYes + remainingConceptsIfAnsweredNo;
        }

        /*Find and return question eliminating the largest number of concepts*/
        int smallestNum = Integer.MAX_VALUE;
        ArrayList<Question> questionsEliminatingMostConcepts = new ArrayList<>();
        for(int i=0; i < numberOfPossibleConceptsAfterAskingQuestion.length; i++){
            if(numberOfPossibleConceptsAfterAskingQuestion[i] < smallestNum) {
                smallestNum = numberOfPossibleConceptsAfterAskingQuestion[i];
                questionsEliminatingMostConcepts = new ArrayList<>();
            }
            if(numberOfPossibleConceptsAfterAskingQuestion[i] == smallestNum)
                questionsEliminatingMostConcepts.add(unaskedQuestions.get(i));

        }

        if(questionsEliminatingMostConcepts.size() == 0) return null; //return null if there are no possible questions to ask.

        Random random = new Random();
        return questionsEliminatingMostConcepts.get(random.nextInt(questionsEliminatingMostConcepts.size()));
    }

    /**
     * Rule out concepts based on already received answers to questions.
     * @return HashSet of Possible Concepts
     */
    private HashSet<Concept> possibleConceptsForCurrentAnswerSet(){
        HashSet<Concept> possibleConcepts = new HashSet<Concept>();
        possibleConcepts.addAll(concepts);
        for(Question question : questions){
            for(Concept concept : concepts) {
                double correctResponseForConcept = question.getCorrectResponseForConcept(concept);
                double currentAnswerForQuestion = question.getAnswer();
                if(        (correctResponseForConcept == Question.YES && currentAnswerForQuestion == Question.NO)
                        || (correctResponseForConcept == Question.NO && currentAnswerForQuestion == Question.YES)){
                    possibleConcepts.remove(concept);
                }
            }
        }
        return possibleConcepts;
    }

    /**
     * Obtain a set of questions which are unanswered
     * @return Set of unanswered questions
     */
    private HashSet<Question> unansweredQuestions(){
        HashSet<Question> unansweredQuestions = new HashSet<>();
        for(Question q : questions)
            if(q.getAnswer() == Question.UNANSWERED)
                unansweredQuestions.add(q);
        return unansweredQuestions;
    }


    /**
     * Check whether nextQuestion() will return a question
     * @see #nextQuestion()
     * @return true only if nextQuestion() will return a question
     */
    public Boolean hasNextQuestion(){
        return nextQuestion() != null;
    }

    /**
     * Determine whether the neural network is ready to make a guess at the player's concept.
     */
    public boolean isReadyToGuess() {
        /*Make guess if there's only one possible answer or cut losses if net doesn't know concept*/
        HashSet<Concept> possibleConcepts = possibleConceptsForCurrentAnswerSet();
        switch (possibleConcepts.size()){
            case 0:
                return true; //cut losses and ask player for concept
            case 1:
                return true;
            default:
                return false; //if there are more than more possible concepts, keep guessing.
        }
    }

    /**
     * Use the neural network to find out what concept the player is most likely thinking of
     * @return Concept the player is most likely thinking of.
     */
    public Concept makeBestGuess() {
        HashSet<Question> unansweredQuestions = unansweredQuestions();
        for(Question q : unansweredQuestions)
            q.recordQuestionAnswer(q.getCorrectResponseForConcept(possibleConceptsForCurrentAnswerSet().iterator().next()));
        MLData netOutput = net.compute(new BasicMLData(calculateNetInputsFromQuestionAnswers()));
        for(Question q : unansweredQuestions)
            q.markAnswerAsUnasked();
        int conceptId = Concept.convertOutputToID(netOutput);
        return concepts.get(conceptId);
    }

    /**
     * Record a new concept and question provided by a human from a failed 20 questions game.
     * @param conceptName Name of Concept the player was thinking of
     * @param question Question provided by player, answer is yes for this concept and no for the concept guessed by the system
     */
    public void recordConceptAndQuestionFromHuman(String conceptName, String question) {
        knowledgeBaseChanged = true;
        int unusedConceptId = concepts.size() + 1;
        Concept newConcept = new Concept(unusedConceptId, conceptName);

        //add response for new concept to all existing question using answers given by human
        for(Question q : questions){
            q.addResponseForConcept(newConcept, q.getAnswer());
        }

        //create and add new question
        Concept guessedConcept = makeBestGuess();
        int unusedQuestionId = questions.size() + 1;
        Question newQuestion = new Question(unusedQuestionId, question);
        for(Concept c : concepts) {
            if(c != guessedConcept)
                newQuestion.addResponseForConcept(c, 0.5);
        }
        newQuestion.addResponseForConcept(newConcept, 1);
        newQuestion.addResponseForConcept(guessedConcept, 0);
        questions.add(newQuestion);
        concepts.add(newConcept);
    }

    /**
     * Saves new question and answer set to original file which was used to load in the
     * information when the net was constructed. Only writes to file if changes have been made.
     */
    public void saveKnowledgeBase(){
        if(knowledgeBaseChanged)
            storeChangesToCSV(questionFilePath);
    }

    /**
     * File Path of Neural Network's data file
     * @return File Path as a String
     */
    public String getQuestionFilePath() {
        return questionFilePath;
    }

    /**
     * Mark all questions as unanswered
     */
    public void resetAllQuestions(){
        for(Question q : questions){
            q.markAnswerAsUnasked();
        }
    }

    /**
     * Generates a Concept Output Table
     */
    public void generateConceptOutputTable() {
        try {
        FileWriter writer = new FileWriter("conceptOutputTable.csv");

        //create head line for csv
        writer.append("Concept ID,Concept");
        for(int i=1; i <= numberOfOutputUnits; i++) writer.append(", Output Unit " + i);
        writer.append("\n");

        //fill in question and answers for each concept line by line
        for(Concept c : concepts){
            writer.append(c.getConceptID() + "," + c.getName());
            double[] binaryId = c.getIdEncodedInBinary(numberOfOutputUnits);
            for(int i=1; i <= numberOfOutputUnits; i++){
                writer.append("," + binaryId[i-1]);
            }
            writer.append("\n");
        }


        writer.flush();
        writer.close();
    }catch (IOException e){
        System.out.println("Attempt to write concept output table failed!");
    }
    }
}
