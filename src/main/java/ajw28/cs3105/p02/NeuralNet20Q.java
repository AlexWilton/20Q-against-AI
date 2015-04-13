package ajw28.cs3105.p02;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

import java.io.*;
import java.util.ArrayList;

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
        numberOfHiddenUnits = 1;
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
                    writer.append( "," + q.getResponseForConcept(c));
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

            int numOfEpochsToTrainFor = 10000 + 100 * concepts.size() * concepts.size() * numberOfHiddenUnits * numberOfHiddenUnits;
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
                double val = questions.get(questionNum).getResponseForConcept(concept);
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
     * @return Question
     */
    public Question nextQuestion() {
        //current implementation: find first unanswered questions TODO Choose questions that eliminates the most alternatives at each stage
        for(Question q : questions){
            if(q.getAnswer() == 0.5)
                return q;
        }
        return null; //no questions left to answer.
    }

    /**
     * Check whether nextQuestion() will return a question
     * @see #nextQuestion()
     * @return true only if nextQuestion() will return a question
     */
    public Boolean hasNextQuestion(){
        if(nextQuestion() == null)
            return false;
        else
            return true;
    }

    /**
     * Determine whether the neural network is ready to make a guess at the player's concept
     */
    public boolean isReadyToGuess() {
        //TODO find the number of concepts possible for the current set of answers, if the size < 2 return true, else return false
        return false;
    }

    /**
     * Use the netural network to find out what concept the player is most likely thinking of
     * @return Concept the player is most likely thinkning of
     */
    public Concept makeBestGuess() {
        MLData netOuput = net.compute(new BasicMLData(calculateNetInputsFromQuestionAnswers()));
        int conceptId = Concept.convertOutputToID(netOuput);
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
        int unusedQuestionId = questions.size();
        Question newQuestion = new Question(unusedQuestionId, question);
        for(Concept c : concepts) {
            if(c != guessedConcept)
                newQuestion.addResponseForConcept(c, 0.5);
        }
        newQuestion.addResponseForConcept(newConcept, 1);
        newQuestion.addResponseForConcept(guessedConcept, 0);
        questions.add(newQuestion);



        concepts.add(newConcept);
        System.out.println("Concept: " + conceptName + " and question: " + question + " recorded in data structure");
    }

    /**
     * Saves new question and answer set to original file which was used to load in the
     * information when the net was constructed. Only writes to file if changes have been made.
     */
    public void saveKnowledgeBase(){
        if(knowledgeBaseChanged)
            storeChangesToCSV(questionFilePath);
    }
}
