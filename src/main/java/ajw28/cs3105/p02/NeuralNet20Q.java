package ajw28.cs3105.p02;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NeuralNet20Q {
    public static final String DEFAULT_Q_FILE_PATH = "20q_data.csv";

    private ArrayList<Question> questions = new ArrayList<Question>();
    private ArrayList<Concept> concepts = new ArrayList<Concept>();
    private BasicNetwork net;
    private int numberOfInputUnits, numberOfHiddenUnits, numberOfOutputUnits;

    public NeuralNet20Q(){
        this(DEFAULT_Q_FILE_PATH);
    }

    public NeuralNet20Q(String q_file_path){
        readInQuestionsAndConceptsFromCSV(q_file_path);
        numberOfInputUnits = questions.size();
        numberOfHiddenUnits = 1;
        numberOfOutputUnits = (int) Math.ceil(Math.log(concepts.size()) / Math.log(2));
        constructNet();
    }

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
                    boolean answer = false;
                    if(columns[columnOfConceptAnswer].equals("1")) answer = true;
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

    public void train() {
        double[][] netInputs = new double[concepts.size()][questions.size()];
        for(int conceptNum=0; conceptNum < concepts.size(); conceptNum++){
            Concept concept = concepts.get(conceptNum);
            for(int questionNum=0; questionNum < concepts.size(); questionNum++){
                double val = 0.0;
                if(questions.get(questionNum).getResponseForConcept(concept)) val = 1.0;
                netInputs[conceptNum][questionNum] = val;
            }
        }

        double[][] netOuts = new double[concepts.size()][numberOfOutputUnits];
        for(int conceptNum=0; conceptNum < concepts.size(); conceptNum++){
            netOuts[conceptNum] = concepts.get(conceptNum).getIdEncodedInBinary(numberOfOutputUnits);
        }

        MLDataSet trainingSet = new BasicMLDataSet(netInputs, netOuts);

        // train the neural network
        ResilientPropagation trainer = new ResilientPropagation(net, trainingSet);
        double MAX_ERROR = 0.04;
        while(true){
            int numOfEpochsToTrainFor = 10000 + 100 * concepts.size() * concepts.size() * numberOfHiddenUnits * numberOfHiddenUnits;
            trainer.iteration(numOfEpochsToTrainFor);
            if(trainer.getError() > MAX_ERROR){
                //add hidden unit and reconstructNet and trainer
                System.out.println("Trained for " + numOfEpochsToTrainFor + " epochs. Maximum Error (" + MAX_ERROR + ") not reached. " +
                        "Increasing number of hidden units from " + numberOfHiddenUnits + " to " + (numberOfHiddenUnits +1) + " and will try again");
                numberOfHiddenUnits++;
                constructNet();
                trainer = new ResilientPropagation(net, trainingSet);
            }else{
                trainer.finishTraining();
                break;
            }
        }

        // test the neural network
        System.out.println("Neural Network Results:");
        for(MLDataPair pair: trainingSet ) {
            final MLData output = net.compute(pair.getInput());
            System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
                    + ", actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
        }

        Encog.getInstance().shutdown();
    }

    private void addHiddenUnit(){

    }

}
